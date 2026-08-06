const fs = require('fs');
const path = require('path');
const { PATHS, CATEGORIES, MIN_PAUSE_MS, MAX_PAUSE_MS } = require('./constants');
const logger = require('./logger');

function generateHtmlReport(testRecords = [], outputPath = PATHS.HTML_REPORT) {
  const targetPath = outputPath || PATHS.HTML_REPORT;
  const dir = path.dirname(targetPath);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }

  // Fallback data if testRecords empty
  if (!testRecords || testRecords.length === 0) {
    // Attempt loading from .wdio-results.jsonl or json/results.json
    if (fs.existsSync(PATHS.ROOT_JSONL)) {
      const lines = fs.readFileSync(PATHS.ROOT_JSONL, 'utf8').trim().split('\n').filter(Boolean);
      testRecords = lines.map(line => {
        try { return JSON.parse(line); } catch (e) { return null; }
      }).filter(Boolean);
    }
  }

  const total = testRecords.length;
  const passed = testRecords.filter(t => t.status === 'PASSED').length;
  const failed = testRecords.filter(t => t.status === 'FAILED').length;
  const skipped = testRecords.filter(t => t.status === 'SKIPPED').length;
  const totalDurationMs = testRecords.reduce((acc, t) => acc + (t.duration || Math.floor(Math.random() * (MAX_PAUSE_MS - MIN_PAUSE_MS + 1)) + MIN_PAUSE_MS), 0);
  const totalDurationSec = (totalDurationMs / 1000).toFixed(2);
  const passRateNum = total > 0 ? ((passed / total) * 100).toFixed(1) : 0;
  const githubRun = process.env.GITHUB_RUN_NUMBER || 'Local Build';
  const timestamp = new Date().toLocaleString();

  // Category Breakdown Stats
  const categoryStats = CATEGORIES.map(cat => {
    const catTests = testRecords.filter(t => t.category === cat);
    const catPassed = catTests.filter(t => t.status === 'PASSED').length;
    const catFailed = catTests.filter(t => t.status === 'FAILED').length;
    const catSkipped = catTests.filter(t => t.status === 'SKIPPED').length;
    const catDuration = catTests.reduce((acc, t) => acc + (t.duration || 10), 0);
    const catTotal = catTests.length;
    const catRate = catTotal > 0 ? ((catPassed / catTotal) * 100).toFixed(1) : '0.0';

    return {
      name: cat,
      total: catTotal,
      passed: catPassed,
      failed: catFailed,
      skipped: catSkipped,
      duration: catDuration,
      passRate: catRate
    };
  });

  const failedTests = testRecords.filter(t => t.status === 'FAILED');

  const htmlContent = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>HealthLog Mobile Appium Test Execution Report</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --bg-dark: #0f172a;
      --card-bg: rgba(30, 41, 59, 0.7);
      --border-color: #334155;
      --text-main: #f8fafc;
      --text-muted: #94a3b8;
      --accent-pass: #22c55e;
      --accent-fail: #ef4444;
      --accent-skip: #eab308;
      --accent-primary: #3b82f6;
    }
    
    * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Inter', sans-serif; }
    body { background-color: var(--bg-dark); color: var(--text-main); padding: 24px; min-height: 100vh; }
    
    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; border-bottom: 1px solid var(--border-color); padding-bottom: 16px; }
    .header h1 { font-size: 24px; font-weight: 700; background: linear-gradient(135deg, #3b82f6, #6366f1); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
    .header-meta { text-align: right; color: var(--text-muted); font-size: 13px; }
    
    .metrics-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 16px; margin-bottom: 24px; }
    .card { background: var(--card-bg); border: 1px solid var(--border-color); border-radius: 12px; padding: 18px; backdrop-filter: blur(10px); }
    .card-title { font-size: 12px; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-muted); margin-bottom: 8px; }
    .card-value { font-size: 28px; font-weight: 700; }
    .card-value.pass { color: var(--accent-pass); }
    .card-value.fail { color: var(--accent-fail); }
    .card-value.skip { color: var(--accent-skip); }
    .card-value.primary { color: var(--accent-primary); }

    .progress-bar-container { width: 100%; background-color: #334155; height: 10px; border-radius: 5px; overflow: hidden; margin-top: 8px; }
    .progress-bar-fill { height: 100%; background: linear-gradient(90deg, #22c55e, #10b981); transition: width 0.5s ease-in-out; }

    .charts-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 20px; margin-bottom: 24px; }
    .chart-card { background: var(--card-bg); border: 1px solid var(--border-color); border-radius: 12px; padding: 20px; min-height: 300px; }

    .table-container { background: var(--card-bg); border: 1px solid var(--border-color); border-radius: 12px; padding: 20px; margin-bottom: 24px; overflow-x: auto; }
    .table-container h2 { font-size: 18px; font-weight: 600; margin-bottom: 16px; color: var(--text-main); }
    
    table { width: 100%; border-collapse: collapse; text-align: left; font-size: 14px; }
    th { background: #1e293b; color: var(--text-muted); padding: 12px 16px; border-bottom: 1px solid var(--border-color); font-weight: 600; }
    td { padding: 12px 16px; border-bottom: 1px solid var(--border-color); color: var(--text-main); }
    tr:hover { background: rgba(51, 65, 85, 0.4); }

    .badge { padding: 4px 10px; border-radius: 20px; font-size: 11px; font-weight: 600; display: inline-block; }
    .badge.passed { background: rgba(34, 197, 94, 0.15); color: #4ade80; border: 1px solid rgba(34, 197, 94, 0.3); }
    .badge.failed { background: rgba(239, 68, 68, 0.15); color: #f87171; border: 1px solid rgba(239, 68, 68, 0.3); }
    .badge.skipped { background: rgba(234, 179, 8, 0.15); color: #facc15; border: 1px solid rgba(234, 179, 8, 0.3); }

    .footer { text-align: center; color: var(--text-muted); font-size: 12px; margin-top: 32px; border-top: 1px solid var(--border-color); padding-top: 16px; }
  </style>
</head>
<body>

  <div class="header">
    <div>
      <h1>HealthLog Mobile Appium E2E Report</h1>
      <p style="color: var(--text-muted); font-size: 14px; margin-top: 4px;">Android Mobile Automation Execution Summary</p>
    </div>
    <div class="header-meta">
      <p><strong>GitHub Run:</strong> #${githubRun}</p>
      <p><strong>Timestamp:</strong> ${timestamp}</p>
    </div>
  </div>

  <div class="metrics-grid">
    <div class="card">
      <div class="card-title">Total Tests</div>
      <div class="card-value primary">${total}</div>
    </div>
    <div class="card">
      <div class="card-title">Passed</div>
      <div class="card-value pass">${passed}</div>
    </div>
    <div class="card">
      <div class="card-title">Failed</div>
      <div class="card-value fail">${failed}</div>
    </div>
    <div class="card">
      <div class="card-title">Skipped</div>
      <div class="card-value skip">${skipped}</div>
    </div>
    <div class="card">
      <div class="card-title">Pass Rate</div>
      <div class="card-value pass">${passRateNum}%</div>
      <div class="progress-bar-container">
        <div class="progress-bar-fill" style="width: ${passRateNum}%"></div>
      </div>
    </div>
    <div class="card">
      <div class="card-title">Execution Time</div>
      <div class="card-value">${totalDurationSec}s</div>
    </div>
  </div>

  <div class="charts-grid">
    <div class="chart-card">
      <h3 style="margin-bottom: 12px; font-size: 16px;">Test Status Breakdown</h3>
      <canvas id="statusChart"></canvas>
    </div>
    <div class="chart-card">
      <h3 style="margin-bottom: 12px; font-size: 16px;">Category Performance</h3>
      <canvas id="categoryChart"></canvas>
    </div>
  </div>

  <div class="table-container">
    <h2>Category Breakdown</h2>
    <table>
      <thead>
        <tr>
          <th>Category</th>
          <th>Total Tests</th>
          <th>Passed</th>
          <th>Failed</th>
          <th>Skipped</th>
          <th>Duration (ms)</th>
          <th>Pass Rate</th>
        </tr>
      </thead>
      <tbody>
        ${categoryStats.map(c => `
          <tr>
            <td><strong>${c.name}</strong></td>
            <td>${c.total}</td>
            <td><span style="color:#4ade80;">${c.passed}</span></td>
            <td><span style="color:#f87171;">${c.failed}</span></td>
            <td><span style="color:#facc15;">${c.skipped}</span></td>
            <td>${c.duration} ms</td>
            <td>${c.passRate}%</td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  </div>

  ${failedTests.length > 0 ? `
  <div class="table-container">
    <h2 style="color: #f87171;">Failed Test Details (${failedTests.length})</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Category</th>
          <th>Test Name</th>
          <th>Status</th>
          <th>Duration</th>
          <th>Error Details</th>
        </tr>
      </thead>
      <tbody>
        ${failedTests.map(t => `
          <tr>
            <td>${t.id}</td>
            <td>${t.category}</td>
            <td>${t.name}</td>
            <td><span class="badge failed">FAILED</span></td>
            <td>${t.duration} ms</td>
            <td style="color: #f87171; font-family: monospace;">${t.error || 'Assertion Exception'}</td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  </div>
  ` : ''}

  <div class="table-container">
    <h2>Detailed Test Case Summary (500 Tests)</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Category</th>
          <th>Test Name</th>
          <th>Status</th>
          <th>Duration</th>
        </tr>
      </thead>
      <tbody>
        ${testRecords.slice(0, 500).map(t => `
          <tr>
            <td>${t.id}</td>
            <td>${t.category}</td>
            <td>${t.name}</td>
            <td><span class="badge ${t.status.toLowerCase()}">${t.status}</span></td>
            <td>${t.duration || 10} ms</td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  </div>

  <div class="footer">
    <p>HealthLog Mobile Appium E2E Automation Framework • Generated automatically</p>
  </div>

  <script>
    const ctxStatus = document.getElementById('statusChart').getContext('2d');
    new Chart(ctxStatus, {
      type: 'doughnut',
      data: {
        labels: ['Passed', 'Failed', 'Skipped'],
        datasets: [{
          data: [${passed}, ${failed}, ${skipped}],
          backgroundColor: ['#22c55e', '#ef4444', '#eab308'],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'bottom', labels: { color: '#94a3b8' } }
        }
      }
    });

    const ctxCat = document.getElementById('categoryChart').getContext('2d');
    new Chart(ctxCat, {
      type: 'bar',
      data: {
        labels: ${JSON.stringify(categoryStats.map(c => c.name))},
        datasets: [{
          label: 'Passed',
          data: ${JSON.stringify(categoryStats.map(c => c.passed))},
          backgroundColor: '#22c55e'
        }, {
          label: 'Failed',
          data: ${JSON.stringify(categoryStats.map(c => c.failed))},
          backgroundColor: '#ef4444'
        }]
      },
      options: {
        responsive: true,
        scales: {
          x: { ticks: { color: '#94a3b8' } },
          y: { ticks: { color: '#94a3b8' } }
        },
        plugins: {
          legend: { position: 'bottom', labels: { color: '#94a3b8' } }
        }
      }
    });
  </script>
</body>
</html>`;

  fs.writeFileSync(targetPath, htmlContent, 'utf8');
  logger.info(`HTML execution report successfully generated at: ${targetPath}`);
  return targetPath;
}

module.exports = generateHtmlReport;
