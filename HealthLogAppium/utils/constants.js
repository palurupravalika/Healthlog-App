const path = require('path');

const ROOT_DIR = path.resolve(__dirname, '..');
const TEST_RESULTS_DIR = path.join(ROOT_DIR, 'test-results');

module.exports = {
  APP_PACKAGE: 'com.example.healthlog',
  APP_ACTIVITY: 'com.example.healthlog.MainActivity',
  APPIUM_HOST: process.env.APPIUM_HOST || '127.0.0.1',
  APPIUM_PORT: parseInt(process.env.APPIUM_PORT || '4723', 10),
  
  PATHS: {
    ROOT: ROOT_DIR,
    TEST_RESULTS: TEST_RESULTS_DIR,
    HTML_REPORT_DIR: path.join(TEST_RESULTS_DIR, 'html'),
    HTML_REPORT: path.join(TEST_RESULTS_DIR, 'html', 'execution-report.html'),
    EXCEL_REPORT_DIR: path.join(TEST_RESULTS_DIR, 'excel'),
    EXCEL_REPORT: path.join(TEST_RESULTS_DIR, 'excel', 'HealthLog_Android_TestReport.xlsx'),
    JSON_REPORT_DIR: path.join(TEST_RESULTS_DIR, 'json'),
    JSON_REPORT: path.join(TEST_RESULTS_DIR, 'json', 'results.json'),
    WDIO_RESULTS_JSONL: path.join(TEST_RESULTS_DIR, 'json', '.wdio-results.jsonl'),
    ROOT_JSONL: path.join(ROOT_DIR, '.wdio-results.jsonl'),
    LOGS_DIR: path.join(TEST_RESULTS_DIR, 'logs'),
    APPIUM_LOG: path.join(TEST_RESULTS_DIR, 'logs', 'appium.log'),
    WDIO_LOG: path.join(TEST_RESULTS_DIR, 'logs', 'wdio.log'),
    ADB_LOG: path.join(TEST_RESULTS_DIR, 'logs', 'adb.log'),
    ADB_LOGCAT: path.join(TEST_RESULTS_DIR, 'logs', 'adb-logcat.log'),
    ADB_ACTIVITY: path.join(TEST_RESULTS_DIR, 'logs', 'adb-activity.log'),
    SCREENSHOTS_DIR: path.join(TEST_RESULTS_DIR, 'screenshots'),
    PASSED_SCREENSHOTS: path.join(TEST_RESULTS_DIR, 'screenshots', 'passed'),
    FAILED_SCREENSHOTS: path.join(TEST_RESULTS_DIR, 'screenshots', 'failed'),
    VIDEOS_DIR: path.join(TEST_RESULTS_DIR, 'videos'),
    HISTORY_DIR: path.join(TEST_RESULTS_DIR, 'history')
  },

  CATEGORIES: [
    'Authentication',
    'Dashboard',
    'Health Records',
    'Diagnosis Reports',
    'Upload Documents',
    'Profile',
    'Notifications',
    'API Integration',
    'Performance',
    'Regression'
  ],

  MIN_PAUSE_MS: 5,
  MAX_PAUSE_MS: 20
};
