---
description: Stop TMS dev environment (kill tmux session tms + stop postgres container)
allowed-tools: Bash, mcp__tmux__find-session, mcp__tmux__kill-session
---

## Task: Stop TMS Development Environment

Stop all services: kill the tmux session and stop the postgres container (don't delete it — preserve data).

### Step 1: Kill tmux Session

1. Use `find-session` with name `tms`
2. If found, use `kill-session` to stop frontend and backend
3. If not found, tell user no tmux session is running

### Step 2: Stop Container

Stop (don't remove — preserves data for next `/start-dev`):

```bash
podman stop tms-postgres 2>/dev/null || true
```

### Step 3: Report Status

Confirm:
- tmux session `tms` killed (frontend, backend stopped)
- Container `tms-postgres` stopped
- Data preserved for next `/start-dev` run
