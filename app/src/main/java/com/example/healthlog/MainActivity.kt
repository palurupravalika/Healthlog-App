package com.example.healthlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.healthlog.ui.screens.*
import com.example.healthlog.ui.theme.*
import com.example.healthlog.ui.components.SuccessPopup
import kotlinx.coroutines.delay

sealed class Screen(val route: String, val label: String, val icon: String) {
    object Home : Screen("profiles", "Home", "🏠")
    object AI : Screen("ai", "AI", "🤖")
    object Reminders : Screen("reminders", "Reminders", "⏰")
    object Account : Screen("profile", "Account", "👤")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val healthViewModel: HealthLogViewModel = viewModel()
            HealthLogTheme(darkTheme = healthViewModel.isDarkMode) {
                MainContent(healthViewModel)
            }
        }
    }
}

@Composable
fun MainContent(healthViewModel: HealthLogViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showSuccessPopup by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(showSuccessPopup) {
        if (showSuccessPopup) {
            delay(3000)
            showSuccessPopup = false
        }
    }

    val mainRoutes = listOf("profiles", "ai", "reminders", "profile")
    val showBottomBar = currentDestination?.route in mainRoutes

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        tonalElevation = 8.dp,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .clip(RoundedCornerShape(32.dp))
                    ) {
                        val items = listOf(Screen.Home, Screen.AI, Screen.Reminders, Screen.Account)
                        items.forEach { screen ->
                            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                            NavigationBarItem(
                                icon = { 
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(if (selected) LavenderLight.copy(alpha = 0.2f) else Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = screen.icon, 
                                            fontSize = if (selected) 26.sp else 22.sp,
                                            modifier = if (selected) Modifier.offset(y = (-1).dp) else Modifier
                                        )
                                    }
                                },
                                label = { 
                                    Text(
                                        text = screen.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (selected) LavenderPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    ) 
                                },
                                selected = selected,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("splash") {
                    SplashScreen(onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    })
                }
                composable("login") {
                    LoginScreen(
                        onNavigateToRegister = { navController.navigate("register") },
                        onForgotPasswordClick = { navController.navigate("resetPassword") },
                        onLoginClick = { email, password ->
                            healthViewModel.loginUser(email, password) {
                                successMessage = "Welcome Back!"
                                showSuccessPopup = true
                                navController.navigate("profiles") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        isLoading = healthViewModel.isLoading,
                        errorMessage = healthViewModel.apiErrorMessage,
                        onClearError = { healthViewModel.clearError() }
                    )
                }
                composable("resetPassword") {
                    ResetPasswordScreen(
                        onBackToLogin = { navController.popBackStack() },
                        onResetClick = { email, phone, newPass ->
                            healthViewModel.resetPassword(email, phone, newPass) {
                                successMessage = "Password Updated!"
                                showSuccessPopup = true
                                navController.navigate("login") {
                                    popUpTo("resetPassword") { inclusive = true }
                                }
                            }
                        },
                        isLoading = healthViewModel.isLoading,
                        errorMessage = healthViewModel.apiErrorMessage,
                        onClearError = { healthViewModel.clearError() }
                    )
                }
                composable("register") {
                    RegisterScreen(
                        onNavigateToLogin = {
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onRegisterClick = { name, email, password ->
                            healthViewModel.registerUser(name, email, password) {
                                successMessage = "Account created!"
                                showSuccessPopup = true
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        },
                        isLoading = healthViewModel.isLoading,
                        errorMessage = healthViewModel.apiErrorMessage,
                        onClearError = { healthViewModel.clearError() }
                    )
                }
                composable("profiles") { 
                    DashboardScreen(
                        userName = healthViewModel.userName,
                        profiles = healthViewModel.profiles,
                        reminders = healthViewModel.reminders,
                        onAddProfileClick = { navController.navigate("addProfile") },
                        onSetReminderClick = { navController.navigate("reminders") },
                        onRecordsClick = { navController.navigate("records") },
                        onViewProfilesClick = { navController.navigate("viewProfiles") },
                        onEditProfilesClick = { navController.navigate("editProfiles") },
                        onMedicineRemindersClick = { navController.navigate("reminders/medicine") },
                        onDoctorAppointmentsClick = { navController.navigate("reminders/appointments") }
                    )
                }
                composable("addProfile") {
                    AddProfileScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { n, a, g, b, r, h, w ->
                            healthViewModel.addProfile(n, a, g, b, r, h, w) {
                                successMessage = "Profile Added!"
                                showSuccessPopup = true
                                navController.popBackStack()
                            }
                        }
                    )
                }
                composable("viewProfiles") {
                    ProfilesListScreen(
                        profiles = healthViewModel.profiles,
                        isEditMode = false,
                        onBackClick = { navController.popBackStack() },
                        onProfileClick = { id -> navController.navigate("profileDetail/$id") },
                        onEditProfile = { _ -> },
                        onDeleteProfile = { id -> 
                            healthViewModel.deleteProfile(id) {
                                successMessage = "Profile Deleted!"
                                showSuccessPopup = true
                            }
                        }
                    )
                }
                composable("editProfiles") {
                    ProfilesListScreen(
                        profiles = healthViewModel.profiles,
                        isEditMode = true,
                        onBackClick = { navController.popBackStack() },
                        onProfileClick = { _ -> },
                        onEditProfile = { profile ->
                            healthViewModel.updateProfile(profile) {
                                successMessage = "Profile updated successfully"
                                showSuccessPopup = true
                            }
                        },
                        onDeleteProfile = { id ->
                            healthViewModel.deleteProfile(id) {
                                successMessage = "Profile Deleted!"
                                showSuccessPopup = true
                            }
                        }
                    )
                }
                composable("profileDetail/{profileId}") { backStackEntry ->
                    val profileId = backStackEntry.arguments?.getString("profileId")
                    val profile = healthViewModel.profiles.find { it.id == profileId }
                    
                    if (profile != null) {
                        ProfileDetailScreen(
                            profile = profile,
                            onBackClick = { navController.popBackStack() },
                            onAddRecordClick = { navController.navigate("addMedicalRecord/${profile.id}") },
                            onRecordClick = { index -> 
                                navController.navigate("medicalRecordDetail/${profile.id}/$index") 
                            },
                            onUpdateRecord = { _, _, _ -> },
                            onDeleteRecord = { _ -> }
                        )
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = LavenderPrimary)
                        }
                    }
                }
                
                composable("medicalRecordDetail/{profileId}/{recordIndex}") { backStackEntry ->
                    val profileId = backStackEntry.arguments?.getString("profileId")
                    val recordIndex = backStackEntry.arguments?.getString("recordIndex")?.toIntOrNull() ?: 0
                    val profile = healthViewModel.profiles.find { it.id == profileId }
                    val record = profile?.records?.getOrNull(recordIndex)
                    
                    if (record != null) {
                        MedicalRecordDetailScreen(
                            record = record,
                            onBackClick = { navController.popBackStack() },
                            onDeleteClick = { }
                        )
                    }
                }

                composable("addMedicalRecord/{profileId}") { backStackEntry ->
                    val profileId = backStackEntry.arguments?.getString("profileId") ?: ""
                    AddMedicalRecordScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { hospital, reason, diagnosis, date, uri ->
                            healthViewModel.addMedicalRecord(
                                context = context,
                                profileId = profileId,
                                hospital = hospital,
                                reason = reason,
                                diagnosis = diagnosis,
                                date = date,
                                uri = uri,
                                onSuccess = {
                                    successMessage = "Record Saved!"
                                    showSuccessPopup = true
                                    navController.popBackStack()
                                }
                            )
                        },
                        isLoading = healthViewModel.isLoading,
                        errorMessage = healthViewModel.apiErrorMessage,
                        onClearError = { healthViewModel.clearError() }
                    )
                }

                composable("ai") {
                    MediAnalyzerScreen(
                        onSummarizeClick = { navController.navigate("ai/summarize") },
                        onExplainTermsClick = { navController.navigate("ai/explainTerms") },
                        onMedicinePurposeClick = { navController.navigate("ai/medicinePurpose") }
                    )
                }
                composable("ai/summarize") { 
                    ReportSummaryScreen(
                        viewModel = healthViewModel,
                        onBackClick = { navController.popBackStack() } 
                    ) 
                }
                composable("ai/explainTerms") { 
                    ExplainTermsScreen(
                        viewModel = healthViewModel,
                        onBackClick = { navController.popBackStack() } 
                    ) 
                }
                composable("ai/medicinePurpose") { 
                    MedicinePurposeScreen(
                        viewModel = healthViewModel,
                        onBackClick = { navController.popBackStack() } 
                    ) 
                }

                composable("profile") { 
                    ProfileScreen(
                        user = healthViewModel.currentUser,
                        isDarkMode = healthViewModel.isDarkMode,
                        onThemeToggle = { healthViewModel.toggleTheme() },
                        onUpdateUser = { gender, phone, blood, age ->
                            healthViewModel.updateUserInfo(gender, phone, blood, age) {
                                successMessage = "Info Updated!"
                                showSuccessPopup = true
                            }
                        },
                        onFamilyManagementClick = { navController.navigate("viewProfiles") },
                        onRemindersClick = { navController.navigate("reminders") },
                        onDeleteAccount = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            }
                        },
                        onLogout = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            }
                        }
                    ) 
                }
                
                composable("reminders") {
                    RemindersScreen(
                        reminders = healthViewModel.reminders,
                        onBackClick = { navController.popBackStack() },
                        onAddReminder = { healthViewModel.addReminder(it) },
                        onDeleteReminder = { healthViewModel.deleteReminder(it) }
                    )
                }
                
                composable("reminders/medicine") {
                    RemindersScreen(
                        reminders = healthViewModel.reminders,
                        filterType = ReminderType.Medicine,
                        onBackClick = { navController.popBackStack() },
                        onAddReminder = { healthViewModel.addReminder(it) },
                        onDeleteReminder = { healthViewModel.deleteReminder(it) }
                    )
                }
                
                composable("reminders/appointments") {
                    RemindersScreen(
                        reminders = healthViewModel.reminders,
                        filterType = ReminderType.DoctorAppointment,
                        onBackClick = { navController.popBackStack() },
                        onAddReminder = { healthViewModel.addReminder(it) },
                        onDeleteReminder = { healthViewModel.deleteReminder(it) }
                    )
                }

                composable("records") {
                    RecordsScreen(
                        profiles = healthViewModel.profiles,
                        onBackClick = { navController.popBackStack() },
                        onRecordClick = { profileId, index -> 
                            navController.navigate("medicalRecordDetail/$profileId/$index")
                        },
                        onProfileClick = { profileId -> 
                            navController.navigate("profileDetail/$profileId")
                        },
                        onAddRecordClick = {
                            navController.navigate("viewProfiles")
                        }
                    )
                }
            }
        }
        SuccessPopup(message = successMessage, isVisible = showSuccessPopup)
    }
}
