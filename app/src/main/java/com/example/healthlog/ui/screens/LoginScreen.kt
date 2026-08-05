package com.example.healthlog.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: (String, String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showEmptyFieldsDialog by remember { mutableStateOf(false) }
    var showInvalidEmailDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-100).dp)
                .background(LavenderPrimary.copy(alpha = 0.1f), CircleShape)
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(110.dp * glowScale)
                        .background(LavenderPrimary.copy(alpha = 0.15f), CircleShape)
                )
                UniqueAppLogo(
                    modifier = Modifier
                        .size(120.dp)
                        .graphicsLayer { rotationZ = -5f }
                        .shadow(16.dp, CircleShape, spotColor = LavenderPrimary)
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Welcome Back",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    PremiumTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    PremiumTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
                
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onForgotPasswordClick() }
                        .padding(4.dp),
                    color = LavenderPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator(color = LavenderPrimary)
            } else {
                ThreeDButton(
                    text = "Login to HealthLog",
                    onClick = {
                        val trimmedEmail = email.trim()
                        if (trimmedEmail.isBlank() || password.isBlank()) {
                            showEmptyFieldsDialog = true
                        } else if (!trimmedEmail.endsWith("@gmail.com")) {
                            showInvalidEmailDialog = true
                        } else {
                            onLoginClick(trimmedEmail, password)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = buildAnnotatedString {
                    append("New to HealthLog? ")
                    withStyle(style = SpanStyle(color = LavenderPrimary, fontWeight = FontWeight.Bold)) {
                        append("Create an Account")
                    }
                },
                modifier = Modifier.clickable { onNavigateToRegister() },
                fontSize = 16.sp
            )
        }
    }

    if (showEmptyFieldsDialog) {
        PremiumAlertDialog(onDismiss = { showEmptyFieldsDialog = false }, title = "Missing Credentials", text = "Please enter both email and password.")
    }

    if (showInvalidEmailDialog) {
        PremiumAlertDialog(onDismiss = { showInvalidEmailDialog = false }, title = "Invalid Email", text = "Please enter a valid @gmail.com address.")
    }

    if (errorMessage != null) {
        PremiumAlertDialog(onDismiss = onClearError, title = "Login Failed", text = errorMessage, isError = true)
    }
}
