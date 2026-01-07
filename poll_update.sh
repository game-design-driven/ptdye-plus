#!/bin/bash

MOD_SLUG=$1
TARGET_DIR=$2
MAX_RETRIES=20
RETRY_INTERVAL=60

echo "Checking for updates for slug '$MOD_SLUG' in '$TARGET_DIR'..."

if [ -n "$TARGET_DIR" ]; then
  cd "$TARGET_DIR" || exit 1
end

for ((i=1; i<=MAX_RETRIES; i++)); do
  if packwiz update "$MOD_SLUG"; then
    echo "Success: Update found and applied for $MOD_ID."
    exit 0
  fi

  echo "Attempt $i/$MAX_RETRIES: No update found yet. Sleeping ${RETRY_INTERVAL}s..."
  sleep $RETRY_INTERVAL
done

echo "Error: Timed out waiting for $MOD_SLUG update after $((MAX_RETRIES * RETRY_INTERVAL / 60)) minutes."
exit 1
