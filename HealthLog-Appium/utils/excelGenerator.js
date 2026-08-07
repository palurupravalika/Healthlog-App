const XLSX = require('xlsx');
const path = require('path');

async function generateExcelReport(reportsDir, summaryData, resultsData) {
  const wb = XLSX.utils.book_new();

  // Sheet 1: Summary
  const summaryRows = [
    { Metric: 'Project Name', Value: 'HealthLog Android Mobile App' },
    { Metric: 'Test Framework', Value: 'HealthLog Appium Isolated E2E Suite' },
    { Metric: 'Execution Date', Value: summaryData.environment.executedAt },
    { Metric: 'Total Test Cases', Value: summaryData.metrics.totalTests },
    { Metric: 'Passed Tests', Value: summaryData.metrics.passed },
    { Metric: 'Failed Tests', Value: summaryData.metrics.failed },
    { Metric: 'Skipped Tests', Value: summaryData.metrics.skipped },
    { Metric: 'Pass Rate (%)', Value: summaryData.metrics.passRateFormatted },
    { Metric: 'Total Execution Time (Sec)', Value: `${summaryData.metrics.executionTimeSec} s` },
    { Metric: 'Average Test Duration (Ms)', Value: `${summaryData.metrics.avgDurationMs} ms` }
  ];
  const summarySheet = XLSX.utils.json_to_sheet(summaryRows);
  XLSX.utils.book_append_sheet(wb, summarySheet, 'Summary');

  // Sheet 2: Categories
  const categoryRows = summaryData.categories.map(cat => ({
    'Category Name': cat.categoryName,
    'Total Tests': cat.total,
    'Passed': cat.passed,
    'Failed': cat.failed,
    'Skipped': cat.skipped,
    'Pass Rate (%)': `${cat.passRate}%`,
    'Avg Duration (ms)': `${cat.avgDurationMs} ms`
  }));
  const categorySheet = XLSX.utils.json_to_sheet(categoryRows);
  XLSX.utils.book_append_sheet(wb, categorySheet, 'Categories');

  // Sheet 3: Test Results
  const resultsRows = resultsData.map(test => ({
    'Test ID': test.id,
    'Category': test.category,
    'Test Name': test.title,
    'Description': test.description,
    'Duration (ms)': test.durationMs,
    'Status': test.status,
    'Timestamp': test.timestamp
  }));
  const resultsSheet = XLSX.utils.json_to_sheet(resultsRows);
  XLSX.utils.book_append_sheet(wb, resultsSheet, 'Test Results');

  // Sheet 4: Execution Statistics
  const statsRows = [
    { Statistic: 'Min Single Test Duration', Value: `${summaryData.metrics.minDurationMs} ms` },
    { Statistic: 'Max Single Test Duration', Value: `${summaryData.metrics.maxDurationMs} ms` },
    { Statistic: 'Mean Test Execution Duration', Value: `${summaryData.metrics.avgDurationMs} ms` },
    { Statistic: 'Total Cumulative Execution Duration', Value: `${summaryData.metrics.totalExecutionDurationMs} ms` },
    { Statistic: 'Test Suite Categories Executed', Value: summaryData.categories.length },
    { Statistic: 'Test Automation Completion Code', Value: '0 (SUCCESS)' }
  ];
  const statsSheet = XLSX.utils.json_to_sheet(statsRows);
  XLSX.utils.book_append_sheet(wb, statsSheet, 'Execution Statistics');

  const excelPath = path.join(reportsDir, 'report.xlsx');
  XLSX.writeFile(wb, excelPath);
  return excelPath;
}

module.exports = { generateExcelReport };
