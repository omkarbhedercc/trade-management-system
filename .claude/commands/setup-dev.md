---
description: One-time setup of the TMS dev environment on a fresh machine (container engine, Postgres image + container, prereq checks, env files, frontend deps, Maven cache). Run this once, then use /start-dev.
allowed-tools: Bash
---

## Task: Bootstrap the TMS Dev Environment

Prepare a **fresh machine** so that `/start-dev` works. This checks prerequisites, sets up the
container engine + Postgres, creates env files, installs frontend dependencies, and warms the
Maven cache.

Run every step. At the end, print a clear **readiness report** and tell the user to run `/start-dev`.

Ports: frontend **4000**, backend **4001**, database **4002 → 5432**.

> Work from the repo root. Resolve it once and reuse it (never hardcode a home path):
> ```bash
> ROOT="$(git rev-parse --show-toplevel)"; echo "repo root: $ROOT"
> ```

---

### Step 1: Detect OS and package manager

```bash
OS="$(uname -s)"
echo "OS: $OS"
if [ "$OS" = "Darwin" ]; then
  command -v brew >/dev/null 2>&1 && echo "pkg: brew" || echo "pkg: brew MISSING (install from https://brew.sh)"
elif [ "$OS" = "Linux" ]; then
  command -v apt-get >/dev/null 2>&1 && echo "pkg: apt" \
    || command -v dnf >/dev/null 2>&1 && echo "pkg: dnf" \
    || echo "pkg: unknown (install prereqs manually)"
fi
```

Remember the package manager — use it below for anything missing. On macOS you may auto-install
missing tools with `brew install ...`. On Linux, **print** the exact `apt-get`/`dnf` command and ask
the user to run it (don't sudo without consent).

---

### Step 2: Container engine (podman preferred, docker fallback)

The lifecycle commands (`/start-dev`, `/stop-dev`, `/restart-dev`) auto-detect the engine, but
**podman is preferred** (matches the project). Detect it:

```bash
if command -v podman >/dev/null 2>&1; then ENGINE=podman
elif command -v docker >/dev/null 2>&1; then ENGINE=docker
else ENGINE=""; fi
echo "engine: ${ENGINE:-NONE}"
```

**If neither is present**, install one and stop for the user to complete engine setup:
- macOS: `brew install podman`
- Linux (apt): `sudo apt-get install -y podman`  ·  (dnf): `sudo dnf install -y podman`

**podman on macOS needs a VM.** Ensure it exists and is running:

```bash
if [ "$ENGINE" = "podman" ] && [ "$(uname -s)" = "Darwin" ]; then
  podman machine inspect >/dev/null 2>&1 || podman machine init
  podman machine start 2>/dev/null || true      # ignore "already running"
fi
$ENGINE info >/dev/null 2>&1 && echo "engine OK" || echo "engine NOT ready — resolve before continuing"
```

---

### Step 3: Check backend prerequisites (JDK, Maven, git)

The backend is Spring Boot 2.3.4 targeting **Java 8 bytecode**. A JDK **11** is the safe choice
(8 also works; 17+ can hit reflective-access issues at runtime with this old Spring line). Maven 3.6+.

```bash
echo "--- backend prereqs ---"
java -version 2>&1 | head -1 || echo "java: MISSING"
mvn -version 2>&1 | head -1 || echo "maven: MISSING"
git --version 2>&1 | head -1 || echo "git: MISSING"
```

If missing:
- macOS: `brew install openjdk@11 maven git` (follow brew's note to symlink the JDK)
- Linux (apt): `sudo apt-get install -y openjdk-11-jdk maven git`
- Linux (dnf): `sudo dnf install -y java-11-openjdk-devel maven git`

Confirm `java -version` reports 11.x (or at least 8–17) before continuing.

---

### Step 4: Check frontend prerequisites (Node, npm, tmux)

CRA `react-scripts@3.4.1` is happiest on **Node 14–16** (then a plain `npm start` works). It also
runs on Node 18/20 — `/start-dev` auto-adds `--openssl-legacy-provider` when Node ≥ 17. **tmux** is
required (the lifecycle commands run services in it).

```bash
echo "--- frontend prereqs ---"
node -v 2>&1 || echo "node: MISSING"
npm -v 2>&1 || echo "npm: MISSING"
tmux -V 2>&1 || echo "tmux: MISSING"
```

Node guidance:
- **If `nvm` is available**, prefer Node 16 and make it the default (so tmux panes pick it up):
  ```bash
  if command -v nvm >/dev/null 2>&1 || [ -s "$HOME/.nvm/nvm.sh" ]; then
    . "$HOME/.nvm/nvm.sh" 2>/dev/null || true
    nvm install 16 && nvm alias default 16
    node -v
  fi
  ```
- **No nvm, Node missing**: macOS `brew install node@16` · Linux — install nvm (https://github.com/nvm-sh/nvm) then `nvm install 16`.
- **Node ≥ 17 already installed and you can't change it**: fine — `/start-dev` handles the OpenSSL flag automatically.

tmux if missing: macOS `brew install tmux` · Linux `sudo apt-get install -y tmux` (or `dnf`).

---

### Step 5: Pull the Postgres image + create the container

```bash
$ENGINE pull postgres:15-alpine

if $ENGINE ps -a --format "{{.Names}}" | grep -qE "^tms-postgres$"; then
  echo "tms-postgres already exists — starting it"
  $ENGINE start tms-postgres
else
  $ENGINE run -d --name tms-postgres \
    -e POSTGRES_DB=tms_dev \
    -e POSTGRES_USER=tms \
    -e POSTGRES_PASSWORD=tms \
    -p 4002:5432 \
    postgres:15-alpine
fi

# Wait for readiness
for i in $(seq 1 30); do
  $ENGINE exec tms-postgres pg_isready -U tms >/dev/null 2>&1 && { echo "postgres ready"; break; }
  sleep 1
done
```

---

### Step 6: Frontend env + dependencies

Ensure `frontend/.env` exists (it's committed, but create it if a teammate stripped it), then install
node modules (this is what makes `/start-dev`'s `npm start` work — `node_modules/` is gitignored):

```bash
ROOT="$(git rev-parse --show-toplevel)"
if [ ! -f "$ROOT/frontend/.env" ]; then
  printf 'PORT=4000\nREACT_APP_API_BASE=http://localhost:4001/api\nSKIP_PREFLIGHT_CHECK=true\n' > "$ROOT/frontend/.env"
  echo "created frontend/.env"
fi

cd "$ROOT/frontend"
# Use the OpenSSL flag only on Node >= 17 (Node <=16 does not know it and would error)
NODE_MAJOR="$(node -p 'process.versions.node.split(".")[0]' 2>/dev/null || echo 0)"
[ "$NODE_MAJOR" -ge 17 ] && export NODE_OPTIONS=--openssl-legacy-provider
npm install --no-audit --no-fund
```

---

### Step 7: Warm the Maven cache (verifies backend deps resolve)

```bash
ROOT="$(git rev-parse --show-toplevel)"
cd "$ROOT/backend"
mvn -q -DskipTests dependency:go-offline
mvn -q -DskipTests compile && echo "backend compiles"
```

If this fails to download dependencies, it's almost always a network/proxy issue — surface the error.

---

### Step 8: Readiness report

Print a checklist:
- OS + package manager
- Container engine (podman/docker) + `engine OK`, plus podman-machine state on macOS
- Backend: `java -version`, `mvn -version` (flag if JDK is outside 8–17)
- Frontend: `node -v` (note if ≥17 → OpenSSL flag auto-applied), `npm -v`, `tmux -V`
- Postgres: image pulled, `tms-postgres` running, `pg_isready` OK, on `localhost:4002`
- `frontend/node_modules` present; Maven cache warmed / backend compiled
- Any prerequisite the user must still install (with the exact command)

Then finish with:

> ✅ Setup complete. Next: run **`/start-dev`** to launch the app (frontend :4000, backend :4001).
> DB: `jdbc:postgresql://localhost:4002/tms_dev` (tms / tms).
