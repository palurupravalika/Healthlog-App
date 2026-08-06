/**
 * Summary Generator Utility
 * Computes execution metrics, category breakdown, pass rates, and timing statistics.
 */

function generateSummaryMetrics(results, totalWallTimeSec) {
  const totalTests = results.length;
  const passed = results.filter(r => r.status === 'PASS').length;
  const failed = results.filter(r => r.status === 'FAIL').length;
  const skipped = results.filter(r => r.status === 'SKIP').length;
  const passRate = totalTests > 0 ? ((passed / totalTests) * 100).toFixed(2) : "0.00";

  const totalExecutionDurationMs = results.reduce((acc, r) => acc + r.durationMs, 0);
  const avgDurationMs = (totalExecutionDurationMs / totalTests).toFixed(2);
  const minDurationMs = Math.min(...results.map(r => r.durationMs));
  const maxDurationMs = Math.max(...results.map(r => r.durationMs));

  // Category breakdown
  const categoryStats = {};
  results.forEach(r => {
    if (!categoryStats[r.category]) {
      categoryStats[r.category] = {
        categoryName: r.category,
        total: 0,
        passed: 0,
        failed: 0,
        skipped: 0,
        totalDurationMs: 0
      };
    }
    const cat = categoryStats[r.category];
    cat.total += 1;
    if (r.status === 'PASS') cat.passed += 1;
    else if (r.status === 'FAIL') cat.failed += 1;
    else cat.skipped += 1;
    cat.totalDurationMs += r.durationMs;
  });

  const categories = Object.values(categoryStats).map(cat => ({
    ...cat,
    passRate: ((cat.passed / cat.total) * 100).toFixed(2),
    avgDurationMs: (cat.totalDurationMs / cat.total).toFixed(2)
  }));

  return {
    environment: {
      framework: "HealthLog Isolated Appium Test Suite",
      platform: "Android Mobile E2E (Automated Execution)",
      nodeVersion: process.version,
      runnerOS: process.platform,
      executedAt: new Date().toISOString()
    },
    metrics: {
      totalTests,
      passed,
      failed,
      skipped,
      passRate: parseFloat(passRate),
      passRateFormatted: `${passRate}%`,
      executionTimeSec: parseFloat(totalWallTimeSec.toFixed(2)),
      totalExecutionDurationMs,
      avgDurationMs: parseFloat(avgDurationMs),
      minDurationMs,
      maxDurationMs
    },
    categories
  };
}

module.exports = { generateSummaryMetrics };
