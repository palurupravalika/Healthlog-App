const fs = require('fs');
const path = require('path');

// Base64 of a valid 16x16 PNG image placeholder with teal health theme accent
const MINIMAL_PNG_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAQSURBVDhPY2AYBaNgFNAHAAAHsAABo7c3mQAAAABJRU5ErkJggg==";

function generateScreenshots(screenshotsDir, count = 20) {
  if (!fs.existsSync(screenshotsDir)) {
    fs.mkdirSync(screenshotsDir, { recursive: true });
  }

  const pngBuffer = Buffer.from(MINIMAL_PNG_BASE64, 'base64');
  const generatedFiles = [];

  for (let i = 1; i <= count; i++) {
    const filename = `Screenshot-${i.toString().padStart(3, '0')}.png`;
    const filePath = path.join(screenshotsDir, filename);
    fs.writeFileSync(filePath, pngBuffer);
    generatedFiles.push(filename);
  }

  return generatedFiles;
}

module.exports = { generateScreenshots };
