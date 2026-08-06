const fs = require('fs');
const path = require('path');
const { PATHS } = require('./constants');

function ensureLogDir() {
  if (!fs.existsSync(PATHS.LOGS_DIR)) {
    fs.mkdirSync(PATHS.LOGS_DIR, { recursive: true });
  }
}

function writeLog(level, message) {
  ensureLogDir();
  const timestamp = new Date().toISOString();
  const logLine = `[${timestamp}] [${level.toUpperCase()}] ${message}\n`;
  
  console.log(logLine.trim());
  try {
    fs.appendFileSync(PATHS.WDIO_LOG, logLine, 'utf8');
  } catch (err) {
    // Ignore fallback log write error
  }
}

module.exports = {
  info: (msg) => writeLog('INFO', msg),
  warn: (msg) => writeLog('WARN', msg),
  error: (msg) => writeLog('ERROR', msg),
  debug: (msg) => writeLog('DEBUG', msg)
};
