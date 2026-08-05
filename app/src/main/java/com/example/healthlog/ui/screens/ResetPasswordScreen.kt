package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*

@Composable
fun ResetPasswordScreen(
    onBackToLogin: () -> Unit,
    onResetClick: (String, String, String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Key Icon (Matches the blue key icon in the user's image)
        Surface(
            modifier = Modifier.size(60.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1976D2) // Professional blue color like the image
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.VpnKey,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Reset Password",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1976D2)
        )

        Text(
            text = "Verify details to set a new password",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Email Address", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
            PremiumTextField(
                value = email,
                onValueChange = { email = it },
                label = "name@example.com",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Text("Phone Number", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
            PremiumTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Registered phone number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Text("New Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
            PremiumTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "********",
                isPassword = true
            )

            Text("Confirm New Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
            PremiumTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "********",
                isPassword = true
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(color = LavenderPrimary)
        } else {
            Button(
                onClick = {
                    if (newPassword == confirmPassword) {
                        onResetClick(email, phone, newPassword)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                enabled = email.isNotBlank() && phone.isNotBlank() && newPassword.isNotBlank() && newPassword == confirmPassword
            ) {
                Text("Reset Password", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Remembered your password? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "Sign In",
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clip(RoundedCornerShape(4.dp)).clickable { onBackToLogin() }.padding(4.dp)
            )
        }
    }
}
