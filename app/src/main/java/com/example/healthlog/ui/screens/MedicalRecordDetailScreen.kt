package com.example.healthlog.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.network.RetrofitClient
import com.example.healthlog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalRecordDetailScreen(
    record: MedicalRecord,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showNoReportDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    // Resolve the report URL: use reportUri as-is if it is already a full https:// Firebase URL.
    // If it points to the local Flask server (127.0.0.1 / 10.0.2.2 / localhost), rewrite the host
    // to the configured RetrofitClient.BASE_URL so the device can reach it on the LAN.
    val fullReportUrl = remember(record.reportUri) {
        val uri = record.reportUri?.trim()
        when {
            uri.isNullOrBlank() ||
            uri.equals("doc", ignoreCase = true) ||
            uri.equals("none", ignoreCase = true) ||
            uri.equals("null", ignoreCase = true) ||
            uri.equals("undefined", ignoreCase = true) -> null
            
            // Firebase Storage and other external HTTPS URLs — use directly
            uri.startsWith("https://") -> uri
            // Local server URL stored by Flask — replace the host with the real server host
            uri.startsWith("http://") -> {
                val serverBase = RetrofitClient.BASE_URL.trimEnd('/')
                uri.replace(Regex("http://(127\\.0\\.0\\.1|10\\.0\\.2\\.2|192\\.168\\.\\d+\\.\\d+|localhost)(:\\d+)?"), serverBase)
            }
            // Relative path — prepend server base URL
            else -> RetrofitClient.BASE_URL.trimEnd('/') + "/" + uri.trimStart('/')
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Record Details", 
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
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDeleteConfirmation = true },
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = ErrorRed
                        )
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(LavenderLight.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = LavenderPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = record.hospital,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-0.5).sp
                    )
                    
                    Surface(
                        color = LavenderLight.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = record.date,
                            fontSize = 12.sp,
                            color = LavenderPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            GlassCard {
                DetailSection(label = "Reason for Visit", content = record.reason)
            }

            GlassCard {
                Text(
                    text = "Medical Diagnosis",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LavenderPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = record.diagnosis.ifBlank { "No specific diagnosis details provided." },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ThreeDButton(
                text = "View Digital Report",
                onClick = { 
                    if (fullReportUrl == null) {
                        showNoReportDialog = true
                    } else {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(fullReportUrl)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No app found to open this file", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showNoReportDialog) {
        AlertDialog(
            onDismissRequest = { showNoReportDialog = false },
            title = { Text("Document Missing", fontWeight = FontWeight.Bold) },
            text = { Text("There are no scanned reports or digital documents attached to this health record.") },
            confirmButton = {
                TextButton(onClick = { showNoReportDialog = false }) {
                    Text("Understood", color = LavenderPrimary, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(28.dp)
        )
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Record", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to remove this medical log? This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmation = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold, color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel", color = Gray500)
                }
            },
            shape = RoundedCornerShape(28.dp)
        )
    }
}

@Composable
fun DetailSection(label: String, content: String) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = LavenderPrimary
        )
        Text(
            text = content,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
