# HealthLog Mobile Appium E2E Automation Testing Framework

Comprehensive, modular, and parameterized Mobile E2E automation testing framework for the HealthLog Android Application using **Appium**, **WebdriverIO (v8)**, and **ExcelJS**.

---

## 📁 Directory Structure

```text
healthLog App/HealthLogAppium/
├── config/
│   ├── android.capabilities.js     # UIAutomator2 & Android App capabilities
│   ├── appium.config.js            # Appium server host, port & timeout config
│   ├── wdio.shared.conf.js         # Base shared WebdriverIO configuration
│   └── wdio.conf.js                # Main WebdriverIO config with lifecycle hooks
├── scripts/
│   └── ci_run_tests.sh             # Bash script for ADB logs, Appium & test execution
├── tests/
│   ├── 01_authentication/
│   │   └── authentication.test.js  # 50 Parameterized Auth Tests
│   ├── 02_dashboard/
│   │   └── dashboard.test.js       # 50 Parameterized Dashboard Tests
│   ├── 03_health_records/
│   │   └── health_records.test.js  # 50 Parameterized Records Tests
│   ├── 04_reports/
│   │   └── diagnosis_reports.test.js # 50 Parameterized Diagnosis Tests
│   ├── 05_upload_documents/
│   │   └── upload_documents.test.js # 50 Parameterized Document Upload Tests
│   ├── 06_profile/
│   │   └── profile.test.js         # 50 Parameterized Profile Tests
│   ├── 07_notifications/
│   │   └── notifications.test.js   # 50 Parameterized Notification Tests
│   ├── 08_api/
│   │   └── api.test.js             # 50 Parameterized API Integration Tests
│   ├── 09_performance/
│   │   └── performance.test.js     # 50 Parameterized Performance Tests
│   ├── 10_regression/
│   │   └── regression.test.js      # 50 Parameterized Regression Tests
│   └── 12_e2e/
│       └── mega_android_500.test.js # Mega Suite (All 500 Parameterized Tests)
├── utils/
│   ├── constants.js                # Shared path & package constants
│   ├── logger.js                   # Console & log file writer
│   ├── helpers.js                  # Appium session verification & screenshots
│   ├── xlsxReporter.js             # Custom ExcelJS reporter (3 sheets)
│   ├── generateHtmlReport.js       # Premium Dark Mode HTML Dashboard generator
│   ├── generateSummary.js          # GITHUB_STEP_SUMMARY formatter
│   └── generateFallbackReport.js   # Crash recovery report generator
├── test-results/
│   ├── html/
│   │   └── execution-report.html   # Premium Dark HTML Dashboard
│   ├── excel/
│   │   └── HealthLog_Android_TestReport.xlsx # Excel Workbook Report
│   ├── json/
│   │   ├── results.json
│   │   └── .wdio-results.jsonl
│   ├── screenshots/
│   │   ├── passed/
│   │   └── failed/
│   ├── videos/
│   └── logs/
│       ├── appium.log
│       ├── wdio.log
│       ├── adb.log
│       ├── adb-logcat.log
│       └── adb-activity.log
├── package.json
├── wdio.conf.js
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Node.js >= 18
- Java JDK 17
- Android SDK & ADB
- Appium Server v2 (`npm install -g appium`)
- UIAutomator2 Driver (`appium driver install uiautomator2`)

### Installation
```bash
cd "healthLog App/HealthLogAppium"
npm install
```

---

## 🧪 Running Tests

### Execute 500 Parameterized Tests
```bash
npm run test:android
```

### Run Custom Spec
```bash
WDIO_CI_SPEC=./tests/01_authentication/authentication.test.js npm test
```

### Full CI Script (Local or GitHub Actions)
```bash
bash scripts/ci_run_tests.sh
```

---

## 📊 Reports & Artifacts

1. **Excel Report:** `test-results/excel/HealthLog_Android_TestReport.xlsx`
   - Sheet 1: Summary Statistics & Metrics
   - Sheet 2: Category Breakdown (10 categories)
   - Sheet 3: Detailed 500 Test Cases Table with durations (never 0 ms)
2. **HTML Dashboard:** `test-results/html/execution-report.html`
   - Dark theme dashboard with interactive Chart.js charts and progress bars.
3. **GitHub Step Summary:** Automatically updated via `$GITHUB_STEP_SUMMARY`.
