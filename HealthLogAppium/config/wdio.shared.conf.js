const path = require('path');
const { APPIUM_HOST, APPIUM_PORT, PATHS } = require('../utils/constants');
const androidCapabilities = require('./android.capabilities');

exports.config = {
  runner: 'local',
  hostname: APPIUM_HOST,
  port: APPIUM_PORT,
  path: '/',

  specs: [
    path.resolve(__dirname, '../tests/**/*.test.js')
  ],
  exclude: [],

  maxInstances: 1,
  capabilities: [
    androidCapabilities
  ],

  logLevel: 'warn',
  bail: 0,
  baseUrl: 'http://localhost',
  waitforTimeout: 10000,
  connectionRetryTimeout: 120000,
  connectionRetryCount: 3,
  services: ['appium'],

  framework: 'mocha',
  reporters: ['spec'],

  mochaOpts: {
    ui: 'bdd',
    timeout: 300000
  }
};
