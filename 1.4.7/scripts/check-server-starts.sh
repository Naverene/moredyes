#!/usr/bin/env bash
# Starts a dedicated server with the mod (runServer), waits until it has loaded every mod and
# generated the spawn area, then stops it. Fails if the server crashes, a mod throws while loading,
# or it does not finish starting in time.
# Used by the GitHub Actions build.
set -u

LOG=${LOG:-build/server-check.log}
TIMEOUT_SECONDS=${TIMEOUT_SECONDS:-900}
mkdir -p "$(dirname "$LOG")"
: > "$LOG"

# Lines that mean the server did not start cleanly.
FAILURE_PATTERN='---- Minecraft Crash Report ----|LoaderException|Encountered an unexpected exception|Exception in server tick loop|Caught exception from|SEVERE\] \[(Minecraft|STDERR)'

# Voldeloom runs the server in run/server. A fresh world each time, so world generation (dye trees,
# flowers and the newer stones) is exercised too.
mkdir -p run/server
[ -f run/server/server.properties ] || echo "online-mode=false" > run/server/server.properties
rm -rf run/server/world

# runServer does not read commands from stdin under Gradle, so the server is stopped by ending
# its process once it has started. nogui keeps the 1.4.7 server from opening its window.
./gradlew --no-daemon runServer --args="nogui" > "$LOG" 2>&1 &
gradle_pid=$!
waited=0
until grep -qE 'Done \([0-9.,]+s\)!|BUILD (FAILED|SUCCESSFUL)' "$LOG" || [ "$waited" -ge "$TIMEOUT_SECONDS" ]; do
    sleep 2
    waited=$((waited + 2))
done
pkill -f '[n]et\.minecraft\.server\.MinecraftServer' || true
wait "$gradle_pid"

if grep -qE -e "$FAILURE_PATTERN" "$LOG"; then
    echo "The server reported an error while starting:"
    grep -nE -A 20 -e "$FAILURE_PATTERN" "$LOG" | head -80
    exit 1
fi
if ! grep -qE 'Done \([0-9.,]+s\)!' "$LOG"; then
    echo "The server did not finish starting within $TIMEOUT_SECONDS seconds. Last lines of the log:"
    tail -60 "$LOG"
    exit 1
fi
echo "The server started with More Dyes loaded:"
grep -E 'Done \([0-9.,]+s\)!' "$LOG"
