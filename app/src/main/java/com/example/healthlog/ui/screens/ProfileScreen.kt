package com.example.healthlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlog.User
import com.example.healthlog.ui.theme.*

@Composable
fun ProfileScreen(
    user: User?,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onUpdateUser: (String, String, String, String) -> Unit,
    onFamilyManagementClick: () -> Unit,
    onRemindersClick: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLogout: () -> Unit
) {
    // Dialog States
    var showAppInfo by remember { mutableStateOf(false) }
    var showHowToUse by remember { mutableStateOf(false) }
    var showTerms by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }
    var showQA by remember { mutableStateOf(false) }
    var showPersonalInfo by remember { mutableStateOf(false) }

    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FAFF)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color.LightGray else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor) 
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // App Logo & App Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = LavenderPrimary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.HealthAndSafety, 
                        contentDescription = null, 
                        tint = Color.White, 
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "HealthLog",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
        }

        // Profile Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = cardColor,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(65.dp),
                    shape = CircleShape,
                    color = LavenderLight.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, LavenderPrimary)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = LavenderPrimary,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = user?.name ?: "User Name",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = user?.email ?: "email@example.com",
                        fontSize = 14.sp,
                        color = secondaryTextColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ACCOUNT SECTION
        SettingsSectionTitle("ACCOUNT")
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = cardColor,
            shadowElevation = 1.dp
        ) {
            Column {
                SettingsItem(Icons.Default.Badge, "Personal Information", textColor, onClick = { showPersonalInfo = true })
                ItemDivider()
                SettingsItem(Icons.Default.Groups, "Family Management", textColor, onClick = onFamilyManagementClick)
                ItemDivider()
                SettingsItem(Icons.Default.NotificationsActive, "Manage remainder & alerts", textColor, onClick = onRemindersClick)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // GENERAL SECTION
        SettingsSectionTitle("GENERAL")
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = cardColor,
            shadowElevation = 1.dp
        ) {
            Column {
                SettingsItem(Icons.Default.Info, "App Info", textColor, onClick = { showAppInfo = true })
                ItemDivider()
                
                // Theme Toggle Row
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onThemeToggle() }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode, 
                            contentDescription = null, 
                            tint = LavenderPrimary, 
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Theme", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = textColor)
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onThemeToggle() },
                        colors = SwitchDefaults.colors(checkedThumbColor = LavenderPrimary)
                    )
                }
                
                ItemDivider()
                SettingsItem(Icons.Default.DeleteForever, "Delete Account", Color.Red, onClick = onDeleteAccount)
                ItemDivider()
                SettingsItem(Icons.AutoMirrored.Filled.Logout, "Log Out", LavenderPrimary, onClick = onLogout)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // HELP & SUPPORT SECTION
        SettingsSectionTitle("HELP & SUPPORT")
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = cardColor,
            shadowElevation = 1.dp
        ) {
            Column {
                SettingsItem(Icons.AutoMirrored.Filled.MenuBook, "How to use App", textColor, onClick = { showHowToUse = true })
                ItemDivider()
                SettingsItem(Icons.Default.Gavel, "Terms and Conditions", textColor, onClick = { showTerms = true })
                ItemDivider()
                SettingsItem(Icons.Default.PrivacyTip, "Privacy Policy", textColor, onClick = { showPrivacy = true })
                ItemDivider()
                SettingsItem(Icons.Default.QuestionAnswer, "Q&A", textColor, onClick = { showQA = true })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ABOUT SECTION
        SettingsSectionTitle("ABOUT")
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = cardColor,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                AboutInfoRow("APP VERSION", "1.0.0", textColor)
                AboutInfoRow("FRAMEWORK", "Jetpack Compose", textColor)
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }

    // --- DIALOGS WITH EXTENSIVE CONTENT ---

    if (showAppInfo) {
        InfoDialog(
            title = "About HealthLog",
            content = """
                HealthLog: Your Smart Medical History Vault
                
                HealthLog is a sophisticated, centralized digital ecosystem designed to transform how individuals and families manage their medical records. Our mission is to move medical history from fragmented paper folders into a secure, intelligent, and highly accessible digital vault.
                
                The Core Vision:
                In modern healthcare, patients often see multiple providers across different systems. HealthLog acts as the universal bridge, allowing you to carry your entire medical history in your pocket. By organizing every clinic visit, lab report, and prescription, we empower users to provide accurate history to doctors, leading to better-informed clinical decisions.
                
                AI-Driven Intelligence:
                HealthLog goes beyond simple storage. Our advanced AI Health Assistant is built to bridge the gap between technical medical terminology and patient understanding. By scanning your uploaded documents, the AI can:
                • Summarize long, complex medical reports.
                • Explain technical jargon in simple, everyday language.
                • Clarify the specific purpose of prescribed medications.
                
                Proactive Family Care:
                Whether you are managing your own health, your children's vaccinations, or your elderly parents' chronic conditions, HealthLog's family-centric design ensures that every family member has a dedicated, organized medical timeline.
            """.trimIndent(),
            onDismiss = { showAppInfo = false }
        )
    }

    if (showHowToUse) {
        InfoDialog(
            title = "Complete User Guide",
            content = """
                Getting the Most Out of HealthLog:
                
                1. CREATE YOUR VAULT:
                Secure your data by creating an account. Your primary login acts as the master key to your family's health history.
                
                2. MANAGE FAMILY PROFILES:
                Navigate to 'Family Management' to add separate profiles for your children, spouse, or parents. This keeps everyone's records distinct and prevents medical confusion.
                
                3. LOGGING A VISIT:
                Select a profile and tap 'Add Record'. 
                • Enter the hospital or clinic name.
                • State the reason for the visit clearly (e.g., 'Chronic Fatigue' or 'Annual Checkup').
                • Input the doctor's final diagnosis or findings.
                • Set the visit date to maintain a chronological medical timeline.
                
                4. DIGITIZING REPORTS:
                Physical reports are easily lost. Use the 'Upload' feature within any medical record to attach high-quality photos of prescriptions or scan multi-page PDF lab results. Once saved, these documents are permanently linked to that medical event.
                
                5. INTERACTING WITH AI:
                Visit the 'AI' tab to intelligently analyze your uploaded records. 
                • 'Summarize': Get a high-level overview of complex hospital discharge papers.
                • 'Explain Terms': Instantly understand technical medical words without complex web searches.
                • 'Medicine Purpose': Learn why a particular drug was prescribed and how it helps.
                
                6. SMART REMINDERS:
                Never miss an appointment or a dose again. Use the 'Reminders' tab to schedule proactive alerts for doctor visits and daily medication intake times.
            """.trimIndent(),
            onDismiss = { showHowToUse = false }
        )
    }

    if (showTerms) {
        InfoDialog(
            title = "Terms and Conditions",
            content = """
                Welcome to HealthLog. By utilizing this platform, you agree to the following:
                
                1. MEDICAL DISCLAIMER:
                The AI Health Assistant provides interpretations and summaries for INFORMATIONAL AND EDUCATIONAL PURPOSES ONLY. HealthLog is NOT a licensed medical professional. The insights generated by our AI do not constitute professional medical advice, diagnosis, or official treatment plans.
                
                2. PROFESSIONAL CONSULTATION REQUIRED:
                Always seek the advice of your physician or other qualified health provider with any questions you may have regarding a medical condition. NEVER disregard professional medical advice or delay in seeking it because of something you have read or interpreted within the HealthLog app.
                
                3. NOT FOR EMERGENCIES:
                HealthLog is an information management tool, NOT an emergency response system. In the event of a medical emergency, immediately contact your local emergency services (e.g., 911 or 112).
                
                4. USER RESPONSIBILITY:
                You are solely responsible for the accuracy and completeness of the data you enter. The effectiveness of the vault and AI interpretation depends entirely on the quality of your inputs.
                
                5. SECURITY OF LOGIN:
                You must maintain the confidentiality of your credentials. Any activity that occurs under your account is your responsibility.
                
                6. UPDATES TO SERVICE:
                We reserve the right to modify or discontinue features to improve our technology and provide a better experience for our community.
            """.trimIndent(),
            onDismiss = { showTerms = false }
        )
    }

    if (showPrivacy) {
        InfoDialog(
            title = "Privacy and Security",
            content = """
                At HealthLog, your medical privacy is our highest priority. We treat your family's data with the same respect as our own.
                
                1. ADVANCED ENCRYPTION:
                Every medical record and personal detail is encrypted during transmission using SSL/TLS protocols. Furthermore, your documents are stored in an encrypted state (AES-256) at rest on our secure servers, protecting them from unauthorized access.
                
                2. DATA SOVEREIGNTY:
                You own your medical records. HealthLog strictly adheres to a policy of NEVER selling, sharing, or trading your identifiable health data with insurance companies, pharmaceutical corporations, or third-party advertisers.
                
                3. SECURE AI PROCESSING:
                When you use our AI assistant to summarize a report, the data is processed through secure, private channels. This document text is used only to generate the specific interpretation you requested and is not stored permanently or used to train public AI models.
                
                4. COMPLETE USER CONTROL:
                You maintain total control over your health history. You can update, edit, or delete any record or profile at any time. 
                
                5. ACCOUNT DELETION RIGHTS:
                If you choose to delete your account, all your family profiles, medical history, and uploaded images are permanently purged from our systems immediately.
                
                6. HIPAA ALIGNMENT:
                We strive to align our data handling practices with the highest global standards for health data privacy.
            """.trimIndent(),
            onDismiss = { showPrivacy = false }
        )
    }

    if (showQA) {
        InfoDialog(
            title = "Frequently Asked Questions",
            content = """
                Q: Is my data safe if I lose my mobile phone?
                A: Yes. Your records are not stored solely on your phone's memory. They are synced to your secure HealthLog account in the cloud. Simply install the app on your new device and log in to restore all your records.
                
                Q: Can I manage my parents and children in one account?
                A: Absolutely. This is a core feature of HealthLog. You can add unlimited family profiles under one primary account, keeping everyone's medical timeline organized in one central vault.
                
                Q: What types of files can I upload?
                A: We support high-resolution JPG/PNG image formats for photos of prescriptions and PDF documents for official lab results and clinical summaries.
                
                Q: Does the AI assistant detect errors in my report?
                A: No. The AI is designed to interpret and explain what is written in the reports you provide. It is an explanation tool, not a clinical validation or second-opinion service.
                
                Q: How do I share a report with a new doctor?
                A: Open the digitized report in the app, and use your phone's native 'Share' button to send it via secure email or messaging directly to your healthcare provider.
                
                Q: Can I use HealthLog without an internet connection?
                A: You can view previously saved records offline. However, adding new records, syncing data, and using AI interpretation features require an active internet connection.
                
                Q: How do I delete a family profile?
                A: Navigate to 'Family Management', select the profile you wish to remove, and click the 'Delete' icon. Note that this will also remove all medical logs for that person.
            """.trimIndent(),
            onDismiss = { showQA = false }
        )
    }

    if (showPersonalInfo) {
        var gender by remember { mutableStateOf(user?.gender ?: "") }
        var phone by remember { mutableStateOf(user?.phone ?: "") }
        var bloodGroup by remember { mutableStateOf(user?.bloodGroup ?: "") }
        var age by remember { mutableStateOf(user?.age ?: "") }

        AlertDialog(
            onDismissRequest = { showPersonalInfo = false },
            title = { Text("Update Personal Info", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") })
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
                    OutlinedTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, label = { Text("Blood Group") })
                    OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    onUpdateUser(gender, phone, bloodGroup, age)
                    showPersonalInfo = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showPersonalInfo = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun InfoDialog(title: String, content: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
        text = { 
            Box(modifier = Modifier.heightIn(max = 480.dp)) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = content, 
                        lineHeight = 22.sp, 
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = LavenderPrimary, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun ItemDivider() {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    label: String,
    labelColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = LavenderPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = labelColor
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun AboutInfoRow(label: String, value: String, textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(value, color = textColor, fontWeight = FontWeight.Medium)
    }
}
