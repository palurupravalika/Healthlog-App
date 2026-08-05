package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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

@Composable
fun DashboardScreen(
    userName: String,
    profiles: List<Profile>,
    reminders: List<Reminder>,
    onAddProfileClick: () -> Unit,
    onSetReminderClick: () -> Unit,
    onRecordsClick: () -> Unit,
    onViewProfilesClick: () -> Unit,
    onEditProfilesClick: () -> Unit,
    onMedicineRemindersClick: () -> Unit,
    onDoctorAppointmentsClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Brand Header Section with Unique App Logo
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UniqueAppLogo(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = LavenderPrimary)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "HealthLog",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "AI-Powered Family Health",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LavenderPrimary
                        )
                    }
                }
            }

            // Glassmorphic User Greeting with Floating Effect
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            rotationX = -2f
                            translationY = -5f
                        }
                        .shadow(20.dp, RoundedCornerShape(32.dp), spotColor = LavenderPrimary.copy(alpha = 0.5f))
                        .clip(RoundedCornerShape(32.dp))
                        .background(Brush.linearGradient(PremiumGradient))
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Welcome back,",
                                color = White.copy(alpha = 0.85f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = userName,
                                color = White,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-1).sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = White.copy(alpha = 0.25f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Secure Vault Active",
                                        color = White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .shadow(10.dp, CircleShape, spotColor = White)
                                .background(White.copy(alpha = 0.2f), CircleShape)
                                .border(2.dp, White.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diversity3,
                                contentDescription = null, 
                                tint = White, 
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }
                }
            }

            // Quick Actions - 3D Hover Style
            item {
                Column {
                    Text(
                        text = "Quick Actions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Action3DCard(
                            modifier = Modifier.weight(1f),
                            label = "Add Member",
                            icon = Icons.Default.PersonAdd,
                            gradient = PremiumGradient,
                            onClick = onAddProfileClick
                        )
                        Action3DCard(
                            modifier = Modifier.weight(1f),
                            label = "Records",
                            icon = Icons.Default.AutoStories,
                            gradient = BlueGradient,
                            onClick = onRecordsClick
                        )
                        Action3DCard(
                            modifier = Modifier.weight(1f),
                            label = "Edit Profiles",
                            icon = Icons.Default.Tune,
                            gradient = PinkGradient,
                            onClick = onEditProfilesClick
                        )
                    }
                }
            }

            // Stats Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatsCard(
                        modifier = Modifier.weight(1f),
                        count = profiles.size.toString(),
                        label = "Profiles",
                        icon = Icons.Default.Badge,
                        color = LavenderPrimary,
                        onClick = onViewProfilesClick
                    )
                    StatsCard(
                        modifier = Modifier.weight(1f),
                        count = reminders.size.toString(),
                        label = "Upcoming",
                        icon = Icons.Default.NotificationImportant,
                        color = CardBlue,
                        onClick = onSetReminderClick
                    )
                }
            }

            // Schedules - Premium List
            item {
                Column {
                    Text(
                        text = "Your Health Schedule",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PremiumScheduleItem(
                        title = "Medication Alerts",
                        subtitle = "Never miss a dose",
                        icon = Icons.Default.Medication,
                        color = LavenderPrimary,
                        onClick = onMedicineRemindersClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PremiumScheduleItem(
                        title = "Doctor Visits",
                        subtitle = "Track appointments",
                        icon = Icons.Default.EventNote,
                        color = CardBlue,
                        onClick = onDoctorAppointmentsClick
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun Action3DCard(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(120.dp)
            .graphicsLayer {
                rotationY = 5f
            }
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = gradient[0]),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface, gradient[0].copy(alpha = 0.08f)))),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = White, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    count: String,
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(24.dp), spotColor = color.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Column {
                Text(
                    text = count,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray500
                )
            }
        }
    }
}

@Composable
fun PremiumScheduleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(24.dp), spotColor = color.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 13.sp, color = Gray500, fontWeight = FontWeight.Medium)
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight, 
                contentDescription = null, 
                tint = Gray500,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
