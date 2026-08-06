/**
 * OCR Testing Suite (50 Test Cases)
 */

const titles = [
  "Verify Camera Scan Initialization for Prescription", "Validate Gallery Image Pick for OCR Scan", "Check OCR Bounding Box Overlay on Text Region",
  "Verify Automatic Flashlight Toggle in Low Light Scanner", "Check Image Auto-Crop & Perspective Correction", "Validate Image Brightness/Contrast Enhancement Pre-filter",
  "Verify Extraction of Patient Name from Prescription Scan", "Check Extraction of Medication Name from OCR Text", "Validate Extraction of Dosage Quantity & Unit",
  "Verify Extraction of Refill Count & Expiry Date", "Check Doctor License Number OCR Extraction", "Validate Pharmacy Stamp Detection",
  "Verify OCR Scan on Printed Lab Test Report", "Check Lab Test Parameter Extraction (e.g. Hemoglobin 14.2)", "Validate Lab Reference Range OCR Parser",
  "Verify Handwriting OCR Text Recognition Accuracy", "Check Multilingual Prescription OCR Scan", "Validate Low Resolution Image Warning Prompt",
  "Verify Blurry Image Blur Detection Filter", "Check Skewed Document Rotation Adjuster", "Validate Multi-Page Document Batch OCR Processing",
  "Verify Raw OCR Text Preview Modal", "Check Editable OCR Field Correction Form", "Validate Auto-Fill Prescription Form from OCR Output",
  "Verify Confident Score Badge per Extracted Field", "Check Unrecognized Text Highlight in Red", "Validate Save Extracted Data to Medical Records",
  "Verify Camera Pinch-to-Zoom Gesture in OCR Mode", "Check Camera Tap-to-Focus Indicator", "Validate OCR Processing Progress Indicator",
  "Verify On-Device ML Kit OCR Offline Execution", "Check Cloud Vision OCR API Fallback", "Validate OCR Engine Switch (Tesseract vs MLKit)",
  "Verify QR Code / Barcode Scan inside Prescription", "Check Insurance Card OCR Name & Policy ID Extraction", "Validate Vaccine Certificate Pass Scan",
  "Verify Medical Bill Amount & Date OCR Extractor", "Check Doctor Signature Region Masking for Privacy", "Validate Image File Format Compatibility (JPEG, PNG, HEIC)",
  "Verify OCR Cache Cleanup on Storage Limit", "Check OCR Scan Audit Log Creation", "Validate PDF Vector Text Direct Extraction",
  "Verify Dark Mode OCR Camera View Finder Visual", "Check Edge Detection Outline Box Rendering", "Validate Shutter Sound Effect Toggle",
  "Verify Re-scan Retake Button Flow", "Check Cancel Scan Navigation Return", "Validate OCR Extracted Data Diff Comparison",
  "Verify Privacy Consent for Document Scanning", "Check Memory Allocation under Consecutive 50 OCR Scans"
];

const tests = titles.map((title, idx) => {
  const num = (idx + 1).toString().padStart(3, '0');
  return {
    id: `TC-OCR-${num}`,
    category: "OCR Testing",
    title: title,
    description: `Validate Optical Character Recognition, document scanning, and data extraction for ${title.toLowerCase()}.`,
    simulatedDelayMs: Math.floor(Math.random() * 16) + 5,
    simulatedDurationMs: Math.floor(Math.random() * 450) + 180
  };
});

module.exports = tests;
