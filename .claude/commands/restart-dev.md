---
description: Restart TMS dev services (frontend, backend) without restarting the container or tmux
allowed-tools: Bash, mcp__tmux__list-sessions, mcp__tmux__find-session, mcp__tmux__list-windows, mcp__tmux__list-panes, mcp__tmux__create-session, mcp__tmux__split-pane, mcp__tmux__execute-command, mcp__tmux__capture-pane, mcp__tmux__kill-pane
---

## Task: Restart TMS Dev Services

Restart frontend and backend. Ensure the postgres container and tmux session exist but don't restart them.

### Step 1: Ensure Container Is Running

Detect the engine (podman preferred, docker fallback):

```bash
ENGINE=$(command -v podman >/dev/null 2>&1 && echo podman || echo docker)
$ENGINE ps --format "{{.Names}}" | grep -E "^tms-postgres$"
```

- If running → leave it alone.
- If it exists but is stopped → `$ENGINE start tms-postgres`
- If it does not exist → create it (same as `/start-dev` Step 1):

```bash
$ENGINE run -d --name tms-postgres \
  -e POSTGRES_DB=tms_dev \
  -e POSTGRES_USER=tms \
  -e POSTGRES_PASSWORD=tms \
  -p 4002:5432 \
  postgres:15-alpine
```

**Wait for PostgreSQL to be ready:**

```bash
for i in {1..30}; do
  $ENGINE exec tms-postgres pg_isready -U tms && break
  sleep 1
done
```

**Do NOT restart the container if it is already running.**

### Step 2: Ensure tmux Session Exists

1. Use `find-session` with name `tms`
2. If NOT found → create session `tms` and split into 2 horizontal panes (frontend, backend)
3. If found → do NOT recreate; verify there are 2 panes (split a 2nd if missing)

### Step 3: Restart Services in tmux Panes

For each pane, send Ctrl+C, wait, then send the start command:

```bash
# Frontend (pane 0) — repo-relative path; OpenSSL legacy flag only on Node >= 17
tmux send-keys -t tms:0.0 C-c
sleep 1
tmux send-keys -t tms:0.0 'cd "$(git rev-parse --show-toplevel)/frontend" && [ "$(node -p "process.versions.node.split(\".\")[0]")" -ge 17 ] && export NODE_OPTIONS=--openssl-legacy-provider; npm start' Enter

# Backend (pane 1)
tmux send-keys -t tms:0.1 C-c
sleep 1
tmux send-keys -t tms:0.1 'cd "$(git rev-parse --show-toplevel)/backend" && mvn spring-boot:run' Enter
```

### Step 4: Verify & Report

Wait a few seconds, then use `capture-pane` on each pane to verify services started.

Tell user:
- Container status (already running or started)
- tmux status (already there or created)
- Services restarted: frontend (:4000), backend (:4001)
- How to view: `tmux attach -t tms`
