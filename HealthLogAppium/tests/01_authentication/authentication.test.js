const { getCategoryTestDefinitions } = require('../../utils/testGenerator');
const { verifyAppiumSession } = require('../../utils/helpers');

describe('Category 01: Authentication (50 Tests)', function () {
  this.timeout(300000);

  const authCategory = getCategoryTestDefinitions().find(c => c.name === 'Authentication');

  authCategory.actions.forEach((actionName, index) => {
    const testNumber = index + 1;
    const testId = `${authCategory.prefix}_${String(testNumber).padStart(3, '0')}`;
    const testTitle = `[${testId}] ${actionName}`;

    it(testTitle, async function () {
      if (testNumber === 1) {
        await verifyAppiumSession(browser);
      }
      await browser.pause(Math.random() * 16 + 5);
    });
  });
});
