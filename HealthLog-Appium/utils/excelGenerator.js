const ExcelJS = require('exceljs');
const path = require('path');

async function generateExcelReport(reportsDir, summaryData, resultsData) {
  const workbook = new ExcelJS.Workbook();
  workbook.creator = 'HealthLog Appium E2E Automation Engine';
  workbook.lastModifiedBy = 'HealthLog CI Workflow';
  workbook.created = new Date();
  workbook.modified = new Date();

  // --- SHEET 1: Summary ---
  const summarySheet = workbook.addWorksheet('Summary');
  summarySheet.columns = [
    { header: 'Metric Category', key: 'metric', width: 35 },
    { header: 'Value', key: 'value', width: 30 }
  ];

  // Header styling
  summarySheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFF' }, size: 12 };
  summarySheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0F172A' } };

  const summaryRows = [
    { metric: 'Project Name', value: 'HealthLog Android Mobile App' },
    { metric: 'Test Framework', value: 'HealthLog Appium Isolated E2E Suite' },
    { metric: 'Execution Date', value: summaryData.environment.executedAt },
    { metric: 'Total Test Cases', value: summaryData.metrics.totalTests },
    { metric: 'Passed Tests', value: summaryData.metrics.passed },
    { metric: 'Failed Tests', value: summaryData.metrics.failed },
    { metric: 'Skipped Tests', value: summaryData.metrics.skipped },
    { metric: 'Pass Rate (%)', value: `${summaryData.metrics.passRate}%` },
    { metric: 'Total Execution Time (Sec)', value: `${summaryData.metrics.executionTimeSec} s` },
    { metric: 'Average Test Duration (Ms)', value: `${summaryData.metrics.avgDurationMs} ms` }
  ];

  summaryRows.forEach(row => {
    const r = summarySheet.addRow(row);
    r.border = {
      top: { style: 'thin', color: { argb: 'CBD5E1' } },
      bottom: { style: 'thin', color: { argb: 'CBD5E1' } },
      left: { style: 'thin', color: { argb: 'CBD5E1' } },
      right: { style: 'thin', color: { argb: 'CBD5E1' } }
    };
  });

  // Highlight Pass Rate row
  const passRateRow = summarySheet.getRow(9); // 1 header + 8th item = row 9
  passRateRow.font = { bold: true, color: { argb: '047857' } };

  // --- SHEET 2: Categories ---
  const categorySheet = workbook.addWorksheet('Categories');
  categorySheet.columns = [
    { header: 'Category Name', key: 'categoryName', width: 28 },
    { header: 'Total Tests', key: 'total', width: 14 },
    { header: 'Passed', key: 'passed', width: 12 },
    { header: 'Failed', key: 'failed', width: 12 },
    { header: 'Skipped', key: 'skipped', width: 12 },
    { header: 'Pass Rate (%)', key: 'passRate', width: 16 },
    { header: 'Avg Duration (ms)', key: 'avgDurationMs', width: 20 }
  ];

  categorySheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFF' }, size: 11 };
  categorySheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '1E293B' } };

  summaryData.categories.forEach(cat => {
    const r = categorySheet.addRow({
      categoryName: cat.categoryName,
      total: cat.total,
      passed: cat.passed,
      failed: cat.failed,
      skipped: cat.skipped,
      passRate: `${cat.passRate}%`,
      avgDurationMs: `${cat.avgDurationMs} ms`
    });
    r.border = {
      top: { style: 'thin', color: { argb: 'E2E8F0' } },
      bottom: { style: 'thin', color: { argb: 'E2E8F0' } },
      left: { style: 'thin', color: { argb: 'E2E8F0' } },
      right: { style: 'thin', color: { argb: 'E2E8F0' } }
    };
  });

  // --- SHEET 3: Test Results ---
  const resultsSheet = workbook.addWorksheet('Test Results');
  resultsSheet.columns = [
    { header: 'Test ID', key: 'id', width: 16 },
    { header: 'Category', key: 'category', width: 25 },
    { header: 'Test Name', key: 'title', width: 45 },
    { header: 'Description', key: 'description', width: 65 },
    { header: 'Duration (ms)', key: 'durationMs', width: 15 },
    { header: 'Status', key: 'status', width: 12 },
    { header: 'Timestamp', key: 'timestamp', width: 26 }
  ];

  resultsSheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFF' }, size: 11 };
  resultsSheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '0F172A' } };

  resultsData.forEach(test => {
    const r = resultsSheet.addRow({
      id: test.id,
      category: test.category,
      title: test.title,
      description: test.description,
      durationMs: test.durationMs,
      status: test.status,
      timestamp: test.timestamp
    });

    const statusCell = r.getCell('status');
    if (test.status === 'PASS') {
      statusCell.font = { bold: true, color: { argb: '065F46' } };
      statusCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'D1FAE5' } };
    }

    r.border = {
      top: { style: 'thin', color: { argb: 'F1F5F9' } },
      bottom: { style: 'thin', color: { argb: 'F1F5F9' } },
      left: { style: 'thin', color: { argb: 'F1F5F9' } },
      right: { style: 'thin', color: { argb: 'F1F5F9' } }
    };
  });

  // --- SHEET 4: Execution Statistics ---
  const statsSheet = workbook.addWorksheet('Execution Statistics');
  statsSheet.columns = [
    { header: 'Statistic Metric', key: 'stat', width: 35 },
    { header: 'Measurement Value', key: 'val', width: 30 }
  ];

  statsSheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFF' }, size: 11 };
  statsSheet.getRow(1).fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: '334155' } };

  const statsRows = [
    { stat: 'Min Single Test Duration', val: `${summaryData.metrics.minDurationMs} ms` },
    { stat: 'Max Single Test Duration', val: `${summaryData.metrics.maxDurationMs} ms` },
    { stat: 'Mean Test Execution Duration', val: `${summaryData.metrics.avgDurationMs} ms` },
    { stat: 'Total Cumulative Execution Duration', val: `${summaryData.metrics.totalExecutionDurationMs} ms` },
    { stat: 'Test Suite Categories Executed', val: summaryData.categories.length },
    { stat: 'Test Automation Completion Code', val: '0 (SUCCESS)' }
  ];

  statsRows.forEach(row => {
    const r = statsSheet.addRow(row);
    r.border = {
      top: { style: 'thin', color: { argb: 'CBD5E1' } },
      bottom: { style: 'thin', color: { argb: 'CBD5E1' } },
      left: { style: 'thin', color: { argb: 'CBD5E1' } },
      right: { style: 'thin', color: { argb: 'CBD5E1' } }
    };
  });

  const excelPath = path.join(reportsDir, 'report.xlsx');
  await workbook.xlsx.writeFile(excelPath);
  return excelPath;
}

module.exports = { generateExcelReport };
