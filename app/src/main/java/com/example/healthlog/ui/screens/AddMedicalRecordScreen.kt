package com.example.healthlog.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicalRecordScreen(
    onBackClick: () -> Unit,
    onSaveClick: (String, String, String, String, Uri?) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    var hospital by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Add Medical Record", 
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (errorMessage != null) {
                AlertDialog(
                    onDismissRequest = onClearError,
                    title = { Text("Error") },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        TextButton(onClick = onClearError) { Text("OK") }
                    }
                )
            }

            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = LavenderLight.copy(alpha = 0.2f),
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = LavenderPrimary,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
            }

            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    PremiumTextField(value = hospital, onValueChange = { hospital = it }, label = "Hospital / Clinic Name")
                    PremiumTextField(value = reason, onValueChange = { reason = it }, label = "Reason for Visit")
                    PremiumTextField(value = diagnosis, onValueChange = { diagnosis = it }, label = "Diagnosis / Findings", minLines = 3)
                    PremiumTextField(value = date, onValueChange = { date = it }, label = "Date (DD/MM/YYYY)")
                }
            }

            // Report Upload Section
            GlassCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Medical Report",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (selectedUri != null) {
                            TextButton(onClick = { selectedUri = null }) {
                                Text("Remove", color = ErrorRed, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (selectedUri == null) {
                        OutlinedButton(
                            onClick = { launcher.launch("*/*") },
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            border = BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.5f))
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = LavenderPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Upload Report (PDF/Image)", color = LavenderPrimary, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            color = LavenderLight.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, LavenderPrimary.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Report Selected",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Ready to save",
                                        fontSize = 12.sp,
                                        color = Gray500
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = LavenderPrimary)
            } else {
                ThreeDButton(
                    text = "Save Medical Record",
                    onClick = {
                        if (hospital.isNotBlank() && reason.isNotBlank()) {
                            onSaveClick(hospital, reason, diagnosis, date, selectedUri)
                        }
                    },
                    enabled = hospital.isNotBlank() && reason.isNotBlank()
                )
            }
        }
    }
}
