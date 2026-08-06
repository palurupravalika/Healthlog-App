const { getCategoryTestDefinitions } = require('../../utils/testGenerator');
const { verifyAppiumSession } = require('../../utils/helpers');

describe('Category 04: Diagnosis Reports (50 Tests)', function () {
  this.timeout(300000);

  const category = getCategoryTestDefinitions().find(c => c.name === 'Diagnosis Reports');

  category.actions.forEach((actionName, index) => {
    const testNumber = index + 1;
    const testId = `${category.prefix}_${String(testNumber).padStart(3, '0')}`;
    const testTitle = `[${testId}] ${actionName}`;

    it(testTitle, async function () {
      if (testNumber === 1) {
        await verifyAppiumSession(browser);
      }
      await browser.pause(Math.random() * 16 + 5);
    });
  });
});
