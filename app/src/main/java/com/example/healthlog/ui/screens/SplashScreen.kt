package com.example.healthlog.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    
    LaunchedEffect(key1 = true) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
            )
        }
        delay(2500)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(PremiumGradient)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale.value)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Background Glow
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                        .graphicsLayer { 
                            scaleX = 1.3f
                            scaleY = 1.3f 
                        }
                )
                
                // Unique App Logo with 3D rotation effect
                UniqueAppLogo(
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer { 
                            rotationY = rotation.value 
                            cameraDistance = 15f * density
                        }
                )
                
                // Floating Sparkle Icon
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(34.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 5.dp, y = (-5).dp)
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "HealthLog",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1.5).sp
            )
            
            Text(
                text = "AI FAMILY HEALTH VAULT",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            
            // Minimalist Loading Indicator
            Box(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .width(45.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.4f))
            )
        }
    }
}
