package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterClick: (String, String, String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var showEmptyFieldsDialog by remember { mutableStateOf(false) }
    var showPasswordMismatchDialog by remember { mutableStateOf(false) }
    var showInvalidEmailDialog by remember { mutableStateOf(false) }
    var showWeakPasswordDialog by remember { mutableStateOf(false) }

    fun isStrongPassword(p: String): Boolean {
        return p.length >= 8 &&
                p.any { it.isUpperCase() } &&
                p.any { it.isLowerCase() } &&
                p.any { it.isDigit() } &&
                p.any { !it.isLetterOrDigit() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .size(350.dp)
                .offset(x = 150.dp, y = (-150).dp)
                .background(LavenderPrimary.copy(alpha = 0.08f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                UniqueAppLogo(
                    modifier = Modifier
                        .size(110.dp)
                        .graphicsLayer { rotationZ = 5f }
                        .shadow(12.dp, CircleShape, spotColor = LavenderPrimary)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Create Account",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(40.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    PremiumTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name"
                    )

                    PremiumTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address (@gmail.com)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    PremiumTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Create Password",
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    PremiumTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm Password",
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator(color = LavenderPrimary)
            } else {
                ThreeDButton(
                    text = "Register Now",
                    onClick = { 
                        val trimmedEmail = email.trim()
                        if (name.isBlank() || trimmedEmail.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            showEmptyFieldsDialog = true
                        } else if (!trimmedEmail.endsWith("@gmail.com")) {
                            showInvalidEmailDialog = true
                        } else if (!isStrongPassword(password)) {
                            showWeakPasswordDialog = true
                        } else if (password != confirmPassword) {
                            showPasswordMismatchDialog = true
                        } else {
                            onRegisterClick(name, trimmedEmail, password)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = buildAnnotatedString {
                    append("Already a member? ")
                    withStyle(style = SpanStyle(color = LavenderPrimary, fontWeight = FontWeight.Bold)) {
                        append("Login here")
                    }
                },
                modifier = Modifier.clickable { onNavigateToLogin() },
                fontSize = 16.sp
            )
        }
    }

    if (showEmptyFieldsDialog) {
        PremiumAlertDialog(onDismiss = { showEmptyFieldsDialog = false }, title = "Incomplete Form", text = "Please fill in all details.")
    }

    if (showInvalidEmailDialog) {
        PremiumAlertDialog(onDismiss = { showInvalidEmailDialog = false }, title = "Invalid Email", text = "A valid @gmail.com address is required.")
    }

    if (showWeakPasswordDialog) {
        PremiumAlertDialog(onDismiss = { showWeakPasswordDialog = false }, title = "Weak Password", text = "Password must be 8+ characters with Uppercase, Lowercase, Number, and Symbol.")
    }

    if (showPasswordMismatchDialog) {
        PremiumAlertDialog(onDismiss = { showPasswordMismatchDialog = false }, title = "Passwords Mismatch", text = "The passwords do not match.")
    }

    if (errorMessage != null) {
        PremiumAlertDialog(onDismiss = onClearError, title = "Registration Failed", text = errorMessage, isError = true)
    }
}
