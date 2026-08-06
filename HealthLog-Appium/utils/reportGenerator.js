const path = require('path');
const { generateSummaryMetrics } = require('./summaryGenerator');
const { generateJsonReports } = require('./jsonGenerator');
const { generateExcelReport } = require('./excelGenerator');
const { generateHtmlReport } = require('./htmlGenerator');
const { generateScreenshots } = require('./screenshotGenerator');

async function buildAllReports(reportsDir, results, wallTimeSec, logger) {
  logger.logRunner("Calculating summary metrics and category statistics...");
  const summaryData = generateSummaryMetrics(results, wallTimeSec);

  logger.logRunner("Generating JSON reports (summary.json, results.json, metrics.json)...");
  const jsonFiles = generateJsonReports(reportsDir, summaryData, results);

  logger.logRunner("Generating Excel workbook report (report.xlsx)...");
  const excelPath = await generateExcelReport(reportsDir, summaryData, results);

  logger.logRunner("Generating Interactive HTML Execution Dashboard (execution-report.html)...");
  const htmlPath = generateHtmlReport(reportsDir, summaryData, results);

  logger.logRunner("Generating PNG placeholder screenshots (reports/screenshots/)...");
  const screenshotsDir = path.join(reportsDir, 'screenshots');
  const screenshots = generateScreenshots(screenshotsDir, 20);

  logger.logSummary(`
Total Tests       : ${summaryData.metrics.totalTests}
Passed            : ${summaryData.metrics.passed}
Failed            : ${summaryData.metrics.failed}
Skipped           : ${summaryData.metrics.skipped}
Pass Rate         : ${summaryData.metrics.passRateFormatted}
Execution Time    : ${summaryData.metrics.executionTimeSec}s

Reports Output:
- HTML Report     : ${htmlPath}
- Excel Report    : ${excelPath}
- Summary JSON    : ${jsonFiles.summaryPath}
- Results JSON    : ${jsonFiles.resultsPath}
- Metrics JSON    : ${jsonFiles.metricsPath}
- Screenshots     : ${screenshots.length} files generated in ${screenshotsDir}
  `);

  return {
    summaryData,
    jsonFiles,
    excelPath,
    htmlPath,
    screenshotsCount: screenshots.length
  };
}

module.exports = { buildAllReports };
