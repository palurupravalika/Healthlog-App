const fs = require('fs');
const path = require('path');

class Logger {
  constructor(logsDir) {
    this.logsDir = logsDir;
    if (!fs.existsSync(logsDir)) {
      fs.mkdirSync(logsDir, { recursive: true });
    }
    this.runnerLogPath = path.join(logsDir, 'runner.log');
    this.executionLogPath = path.join(logsDir, 'execution.log');
    this.summaryLogPath = path.join(logsDir, 'summary.log');

    // Reset log files
    fs.writeFileSync(this.runnerLogPath, `[${new Date().toISOString()}] === HealthLog Appium Test Runner Initialized ===\n`);
    fs.writeFileSync(this.executionLogPath, `[${new Date().toISOString()}] === Test Execution Details Log ===\n`);
    fs.writeFileSync(this.summaryLogPath, `[${new Date().toISOString()}] === HealthLog E2E Summary Log ===\n`);
  }

  logRunner(message) {
    const timestamp = new Date().toISOString();
    const entry = `[${timestamp}] [INFO] ${message}\n`;
    fs.appendFileSync(this.runnerLogPath, entry);
    console.log(`[HealthLog-E2E] ${message}`);
  }

  logTest(testResult) {
    const timestamp = testResult.timestamp || new Date().toISOString();
    const entry = `[${timestamp}] [${testResult.status}] [${testResult.category}] ${testResult.id} - ${testResult.title} (Duration: ${testResult.durationMs}ms)\n  Description: ${testResult.description}\n`;
    fs.appendFileSync(this.executionLogPath, entry);
  }

  logSummary(summaryText) {
    const timestamp = new Date().toISOString();
    const entry = `\n[${timestamp}] === FINAL TEST EXECUTION SUMMARY ===\n${summaryText}\n`;
    fs.appendFileSync(this.summaryLogPath, entry);
  }
}

module.exports = Logger;
