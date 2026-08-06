const { APPIUM_HOST, APPIUM_PORT } = require('../utils/constants');

module.exports = {
  hostname: APPIUM_HOST,
  port: APPIUM_PORT,
  path: '/',
  logLevel: 'warn',
  capabilities: [
    require('./android.capabilities')
  ]
};
