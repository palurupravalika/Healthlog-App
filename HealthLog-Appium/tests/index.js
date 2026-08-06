/**
 * Combined Test Suite Index (500 Test Cases Total)
 */

const functionalTests = require('./functional.test');
const uiTests = require('./ui.test');
const navigationTests = require('./navigation.test');
const authTests = require('./auth.test');
const medicalTests = require('./medical.test');
const reminderTests = require('./reminder.test');
const aiHubTests = require('./ai_hub.test');
const ocrTests = require('./ocr.test');
const performanceTests = require('./performance.test');
const regressionTests = require('./regression.test');

const allTests = [
  ...functionalTests,
  ...uiTests,
  ...navigationTests,
  ...authTests,
  ...medicalTests,
  ...reminderTests,
  ...aiHubTests,
  ...ocrTests,
  ...performanceTests,
  ...regressionTests
];

module.exports = {
  allTests,
  categories: [
    "Functional Testing",
    "UI Testing",
    "Navigation Testing",
    "Authentication Testing",
    "Medical Records Testing",
    "Reminder Testing",
    "AI Hub Testing",
    "OCR Testing",
    "Performance Testing",
    "Regression Testing"
  ]
};
