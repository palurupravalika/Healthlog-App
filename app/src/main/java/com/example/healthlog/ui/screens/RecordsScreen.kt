package com.example.healthlog.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    profiles: List<Profile>,
    onBackClick: () -> Unit,
    onRecordClick: (String, Int) -> Unit,
    onProfileClick: (String) -> Unit = {},
    onAddRecordClick: () -> Unit = {}
) {
    // Collect all records from all profiles into a descending timeline
    // FIX: Using .orEmpty() to safely handle nullable records list
    val allRecords by remember(profiles) {
        derivedStateOf {
            profiles.flatMap { profile ->
                profile.records.orEmpty().mapIndexed { index, record ->
                    record to (profile.id to profile.name to index)
                }
            }.sortedByDescending { it.first.date }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Health Vault", 
                        fontWeight = FontWeight.ExtraBold, 
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = LavenderPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Quick Add Action
            item {
                Surface(
                    onClick = onAddRecordClick,
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = LavenderPrimary),
                    shape = RoundedCornerShape(20.dp),
                    color = LavenderPrimary
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Add, null, tint = White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Add New Medical Log", color = White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 2. Member Section
            item {
                Text(
                    text = "Browse by Member",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (profiles.isEmpty()) {
                    Surface(
                        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = "No family members found.",
                            fontSize = 14.sp,
                            color = Gray500,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth().height(110.dp)
                    ) {
                        items(profiles) { profile ->
                            MemberMiniCard(
                                name = profile.name,
                                onClick = { onProfileClick(profile.id) }
                            )
                        }
                    }
                }
            }

            // 3. Timeline Section
            item {
                Text(
                    text = "Recent Records",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 12.dp)
                )
            }

            if (allRecords.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.History, null, modifier = Modifier.size(48.dp), tint = LavenderPrimary.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No records found", color = Gray500)
                        }
                    }
                }
            } else {
                items(allRecords) { (record, info) ->
                    val (ids, index) = info
                    val (profileId, profileName) = ids
                    
                    Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                        Surface(
                            onClick = { onRecordClick(profileId, index) },
                            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(PremiumGradient)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Description, null, tint = White, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(record.hospital, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1)
                                    Text("$profileName • ${record.date}", fontSize = 12.sp, color = LavenderPrimary, fontWeight = FontWeight.Medium)
                                }
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Gray500, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemberMiniCard(name: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp).clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier.size(60.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(2.dp, LavenderLight),
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = LavenderPrimary)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
