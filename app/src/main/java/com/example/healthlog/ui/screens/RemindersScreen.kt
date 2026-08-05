package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    reminders: List<Reminder>,
    filterType: ReminderType? = null,
    onBackClick: () -> Unit,
    onAddReminder: (Reminder) -> Unit,
    onDeleteReminder: (Reminder) -> Unit
) {
    var showTypeSelection by remember { mutableStateOf(false) }
    var selectedTypeForAdd by remember { mutableStateOf<ReminderType?>(null) }
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }

    val filteredReminders = if (filterType != null) {
        reminders.filter { it.type == filterType }
    } else {
        reminders
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = when(filterType) {
                            ReminderType.Medicine -> "Medicine Alerts"
                            ReminderType.DoctorAppointment -> "Doctor Visits"
                            else -> "Health Schedules"
                        },
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showTypeSelection = true },
                containerColor = LavenderPrimary,
                contentColor = White,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.shadow(12.dp, RoundedCornerShape(22.dp), spotColor = LavenderPrimary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(32.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (filteredReminders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(160.dp),
                            shape = CircleShape,
                            color = LavenderLight.copy(alpha = 0.2f),
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsNone,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = LavenderPrimary.copy(alpha = 0.4f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Your schedule is clear",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap + to set a health reminder",
                            fontSize = 14.sp,
                            color = Gray500,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 100.dp, start = 24.dp, end = 24.dp, top = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredReminders) { reminder ->
                        CreativeReminderCard(reminder, onDelete = { reminderToDelete = reminder })
                    }
                }
            }
        }
    }

    if (showTypeSelection) {
        ReminderTypeSelectionDialog(
            onTypeSelected = { type ->
                selectedTypeForAdd = type
                showTypeSelection = false
            },
            onDismiss = { showTypeSelection = false }
        )
    }

    if (selectedTypeForAdd != null) {
        AddReminderDialog(
            type = selectedTypeForAdd!!,
            onDismiss = { selectedTypeForAdd = null },
            onSave = { reminder ->
                onAddReminder(reminder)
                selectedTypeForAdd = null
            }
        )
    }

    if (reminderToDelete != null) {
        AlertDialog(
            onDismissRequest = { reminderToDelete = null },
            title = { Text("Remove Reminder?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("AI will stop tracking this schedule. Proceed with deletion?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteReminder(reminderToDelete!!)
                        reminderToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold, color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { reminderToDelete = null }) {
                    Text("Cancel", color = Gray500, fontWeight = FontWeight.Medium)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun ReminderTypeSelectionDialog(
    onTypeSelected: (ReminderType) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Type", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 8.dp)) {
                TypeOption(
                    title = "Medicine Reminder",
                    icon = Icons.Default.Medication,
                    gradient = PremiumGradient,
                    onClick = { onTypeSelected(ReminderType.Medicine) }
                )
                TypeOption(
                    title = "Doctor Appointment",
                    icon = Icons.Default.Event,
                    gradient = BlueGradient,
                    onClick = { onTypeSelected(ReminderType.DoctorAppointment) }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Gray500, fontWeight = FontWeight.Bold) }
        },
        shape = RoundedCornerShape(32.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun TypeOption(title: String, icon: ImageVector, gradient: List<Color>, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = gradient[0].copy(alpha = 0.2f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .background(Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.surface, gradient[0].copy(alpha = 0.05f))))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = White, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
        }
    }
}

@Composable
fun CreativeReminderCard(reminder: Reminder, onDelete: () -> Unit) {
    val isMedicine = reminder.type == ReminderType.Medicine
    val gradient = if (isMedicine) PremiumGradient else BlueGradient
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = gradient[0].copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .background(Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.surface, gradient[0].copy(alpha = 0.05f))))
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isMedicine) Icons.Default.Medication else Icons.Default.Event,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime, 
                        contentDescription = null, 
                        modifier = Modifier.size(14.dp),
                        tint = Gray500
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${reminder.date} • ${reminder.time}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(40.dp)
                    .background(ErrorRed.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    type: ReminderType,
    onDismiss: () -> Unit, 
    onSave: (Reminder) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var hospitalName by remember { mutableStateOf("") }
    var reasonForVisit by remember { mutableStateOf("") }
    
    val calendar = Calendar.getInstance()
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE)
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                        date = sdf.format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("Confirm", color = LavenderPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = Gray500) }
            },
            shape = RoundedCornerShape(32.dp),
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val hour = if (timePickerState.hour % 12 == 0) 12 else timePickerState.hour % 12
                    val minute = String.format("%02d", timePickerState.minute)
                    val amPm = if (timePickerState.hour < 12) "AM" else "PM"
                    time = "$hour:$minute $amPm"
                    showTimePicker = false
                }) { Text("Confirm", color = LavenderPrimary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel", color = Gray500) }
            },
            text = {
                TimePicker(state = timePickerState)
            },
            shape = RoundedCornerShape(32.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                if (type == ReminderType.Medicine) "New Medication Alert" else "New Doctor Visit",
                fontWeight = FontWeight.ExtraBold, 
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            ) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top = 8.dp)) {
                if (type == ReminderType.Medicine) {
                    PremiumTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = "Medicine Name / Instructions"
                    )
                } else {
                    PremiumTextField(
                        value = hospitalName,
                        onValueChange = { hospitalName = it },
                        label = "Hospital / Clinic"
                    )
                    PremiumTextField(
                        value = reasonForVisit,
                        onValueChange = { reasonForVisit = it },
                        label = "Reason for Visit"
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.weight(1f).shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (date.isBlank()) "Date" else date,
                                fontSize = 14.sp,
                                color = if (date.isBlank()) Gray500 else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Surface(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.weight(1f).shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (time.isBlank()) "Time" else time,
                                fontSize = 14.sp,
                                color = if (time.isBlank()) Gray500 else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalTitle = if (type == ReminderType.Medicine) title else "$hospitalName - $reasonForVisit"
                    if (finalTitle.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {
                        onSave(Reminder(title = finalTitle, type = type, date = date, time = time))
                    }
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, end = 8.dp, start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LavenderPrimary)
            ) {
                Text("Confirm Schedule", fontWeight = FontWeight.Bold, color = White)
            }
        },
        shape = RoundedCornerShape(32.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
