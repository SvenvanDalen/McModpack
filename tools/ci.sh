#!/usr/bin/env bash
# Draait build, unit-tests en GameTests in één keer. Stopt bij de eerste fout.
set -e
cd "$(dirname "$0")/.."
export JAVA_HOME="${DR_JAVA_HOME:-/c/Program Files/Java/jdk-25}"
export PATH="$JAVA_HOME/bin:$PATH"
export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS:--Djavax.net.ssl.trustStoreType=Windows-ROOT}"
./gradlew build runGameTest --no-daemon
echo "CI OK: build, tests en GameTests geslaagd"
