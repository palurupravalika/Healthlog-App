const fs = require('fs');
const path = require('path');

function generateJsonReports(reportsDir, summaryData, resultsData) {
  if (!fs.existsSync(reportsDir)) {
    fs.mkdirSync(reportsDir, { recursive: true });
  }

  // 1. summary.json
  const summaryPath = path.join(reportsDir, 'summary.json');
  fs.writeFileSync(summaryPath, JSON.stringify(summaryData, null, 2));

  // 2. results.json
  const resultsPath = path.join(reportsDir, 'results.json');
  fs.writeFileSync(resultsPath, JSON.stringify(resultsData, null, 2));

  // 3. metrics.json
  const metricsPath = path.join(reportsDir, 'metrics.json');
  const metricsData = {
    summary: summaryData.metrics,
    environment: summaryData.environment,
    categoryBreakdown: summaryData.categories
  };
  fs.writeFileSync(metricsPath, JSON.stringify(metricsData, null, 2));

  return {
    summaryPath,
    resultsPath,
    metricsPath
  };
}

module.exports = { generateJsonReports };
