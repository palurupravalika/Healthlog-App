package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesListScreen(
    profiles: List<Profile>,
    isEditMode: Boolean,
    onBackClick: () -> Unit,
    onProfileClick: (String) -> Unit,
    onEditProfile: (Profile) -> Unit,
    onDeleteProfile: (String) -> Unit
) {
    var profileToEdit by remember { mutableStateOf<Profile?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        if (isEditMode) "Manage Family" else "Family Profiles",
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
        ) {
            if (profiles.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(140.dp),
                            shape = CircleShape,
                            color = LavenderLight.copy(alpha = 0.2f),
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Diversity3,
                                    contentDescription = null,
                                    modifier = Modifier.size(70.dp),
                                    tint = LavenderPrimary.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Your family list is empty",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Add profiles to start tracking health records.",
                            fontSize = 14.sp,
                            color = Gray500,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(profiles) { profile ->
                        Surface(
                            onClick = { 
                                if (!isEditMode) onProfileClick(profile.id)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 6.dp,
                                    shape = RoundedCornerShape(28.dp),
                                    spotColor = LavenderPrimary.copy(alpha = 0.1f)
                                ),
                            shape = RoundedCornerShape(28.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.surface, 
                                                LavenderLight.copy(alpha = 0.1f)
                                            )
                                        )
                                    )
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .shadow(4.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(LavenderLight.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person, 
                                        contentDescription = null, 
                                        tint = LavenderPrimary,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(20.dp))
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = profile.name, 
                                        fontWeight = FontWeight.ExtraBold, 
                                        fontSize = 19.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = profile.relationship, 
                                            fontSize = 13.sp,
                                            color = LavenderPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = " • ${profile.bloodGroup}", 
                                            fontSize = 13.sp,
                                            color = Gray500,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                
                                if (isEditMode) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        IconButton(
                                            onClick = { profileToEdit = profile },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(LavenderLight.copy(alpha = 0.2f), CircleShape)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = LavenderPrimary, modifier = Modifier.size(18.dp))
                                        }
                                        IconButton(
                                            onClick = { onDeleteProfile(profile.id) },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(ErrorRed.copy(alpha = 0.1f), CircleShape)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (profileToEdit != null) {
        EditProfileDialog(
            profile = profileToEdit!!,
            onDismiss = { profileToEdit = null },
            onSave = { updatedProfile ->
                onEditProfile(updatedProfile)
                profileToEdit = null
            }
        )
    }
}

@Composable
fun EditProfileDialog(
    profile: Profile,
    onDismiss: () -> Unit,
    onSave: (Profile) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var ageOrDob by remember { mutableStateOf(profile.ageOrDob) }
    var gender by remember { mutableStateOf(profile.gender) }
    var bloodGroup by remember { mutableStateOf(profile.bloodGroup) }
    var relationship by remember { mutableStateOf(profile.relationship) }
    var height by remember { mutableStateOf(profile.height) }
    var weight by remember { mutableStateOf(profile.weight) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Member Info", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumTextField(value = name, onValueChange = { name = it }, label = "Full Name")
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PremiumTextField(value = ageOrDob, onValueChange = { ageOrDob = it }, label = "Age / DOB", modifier = Modifier.weight(1f))
                    PremiumTextField(value = gender, onValueChange = { gender = it }, label = "Gender", modifier = Modifier.weight(1f))
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PremiumTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, label = "Blood Group", modifier = Modifier.weight(1f))
                    PremiumTextField(value = relationship, onValueChange = { relationship = it }, label = "Relationship", modifier = Modifier.weight(1f))
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PremiumTextField(value = height, onValueChange = { height = it }, label = "Height", modifier = Modifier.weight(1f))
                    PremiumTextField(value = weight, onValueChange = { weight = it }, label = "Weight", modifier = Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(profile.copy(
                        name = name,
                        ageOrDob = ageOrDob,
                        gender = gender,
                        bloodGroup = bloodGroup,
                        relationship = relationship,
                        height = height,
                        weight = weight
                    ))
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LavenderPrimary),
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp)
            ) {
                Text("Update Profile", fontWeight = FontWeight.Bold, color = White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.padding(bottom = 8.dp)) {
                Text("Cancel", color = Gray500, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(32.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
