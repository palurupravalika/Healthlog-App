package com.example.healthlog.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    profile: Profile,
    onBackClick: () -> Unit,
    onAddRecordClick: () -> Unit,
    onRecordClick: (Int) -> Unit,
    onUpdateRecord: (Int, MedicalRecord, Uri?) -> Unit,
    onDeleteRecord: (Int) -> Unit
) {
    var recordToEdit by remember { mutableStateOf<Pair<Int, MedicalRecord>?>(null) }
    var recordToDelete by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Member Profile", 
                        fontWeight = FontWeight.ExtraBold, 
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onAddRecordClick,
                containerColor = LavenderPrimary,
                contentColor = White,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.shadow(12.dp, RoundedCornerShape(22.dp), spotColor = LavenderPrimary)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Record", modifier = Modifier.size(32.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Premium 3D Profile Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(32.dp), spotColor = LavenderPrimary.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.surface,
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(80.dp),
                                shape = CircleShape,
                                color = LavenderLight.copy(alpha = 0.2f),
                                border = BorderStroke(3.dp, MaterialTheme.colorScheme.surface),
                                shadowElevation = 4.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = LavenderPrimary,
                                        modifier = Modifier.size(45.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column {
                                Text(
                                    text = profile.name, 
                                    fontWeight = FontWeight.ExtraBold, 
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    letterSpacing = (-0.5).sp
                                )
                                Text(
                                    text = "${profile.relationship} • ${profile.ageOrDob}", 
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = LavenderPrimary,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            profile.bloodGroup, 
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            color = White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "${profile.height} cm | ${profile.weight} kg", 
                                        fontSize = 13.sp, 
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Medical History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Surface(
                    color = LavenderLight.copy(alpha = 0.2f),
                    shape = CircleShape
                ) {
                    Text(
                        text = "${profile.records?.size ?: 0} Records",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LavenderPrimary
                    )
                }
            }

            val recordList = profile.records.orEmpty()
            if (recordList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(100.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Gray500.copy(alpha = 0.3f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "No health records found",
                            color = Gray500,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap + to add your first record",
                            color = Gray500.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp, start = 24.dp, end = 24.dp, top = 16.dp)
                ) {
                    items(recordList.indices.toList()) { index ->
                        RecordCard(
                            record = recordList[index],
                            onClick = { onRecordClick(index) },
                            onEditClick = { recordToEdit = index to recordList[index] },
                            onDeleteClick = { recordToDelete = index }
                        )
                    }
                }
            }
        }
    }

    // Edit Dialog
    recordToEdit?.let { (index, record) ->
        RecordDialog(
            title = "Update Record",
            initialHospital = record.hospital,
            initialReason = record.reason,
            initialDiagnosis = record.diagnosis,
            initialDate = record.date,
            onDismiss = { recordToEdit = null },
            onConfirm = { hospital, reason, diagnosis, date, newReportUri ->
                onUpdateRecord(index, record.copy(
                    hospital = hospital,
                    reason = reason,
                    diagnosis = diagnosis,
                    date = date
                ), newReportUri)
                recordToEdit = null
            }
        )
    }

    // Delete Confirmation
    recordToDelete?.let { index ->
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            title = { Text("Delete Record", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("Are you sure you want to remove this medical log? This cannot be undone.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteRecord(index)
                        recordToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold, color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text("Cancel", color = Gray500, fontWeight = FontWeight.Medium)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun RecordCard(
    record: MedicalRecord, 
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = LavenderPrimary.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LavenderLight.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Description, 
                    contentDescription = null, 
                    tint = LavenderPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.hospital, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = record.reason, 
                    fontSize = 13.sp, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = record.date, 
                    fontSize = 12.sp, 
                    color = Gray500,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Gray500)
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface).clip(RoundedCornerShape(16.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Record", fontWeight = FontWeight.Bold, color = LavenderPrimary) },
                        onClick = {
                            showMenu = false
                            onEditClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", fontWeight = FontWeight.Bold, color = ErrorRed) },
                        onClick = {
                            showMenu = false
                            onDeleteClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecordDialog(
    title: String,
    initialHospital: String = "",
    initialReason: String = "",
    initialDiagnosis: String = "",
    initialDate: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Uri?) -> Unit
) {
    var hospital by remember { mutableStateOf(initialHospital) }
    var reason by remember { mutableStateOf(initialReason) }
    var diagnosis by remember { mutableStateOf(initialDiagnosis) }
    var date by remember { mutableStateOf(initialDate) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp), 
                modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
            ) {
                PremiumTextField(value = hospital, onValueChange = { hospital = it }, label = "Hospital Name")
                PremiumTextField(value = reason, onValueChange = { reason = it }, label = "Reason for Visit")
                PremiumTextField(value = diagnosis, onValueChange = { diagnosis = it }, label = "Diagnosis", minLines = 2)
                PremiumTextField(value = date, onValueChange = { date = it }, label = "Date (DD/MM/YYYY)")
                
                // Report Update Section in Dialog
                Surface(
                    onClick = { launcher.launch("*/*") },
                    shape = RoundedCornerShape(16.dp),
                    color = LavenderLight.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (selectedUri != null) Icons.Default.PictureAsPdf else Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = LavenderPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (selectedUri != null) "New Report Selected" else "Update Digital Report",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = LavenderPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(hospital, reason, diagnosis, date, selectedUri) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LavenderPrimary),
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp)
            ) {
                Text("Update", fontWeight = FontWeight.Bold, color = White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.padding(bottom = 8.dp)) {
                Text("Cancel", color = Gray500, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(32.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
