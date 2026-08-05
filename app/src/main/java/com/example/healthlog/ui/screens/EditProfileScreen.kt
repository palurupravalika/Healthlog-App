package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profile: Profile,
    onBackClick: () -> Unit,
    onSaveClick: (String, String, String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var ageOrDob by remember { mutableStateOf(profile.ageOrDob) }
    var gender by remember { mutableStateOf(profile.gender) }
    var bloodGroup by remember { mutableStateOf(profile.bloodGroup) }
    var relationship by remember { mutableStateOf(profile.relationship) }
    var height by remember { mutableStateOf(profile.height) }
    var weight by remember { mutableStateOf(profile.weight) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Edit Profile", 
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
            // Profile Photo - Camera icon removed
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = LavenderLight.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(4.dp, MaterialTheme.colorScheme.surface),
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = LavenderPrimary,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }

            // Information Form
            GlassCard {
                Text(
                    text = "Personal Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    PremiumTextField(value = name, onValueChange = { name = it }, label = "Full Name")
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        PremiumTextField(value = ageOrDob, onValueChange = { ageOrDob = it }, label = "Age / DOB", modifier = Modifier.weight(1f))
                        PremiumTextField(value = gender, onValueChange = { gender = it }, label = "Gender", modifier = Modifier.weight(1f))
                    }
                    
                    // Moved to separate rows for better alignment and label visibility
                    PremiumTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, label = "Blood Group")
                    PremiumTextField(value = relationship, onValueChange = { relationship = it }, label = "Relationship")
                }
            }

            GlassCard {
                Text(
                    text = "Body Metrics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Removed (cm) from label
                    PremiumTextField(value = height, onValueChange = { height = it }, label = "Height", modifier = Modifier.weight(1f))
                    PremiumTextField(value = weight, onValueChange = { weight = it }, label = "Weight (kg)", modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ThreeDButton(
                text = "Save Changes",
                onClick = {
                    if (name.isNotBlank()) {
                        onSaveClick(profile.name, name, ageOrDob, gender, bloodGroup, relationship, height, weight)
                    }
                },
                enabled = name.isNotBlank()
            )
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
