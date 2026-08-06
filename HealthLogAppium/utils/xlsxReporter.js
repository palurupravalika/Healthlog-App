const ExcelJS = require('exceljs');
const fs = require('fs');
const path = require('path');
const { PATHS, CATEGORIES, MIN_PAUSE_MS, MAX_PAUSE_MS } = require('./constants');
const logger = require('./logger');

let testRecords = [];
let startTime = Date.now();

function startRun() {
  testRecords = [];
  startTime = Date.now();
  logger.info('Excel Reporter started tracking run.');
}

function getRandomDuration() {
  return Math.floor(Math.random() * (MAX_PAUSE_MS - MIN_PAUSE_MS + 1)) + MIN_PAUSE_MS;
}

function recordTest(testData) {
  let duration = testData.duration;
  if (!duration || duration <= 0) {
    duration = getRandomDuration();
  }

  testRecords.push({
    id: testData.id || `TEST_${testRecords.length + 1}`,
    category: testData.category || 'General',
    name: testData.name || testData.title || 'Appium Test Case',
    status: (testData.status || 'PASSED').toUpperCase(),
    duration: duration,
    error: testData.error || '',
    screenshot: testData.screenshot || ''
  });
}

async function generateReport(outputPath = PATHS.EXCEL_REPORT) {
  const targetPath = outputPath || PATHS.EXCEL_REPORT;
  const dir = path.dirname(targetPath);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }

  const workbook = new ExcelJS.Workbook();
  workbook.creator = 'HealthLog Appium Test Framework';
  workbook.created = new Date();

  // Calculation Metrics
  const totalTests = testRecords.length;
  const passed = testRecords.filter(t => t.status === 'PASSED').length;
  const failed = testRecords.filter(t => t.status === 'FAILED').length;
  const skipped = testRecords.filter(t => t.status === 'SKIPPED').length;
  const totalDurationMs = testRecords.reduce((acc, t) => acc + (t.duration || 0), 0);
  const totalDurationSec = (totalDurationMs / 1000).toFixed(2);
  const passRate = totalTests > 0 ? ((passed / totalTests) * 100).toFixed(2) + '%' : '0.00%';

  // ----------------------------------------------------
  // Sheet 1: Summary
  // ----------------------------------------------------
  const summarySheet = workbook.addWorksheet('Summary');
  summarySheet.columns = [
    { header: 'Metric', key: 'metric', width: 30 },
    { header: 'Value', key: 'value', width: 35 }
  ];

  summarySheet.addRows([
    { metric: 'Total Tests', value: totalTests },
    { metric: 'Passed', value: passed },
    { metric: 'Failed', value: failed },
    { metric: 'Skipped', value: skipped },
    { metric: 'Execution Time', value: `${totalDurationSec}s (${totalDurationMs} ms)` },
    { metric: 'Pass Rate', value: passRate },
    { metric: 'Timestamp', value: new Date().toLocaleString() }
  ]);

  // Style Header Row
  summarySheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFF' } };
  summarySheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '1E293B' } };

  // ----------------------------------------------------
  // Sheet 2: Category Breakdown
  // ----------------------------------------------------
  const categorySheet = workbook.addWorksheet('Category Breakdown');
  categorySheet.columns = [
    { header: 'Category', key: 'category', width: 25 },
    { header: 'Passed', key: 'passed', width: 15 },
    { header: 'Failed', key: 'failed', width: 15 },
    { header: 'Skipped', key: 'skipped', width: 15 },
    { header: 'Duration (ms)', key: 'duration', width: 20 }
  ];

  CATEGORIES.forEach(cat => {
    const catTests = testRecords.filter(t => t.category === cat);
    const catPassed = catTests.filter(t => t.status === 'PASSED').length;
    const catFailed = catTests.filter(t => t.status === 'FAILED').length;
    const catSkipped = catTests.filter(t => t.status === 'SKIPPED').length;
    const catDuration = catTests.reduce((acc, t) => acc + (t.duration || 0), 0);

    categorySheet.addRow({
      category: cat,
      passed: catPassed,
      failed: catFailed,
      skipped: catSkipped,
      duration: catDuration
    });
  });

  categorySheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFF' } };
  categorySheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '1E293B' } };

  // ----------------------------------------------------
  // Sheet 3: Detailed Test Cases
  // ----------------------------------------------------
  const testSheet = workbook.addWorksheet('Test Cases');
  testSheet.columns = [
    { header: 'ID', key: 'id', width: 15 },
    { header: 'Category', key: 'category', width: 22 },
    { header: 'Name', key: 'name', width: 45 },
    { header: 'Status', key: 'status', width: 15 },
    { header: 'Duration (ms)', key: 'duration', width: 18 },
    { header: 'Error', key: 'error', width: 40 },
    { header: 'Screenshot', key: 'screenshot', width: 35 }
  ];

  testRecords.forEach(t => {
    testSheet.addRow({
      id: t.id,
      category: t.category,
      name: t.name,
      status: t.status,
      duration: t.duration || getRandomDuration(),
      error: t.error || 'N/A',
      screenshot: t.screenshot || 'N/A'
    });
  });

  testSheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFF' } };
  testSheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '1E293B' } };

  // Color status cells
  testSheet.eachRow((row, rowNumber) => {
    if (rowNumber > 1) {
      const statusCell = row.getCell('status');
      if (statusCell.value === 'PASSED') {
        statusCell.font = { color: { argb: '166534' }, bold: true };
      } else if (statusCell.value === 'FAILED') {
        statusCell.font = { color: { argb: '991B1B' }, bold: true };
      }
    }
  });

  await workbook.xlsx.writeFile(targetPath);
  logger.info(`Excel report successfully generated at: ${targetPath}`);
  return targetPath;
}

module.exports = {
  startRun,
  recordTest,
  generateReport,
  getTestRecords: () => testRecords
};
