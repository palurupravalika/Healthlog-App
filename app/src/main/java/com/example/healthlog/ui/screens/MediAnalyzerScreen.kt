package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediAnalyzerScreen(
    onSummarizeClick: () -> Unit,
    onExplainTermsClick: () -> Unit,
    onMedicinePurposeClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "AI Health Insights", 
                        fontWeight = FontWeight.ExtraBold, 
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp
                    ) 
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = LavenderPrimary)
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main AI Hero Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = LavenderPrimary.copy(alpha = 0.4f))
                    .clip(RoundedCornerShape(32.dp))
                    .background(Brush.linearGradient(PremiumGradient))
                    .padding(28.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Analyze with AI",
                            color = White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Get instant clarity on your medical records and prescriptions.",
                            color = White.copy(alpha = 0.85f),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(White.copy(alpha = 0.2f), CircleShape)
                            .border(1.dp, White.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null, 
                            tint = White, 
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Choose AI Feature",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                PremiumAICard(
                    title = "Summarize Reports",
                    description = "Converts complex medical jargon into easy-to-read summaries.",
                    icon = Icons.Default.Description,
                    gradient = PremiumGradient,
                    onClick = onSummarizeClick
                )
                
                PremiumAICard(
                    title = "Explain Terms",
                    description = "Understand difficult medical vocabulary and acronyms.",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    gradient = BlueGradient,
                    onClick = onExplainTermsClick
                )
                
                PremiumAICard(
                    title = "Medicine Purpose",
                    description = "Identify the common uses and effects of prescribed medications.",
                    icon = Icons.Default.Medication,
                    gradient = PinkGradient,
                    onClick = onMedicinePurposeClick
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun PremiumAICard(
    title: String,
    description: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { rotationY = -3f }
            .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = gradient[0].copy(alpha = 0.3f)),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface, 
                            gradient[0].copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(4.dp, RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Gray500,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Surface(
                shape = CircleShape,
                color = LavenderLight.copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = LavenderPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
