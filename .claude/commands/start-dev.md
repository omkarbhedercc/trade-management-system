---
description: Start or connect to TMS dev environment (postgres container + frontend + backend in tmux)
allowed-tools: Bash, mcp__tmux__list-sessions, mcp__tmux__find-session, mcp__tmux__list-windows, mcp__tmux__list-panes, mcp__tmux__create-session, mcp__tmux__split-pane, mcp__tmux__execute-command, mcp__tmux__capture-pane
---

## Task: Start TMS Development Environment

Start the full TMS dev environment: PostgreSQL container + tmux session with frontend and backend.

Ports: frontend **4000**, backend **4001**, database **4002 → 5432**.

> First run `/setup-dev` on a fresh machine — it installs prerequisites, the container engine,
> the Postgres image, frontend deps, and the Maven cache. This command assumes those are in place.

### Step 1: Manage Postgres Container

Detect the container engine (podman preferred, docker fallback) and reuse it:

```bash
ENGINE=$(command -v podman >/dev/null 2>&1 && echo podman || echo docker)
# macOS podman needs its VM running:
[ "$ENGINE" = "podman" ] && [ "$(uname -s)" = "Darwin" ] && podman machine start 2>/dev/null || true
echo "engine: $ENGINE"
```

Container name `tms-postgres` persists data across restarts.

**Check if it exists (stopped or running):**

```bash
$ENGINE ps -a --format "{{.Names}}" | grep -E "^tms-postgres$"
```

**If it does NOT exist, create it:**

```bash
$ENGINE run -d --name tms-postgres \
  -e POSTGRES_DB=tms_dev \
  -e POSTGRES_USER=tms \
  -e POSTGRES_PASSWORD=tms \
  -p 4002:5432 \
  postgres:15-alpine
```

**If it exists but is stopped, start it:**

```bash
$ENGINE start tms-postgres
```

**Wait for PostgreSQL to be ready:**

```bash
for i in {1..30}; do
  $ENGINE exec tms-postgres pg_isready -U tms && break
  sleep 1
done
```

### Step 2: Setup tmux Session

1. **Check if tmux session exists** — use `find-session` with name `tms`. If not found, create with `create-session`.
2. **Check panes** — need 2 horizontal panes: frontend, backend. If only 1 pane, split horizontally once.
3. **Check if services running** (use `capture-pane`):
   - Frontend (pane 0): React dev server on port 4000
   - Backend (pane 1): Spring Boot on port 4001

4. **Start services if not running** (paths resolved from the repo root; the frontend adds the
   OpenSSL legacy flag only on Node ≥ 17):
   - Pane 0 (frontend):
     `cd "$(git rev-parse --show-toplevel)/frontend" && [ "$(node -p 'process.versions.node.split(".")[0]')" -ge 17 ] && export NODE_OPTIONS=--openssl-legacy-provider; npm start`
   - Pane 1 (backend):
     `cd "$(git rev-parse --show-toplevel)/backend" && mvn spring-boot:run`

### Step 3: Report Status

Tell user:
- Container status (`tms-postgres` created / started / already running)
- Service status (frontend :4000, backend :4001)
- DB connection: `jdbc:postgresql://localhost:4002/tms_dev` (tms / tms)
- How to view: `tmux attach -t tms`
