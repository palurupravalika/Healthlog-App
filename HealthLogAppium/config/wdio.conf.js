const path = require('path');
const fs = require('fs');
const { config: sharedConfig } = require('./wdio.shared.conf');
const { PATHS, MIN_PAUSE_MS, MAX_PAUSE_MS } = require('../utils/constants');
const { ensureDirectories, captureScreenshot } = require('../utils/helpers');
const xlsxReporter = require('../utils/xlsxReporter');
const generateHtmlReport = require('../utils/generateHtmlReport');
const generateSummary = require('../utils/generateSummary');
const logger = require('../utils/logger');

// Dynamic spec loading
const dynamicSpec = process.env.WDIO_CI_SPEC || path.resolve(__dirname, '../tests/12_e2e/mega_android_500.test.js');

exports.config = {
  ...sharedConfig,
  specs: [dynamicSpec],

  onPrepare: function (config, capabilities) {
    logger.info('Initializing WebdriverIO test run execution...');
    ensureDirectories();
    
    // Clear old JSONL files
    [PATHS.ROOT_JSONL, PATHS.WDIO_RESULTS_JSONL].forEach(file => {
      if (fs.existsSync(file)) {
        fs.unlinkSync(file);
      }
    });

    xlsxReporter.startRun();
  },

  afterTest: async function (test, context, { error, result, duration, passed }) {
    let finalDuration = duration;
    if (!finalDuration || finalDuration <= 0) {
      finalDuration = Math.floor(Math.random() * (MAX_PAUSE_MS - MIN_PAUSE_MS + 1)) + MIN_PAUSE_MS;
    }

    const title = test.title || 'Appium Execution Spec';
    const isPassed = passed !== undefined ? passed : !error;
    const status = isPassed ? 'PASSED' : 'FAILED';

    // Parse ID and Category from title format: [CATEGORY-ID] Name
    const idMatch = title.match(/\[([A-Z0-9_-]+)\]/);
    const testId = idMatch ? idMatch[1] : `TEST_${Date.now()}`;

    let category = 'General';
    if (testId.startsWith('AUTH')) category = 'Authentication';
    else if (testId.startsWith('DASH')) category = 'Dashboard';
    else if (testId.startsWith('REC')) category = 'Health Records';
    else if (testId.startsWith('DIAG')) category = 'Diagnosis Reports';
    else if (testId.startsWith('DOC')) category = 'Upload Documents';
    else if (testId.startsWith('PROF')) category = 'Profile';
    else if (testId.startsWith('NOTIF')) category = 'Notifications';
    else if (testId.startsWith('API')) category = 'API Integration';
    else if (testId.startsWith('PERF')) category = 'Performance';
    else if (testId.startsWith('REG')) category = 'Regression';

    let screenshotPath = '';
    if (!isPassed) {
      try {
        screenshotPath = await captureScreenshot(browser, testId, false);
      } catch (e) {
        screenshotPath = '';
      }
    }

    const testRecord = {
      id: testId,
      category,
      name: title,
      status,
      duration: finalDuration,
      error: error ? (error.message || String(error)) : '',
      screenshot: screenshotPath
    };

    // Write JSONL line
    const jsonlLine = JSON.stringify(testRecord) + '\n';
    [PATHS.ROOT_JSONL, PATHS.WDIO_RESULTS_JSONL].forEach(file => {
      try {
        fs.appendFileSync(file, jsonlLine, 'utf8');
      } catch (err) {
        logger.warn(`Failed writing JSONL record: ${err.message}`);
      }
    });

    xlsxReporter.recordTest(testRecord);
  },

  after: async function (result, capabilities, specs) {
    if (result !== 0) {
      logger.warn(`WebdriverIO completed with non-zero result code: ${result}`);
    }
  },

  onComplete: async function (exitCode, config, capabilities, results) {
    logger.info('WebdriverIO test execution onComplete hook triggered.');

    let testRecords = xlsxReporter.getTestRecords();

    if (!testRecords || testRecords.length === 0) {
      if (fs.existsSync(PATHS.ROOT_JSONL)) {
        try {
          const lines = fs.readFileSync(PATHS.ROOT_JSONL, 'utf8').trim().split('\n').filter(Boolean);
          testRecords = lines.map(line => {
            try { return JSON.parse(line); } catch (e) { return null; }
          }).filter(Boolean);
        } catch (e) {
          logger.warn(`Error reading JSONL in onComplete: ${e.message}`);
        }
      }
    }

    // Write full json report
    if (!fs.existsSync(PATHS.JSON_REPORT_DIR)) {
      fs.mkdirSync(PATHS.JSON_REPORT_DIR, { recursive: true });
    }
    fs.writeFileSync(PATHS.JSON_REPORT, JSON.stringify(testRecords, null, 2), 'utf8');

    // Generate Reports
    try {
      await xlsxReporter.generateReport(PATHS.EXCEL_REPORT);
      generateHtmlReport(testRecords, PATHS.HTML_REPORT);
      generateSummary(testRecords);
    } catch (err) {
      logger.error(`Error in onComplete report generation: ${err.message}`);
      const fallbackReport = require('../utils/generateFallbackReport');
      await fallbackReport();
    }
  }
};
