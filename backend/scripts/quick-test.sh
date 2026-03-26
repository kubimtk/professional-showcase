#!/usr/bin/env bash

set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
APP_JAR="${BACKEND_DIR}/target/appointment-backend-showcase-0.0.1-SNAPSHOT.jar"
backend_started_by_script="false"
backend_pid=""
backend_log=""
appointment_id=""

cleanup() {
  if [[ "${backend_started_by_script}" == "true" && -n "${backend_pid}" ]]; then
    echo
    echo "Stopping backend process ${backend_pid}"
    kill "${backend_pid}" >/dev/null 2>&1 || true
    wait "${backend_pid}" 2>/dev/null || true
  fi

  if [[ -n "${backend_log}" && -f "${backend_log}" ]]; then
    rm -f "${backend_log}"
  fi
}

trap cleanup EXIT

api_is_reachable() {
  curl -fsS "${BASE_URL}/api/appointments" >/dev/null 2>&1
}

start_backend_if_needed() {
  if api_is_reachable; then
    echo "Backend already reachable at ${BASE_URL}"
    return 0
  fi

  if [[ "${BASE_URL}" != "http://localhost:8080" && "${BASE_URL}" != "http://127.0.0.1:8080" ]]; then
    echo "Backend not reachable at ${BASE_URL} and auto-start is only supported for local default URLs." >&2
    exit 1
  fi

  backend_started_by_script="true"
  backend_log="$(mktemp -t appointment-backend-smoketest.XXXXXX.log)"

  echo "Building backend jar"
  (
    cd "${BACKEND_DIR}"
    mvn -q -DskipTests package
  ) >/dev/null

  echo "Starting backend locally from ${APP_JAR}"
  java -jar "${APP_JAR}" >"${backend_log}" 2>&1 &
  backend_pid=$!

  for _ in {1..60}; do
    if api_is_reachable; then
      echo "Backend is ready at ${BASE_URL}"
      return 0
    fi

    if ! kill -0 "${backend_pid}" >/dev/null 2>&1; then
      echo "Backend failed to start. Log output:" >&2
      python3 - "${backend_log}" <<'PY' >&2
import pathlib
import sys

path = pathlib.Path(sys.argv[1])
if path.exists():
    text = path.read_text()
    print(text[-4000:] if len(text) > 4000 else text)
PY
      exit 1
    fi

    sleep 1
  done

  echo "Backend did not become ready within 60 seconds." >&2
  exit 1
}

start_backend_if_needed

read -r START_AT END_AT <<EOF
$(python3 <<'PY'
from datetime import datetime, timedelta

start = datetime.now() + timedelta(days=1)
while start.weekday() >= 5:
    start += timedelta(days=1)
start = start.replace(hour=10, minute=0, second=0, microsecond=0)
end = start + timedelta(minutes=30)

print(start.strftime("%Y-%m-%dT%H:%M:%S"), end.strftime("%Y-%m-%dT%H:%M:%S"))
PY
)
EOF

echo "Using base URL: ${BASE_URL}"
echo "Creating appointment for ${START_AT} -> ${END_AT}"

create_response="$(
curl -sS \
  -X POST "${BASE_URL}/api/appointments" \
  -H "Content-Type: application/json" \
  -d "{
    \"customerName\": \"Sample Customer\",
    \"customerEmail\": \"sample.customer.$(date +%s)@example.org\",
    \"subject\": \"Quick curl smoke test\",
    \"startsAt\": \"${START_AT}\",
    \"endsAt\": \"${END_AT}\"
  }"
)"

echo
echo "Create response:"
printf '%s' "${create_response}" | python3 -m json.tool

appointment_id="$(
printf '%s' "${create_response}" | python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])'
)"

echo
echo "Fetching appointment ${appointment_id}:"
curl -sS "${BASE_URL}/api/appointments/${appointment_id}" | python3 -m json.tool

echo
echo "Cancelling appointment ${appointment_id}:"
curl -sS -X DELETE "${BASE_URL}/api/appointments/${appointment_id}" | python3 -m json.tool

echo
echo "Listing appointments:"
curl -sS "${BASE_URL}/api/appointments" | python3 -m json.tool
