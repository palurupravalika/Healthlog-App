const path = require('path');
const { APP_PACKAGE, APP_ACTIVITY } = require('../utils/constants');

const DEFAULT_APK_PATH = path.resolve(__dirname, '../../app/build/outputs/apk/debug/app-debug.apk');

const caps = {
  platformName: 'Android',
  'appium:automationName': 'UiAutomator2',
  'appium:deviceName': process.env.ANDROID_DEVICE_NAME || 'Android',
  'appium:app': process.env.APK_PATH || DEFAULT_APK_PATH,
  'appium:appPackage': APP_PACKAGE,
  'appium:appActivity': APP_ACTIVITY,
  'appium:noReset': false,
  'appium:fullReset': false,
  'appium:autoGrantPermissions': true,
  'appium:newCommandTimeout': 180,
  'appium:uiautomator2ServerInstallTimeout': 60000
};

if (process.env.ANDROID_PLATFORM_VERSION) {
  caps['appium:platformVersion'] = process.env.ANDROID_PLATFORM_VERSION;
}

module.exports = caps;
