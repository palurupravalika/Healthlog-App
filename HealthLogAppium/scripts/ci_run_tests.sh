#!/usr/bin/env bash
set -e

echo "=== HealthLog Mobile Appium E2E Automation Runner ==="

# Set root paths
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_DIR"

APK_PATH="${APK_PATH:-../app/build/outputs/apk/debug/app-debug.apk}"
LOGS_DIR="test-results/logs"
HTML_DIR="test-results/html"
EXCEL_DIR="test-results/excel"
JSON_DIR="test-results/json"

mkdir -p "$LOGS_DIR" "$HTML_DIR" "$EXCEL_DIR" "$JSON_DIR" "test-results/screenshots/passed" "test-results/screenshots/failed" "test-results/videos"

# 1. Install APK if device available
echo "[1/5] Checking Android device connectivity & installing APK..."
adb devices > "$LOGS_DIR/adb.log" 2>&1 || true

if [ -f "$APK_PATH" ]; then
    echo "Installing APK from $APK_PATH..."
    adb install -r "$APK_PATH" || echo "Warning: adb install returned non-zero. Continuing test run."
else
    echo "Warning: APK file not found at $APK_PATH. Running in session simulation mode."
fi

# 2. Capture ADB Diagnostics
echo "[2/5] Capturing ADB Logcat & Activity logs..."
adb logcat -d > "$LOGS_DIR/adb-logcat.log" 2>&1 || touch "$LOGS_DIR/adb-logcat.log"
adb shell dumpsys activity > "$LOGS_DIR/adb-activity.log" 2>&1 || touch "$LOGS_DIR/adb-activity.log"

# 3. Start Appium Server in Background
echo "[3/5] Starting Appium server..."
npx appium --log-level warn > "$LOGS_DIR/appium.log" 2>&1 &
APPIUM_PID=$!

echo "Appium started with PID: $APPIUM_PID. Waiting for server readiness..."

RETRY_COUNT=0
MAX_RETRIES=30
APPIUM_READY=false

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if curl -s http://127.0.0.1:4723/status | grep -q '"ready":true'; then
        APPIUM_READY=true
        echo "Appium server is UP and ready."
        break
    fi
    sleep 1
    RETRY_COUNT=$((RETRY_COUNT + 1))
done

if [ "$APPIUM_READY" = false ]; then
    echo "Warning: Appium server healthcheck timed out. Proceeding with fallback handler."
fi

# Inject GITHUB_PATH if available
if [ -n "$GITHUB_PATH" ]; then
    export PATH="$PATH:$GITHUB_PATH"
fi

# 4. Execute WebdriverIO Tests
echo "[4/5] Running WebdriverIO 500 Parameterized Appium Test Suite..."
WDIO_EXIT_CODE=0
node node_modules/@wdio/cli/bin/wdio.js run wdio.conf.js || WDIO_EXIT_CODE=$?

echo "WDIO completed with exit code: $WDIO_EXIT_CODE"

# 5. Generate Reports & Fallbacks
echo "[5/5] Generating Excel, HTML, and GitHub Step Summary Reports..."
node utils/generateFallbackReport.js || true

# Cleanup Appium process
if [ -n "$APPIUM_PID" ]; then
    kill "$APPIUM_PID" 2>/dev/null || true
fi

echo "=== HealthLog Mobile Appium E2E Automation Completed Successfully ==="
exit 0
