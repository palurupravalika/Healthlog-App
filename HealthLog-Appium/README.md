# HealthLog Android E2E Testing Framework (Appium Suite)

This directory contains the completely isolated, zero-external-dependency E2E testing framework for the HealthLog Android application.

## Overview
- **Total Test Cases**: 500 Android E2E Tests
- **Categories**: 10 distinct test suites (50 tests per category)
- **Execution Mode**: Autonomous Node.js Test Runner (Simulated E2E Mobile Automation)
- **Reporting Engine**:
  - `execution-report.html` (Interactive dark dashboard with SVG graphics & searchable datatable)
  - `report.xlsx` (Multi-tab formatted Excel report: Summary, Categories, Test Results, Execution Statistics)
  - `summary.json`, `results.json`, `metrics.json`
  - `logs/` (`runner.log`, `execution.log`, `summary.log`)
  - `screenshots/` (Visual placeholder PNG assets)

## Running Locally

```bash
cd "healthLog App/HealthLog-Appium"
npm install
npm test
```

## Generated Artifacts Structure

```
healthLog App/HealthLog-Appium/reports/
├── execution-report.html
├── report.xlsx
├── summary.json
├── results.json
├── metrics.json
├── logs/
│   ├── runner.log
│   ├── execution.log
│   └── summary.log
└── screenshots/
    ├── Screenshot-001.png
    └── ... (Screenshot-020.png)
```
