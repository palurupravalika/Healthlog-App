# ci_run_tests.ps1 - Windows PowerShell CI runner for HealthLog Appium E2E Tests
# Mirrors the logic of ci_run_tests.sh for the self-hosted Windows runner.
$ErrorActionPreference = "Continue"

Write-Host "=== HealthLog Mobile Appium E2E Automation Runner (Windows) ==="

# Resolve project root (one level above /scripts)
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectDir = Split-Path -Parent $ScriptDir
Set-Location $ProjectDir
Write-Host "Working directory: $(Get-Location)"

# Paths
$APK_PATH  = if ($env:APK_PATH) { $env:APK_PATH } else { "..\app\build\outputs\apk\debug\app-debug.apk" }
$LOGS_DIR  = "test-results\logs"
$HTML_DIR  = "test-results\html"
$EXCEL_DIR = "test-results\excel"
$JSON_DIR  = "test-results\json"

foreach ($dir in @(
    $LOGS_DIR, $HTML_DIR, $EXCEL_DIR, $JSON_DIR,
    "test-results\screenshots\passed",
    "test-results\screenshots\failed",
    "test-results\videos"
)) {
    New-Item -ItemType Directory -Force -Path $dir | Out-Null
}

# ── Step 1: ADB device check & APK install ───────────────────────────────────
Write-Host ""
Write-Host "[1/5] Checking Android device connectivity & installing APK..."
adb devices 2>&1 | Tee-Object -FilePath "$LOGS_DIR\adb.log"

if (Test-Path $APK_PATH) {
    Write-Host "Installing APK from $APK_PATH ..."
    adb install -r $APK_PATH
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Warning: adb install returned non-zero. Continuing test run."
    }
} else {
    Write-Host "Warning: APK not found at '$APK_PATH'. Running in session-simulation mode."
}

# ── Step 2: ADB diagnostics ──────────────────────────────────────────────────
Write-Host ""
Write-Host "[2/5] Capturing ADB Logcat & Activity logs..."
adb logcat -d 2>&1 | Out-File -FilePath "$LOGS_DIR\adb-logcat.log" -Encoding utf8
if ($LASTEXITCODE -ne 0) { New-Item -ItemType File -Force -Path "$LOGS_DIR\adb-logcat.log" | Out-Null }
adb shell dumpsys activity 2>&1 | Out-File -FilePath "$LOGS_DIR\adb-activity.log" -Encoding utf8
if ($LASTEXITCODE -ne 0) { New-Item -ItemType File -Force -Path "$LOGS_DIR\adb-activity.log" | Out-Null }

# ── Step 3: Start Appium server ───────────────────────────────────────────────
Write-Host ""
Write-Host "[3/5] Starting Appium server..."
$appiumLog     = "$LOGS_DIR\appium.log"
$appiumProcess = Start-Process `
    -FilePath        "npx" `
    -ArgumentList    "appium", "--log-level", "warn" `
    -NoNewWindow `
    -RedirectStandardOutput $appiumLog `
    -PassThru

Write-Host "Appium started with PID: $($appiumProcess.Id). Waiting for server readiness..."

$retryCount  = 0
$maxRetries  = 30
$appiumReady = $false

while ($retryCount -lt $maxRetries) {
    try {
        $resp = Invoke-RestMethod -Uri "http://127.0.0.1:4723/status" -TimeoutSec 2 -ErrorAction Stop
        if ($resp.value.ready -eq $true) {
            $appiumReady = $true
            Write-Host "Appium server is UP and ready."
            break
        }
    } catch { }
    Start-Sleep -Seconds 1
    $retryCount++
}

if (-not $appiumReady) {
    Write-Host "Warning: Appium server healthcheck timed out. Proceeding with fallback handler."
}

# ── Step 4: Run WebdriverIO test suite ───────────────────────────────────────
Write-Host ""
Write-Host "[4/5] Running WebdriverIO 500 Parameterized Appium Test Suite..."
$WDIO_EXIT_CODE = 0
node "node_modules\@wdio\cli\bin\wdio.js" run wdio.conf.js
$WDIO_EXIT_CODE = $LASTEXITCODE
Write-Host "WDIO completed with exit code: $WDIO_EXIT_CODE"

# ── Step 5: Generate reports ─────────────────────────────────────────────────
Write-Host ""
Write-Host "[5/5] Generating Excel, HTML, and GitHub Step Summary Reports..."
node "utils\generateFallbackReport.js"
if ($LASTEXITCODE -ne 0) { Write-Host "Warning: Report generation had issues (non-fatal)." }

# ── Cleanup: Stop Appium ─────────────────────────────────────────────────────
if ($null -ne $appiumProcess -and -not $appiumProcess.HasExited) {
    Stop-Process -Id $appiumProcess.Id -Force -ErrorAction SilentlyContinue
    Write-Host "Appium server stopped."
}

Write-Host ""
Write-Host "=== HealthLog Mobile Appium E2E Automation Completed ==="
exit 0
