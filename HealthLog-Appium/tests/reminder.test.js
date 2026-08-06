/**
 * Reminder Testing Suite (50 Test Cases)
 */

const titles = [
  "Verify Create Daily Medication Reminder", "Validate Multi-Frequency Alarm Selection", "Check Custom Reminder Sound Chooser",
  "Verify Snooze Button (5 min, 10 min, 15 min)", "Check Take Medicine Action Button on Push Alert", "Validate Skip Pill Action with Reason Log",
  "Verify Refill Alert when Inventory falls below 5 Pills", "Check Doctor Appointment Reminder 24 Hours Prior", "Validate Doctor Appointment Reminder 1 Hour Prior",
  "Verify Water Intake Hourly Reminder Trigger", "Check Blood Pressure Measurement Alarm", "Validate Glucose Check Reminder Post-Meal",
  "Verify Sleep Schedule Wind-down Notification", "Check Exercise / Walk Alarm Setup", "Validate Repeat Schedule (Mon-Fri vs Weekends)",
  "Verify Custom Date Range for Antibiotic Course", "Check Missed Dose Auto-Log Entry", "Validate Reminder History Calendar View",
  "Verify Silent Mode Override for Critical Alarms", "Check Vibration Pattern Selector", "Validate Timezone Change Alarm Reschedule",
  "Verify Daylight Savings Time Adjust", "Check Device Restart Alarm Reschedule Broadcast", "Validate Alarm Manager Exact Time Permission Gate",
  "Verify Notification Badge Counter Increment", "Check Notification Grouping by Category", "Validate Dismiss All Active Reminders Action",
  "Verify Quick Add Reminder from Home Screen Widget", "Check Pill Count Increment after Confirmed Intake", "Validate Medication Stock Calculation",
  "Verify Doctor Consultation Follow-up Alert", "Check Vaccine Booster Shot Reminder", "Validate Lab Test Fasting Reminder", "Verify Hydration Goal Completion Alert",
  "Check Custom Text Note in Push Payload", "Validate Caregiver Notification on Missed Dose", "Verify Wearable Smartwatch Alarm Sync",
  "Check Alarm Dismissal Verification Lock", "Validate Multiple Medication Combined Alarm", "Verify Post-Surgery Recovery Checklist Alarm",
  "Check Prescription Expiration Alert", "Validate Daily Health Survey Push Prompt", "Verify Snooze Limit Max Threshold",
  "Check Recurring Alarm End-Date Expiration", "Validate Quiet Hours / Do Not Disturb Exception", "Verify Reminder Audio Fade-In Effect",
  "Check Battery Optimization Whitelist Gate for Alarms", "Validate In-App Banner Alarm Alert", "Verify Reminder Color Coding by Urgency",
  "Check Export Alarm Schedule to iCal / Google Calendar"
];

const tests = titles.map((title, idx) => {
  const num = (idx + 1).toString().padStart(3, '0');
  return {
    id: `TC-REM-${num}`,
    category: "Reminder Testing",
    title: title,
    description: `Validate local notifications, scheduled background tasks, and alerts for ${title.toLowerCase()}.`,
    simulatedDelayMs: Math.floor(Math.random() * 16) + 5,
    simulatedDurationMs: Math.floor(Math.random() * 330) + 120
  };
});

module.exports = tests;
