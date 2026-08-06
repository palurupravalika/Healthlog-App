/**
 * Medical Records Testing Suite (50 Test Cases)
 */

const titles = [
  "Verify Add New Prescription Record", "Validate Prescription Dosage Unit Selector", "Check Doctor Name Search Autocomplete",
  "Verify Edit Existing Medical Note", "Check Delete Record Confirmation Modal", "Validate Lab Test Result Value Validation",
  "Verify Blood Pressure Reading Log (Systolic/Diastolic)", "Check Glucose Level Tag (Fasting vs Post-Meal)", "Validate Cholesterol Sub-types (HDL/LDL)",
  "Verify Heart Rate ECG Graph Generation", "Check Body Temperature Scale Toggle (C/F)", "Validate Oxygen Saturation (SpO2) Range Warning",
  "Verify Medical Attachment File Upload (PDF)", "Check Medical Attachment Image Preview", "Validate PDF Encryption Key Generation",
  "Verify Record Date Sorting (Newest First)", "Check Category Filter (Cardiology, Neurology, etc.)", "Validate Full-Text Record Keyword Search",
  "Verify Export Health Summary to PDF", "Check Export Record Data to Encrypted Zip", "Validate Share Record via Secure Doctor Portal",
  "Verify Medical History Timeline Visualization", "Check Chronic Condition Tagging", "Validate Surgical History Entry",
  "Verify Vaccination Passport QR Code Generator", "Check Allergy Severity Classification", "Validate Family Medical History Tree Input",
  "Verify Vital Signs Normal Range Indicators", "Check Abnormal Test Value Highlight (Red Flag)", "Validate Provider NPI Identifier Lookup",
  "Verify Pharmacy Contact Detail Association", "Check Refill Request History Tracker", "Validate Medication Interaction Warning Alert",
  "Verify Multi-Page Medical Document Stitching", "Check Record Archival & Restore", "Validate Offline Medical Data Cache",
  "Verify Record Sync with Health Connect / Google Fit", "Check FHIR Data Format Standard Export", "Validate HL7 Message Parser",
  "Verify ICD-10 Code Auto-suggest", "Check Symptom Intensity Scale (1-10)", "Validate Diagnostic Image Viewer Zoom",
  "Verify Radiology Report Tagging", "Check Emergency Medical ID Card Rendering", "Validate Lock Record with Secondary PIN",
  "Verify Audit Trail for Medical Data Access", "Check Doctor Signature Verification", "Validate Medical Record Tag Management",
  "Verify Record Duplicate Warning", "Check Cloud Backup Integrity Check sum"
];

const tests = titles.map((title, idx) => {
  const num = (idx + 1).toString().padStart(3, '0');
  return {
    id: `TC-MED-${num}`,
    category: "Medical Records Testing",
    title: title,
    description: `Test health record management, EHR integrations, and data validation for ${title.toLowerCase()}.`,
    simulatedDelayMs: Math.floor(Math.random() * 16) + 5,
    simulatedDurationMs: Math.floor(Math.random() * 360) + 130
  };
});

module.exports = tests;
