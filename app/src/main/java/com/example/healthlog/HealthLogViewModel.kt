package com.example.healthlog

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthlog.network.*
import com.example.healthlog.ui.screens.Profile
import com.example.healthlog.ui.screens.Reminder
import com.example.healthlog.ui.screens.MedicalRecord
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class HealthLogViewModel : ViewModel() {
    // --- UI State ---
    var isDarkMode by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var apiErrorMessage by mutableStateOf<String?>(null)
        private set
    var userName by mutableStateOf("User")
        private set
    var currentUser by mutableStateOf<User?>(null)
        private set

    // --- AI State ---
    var aiSummary by mutableStateOf<String?>(null)
        private set
    var isAiLoading by mutableStateOf(false)
        private set

    var useMockAi by mutableStateOf(false) 

    
    private val _profiles = mutableStateListOf<Profile>()
    val profiles: List<Profile> get() = _profiles
    private val _reminders = mutableStateListOf<Reminder>()
    val reminders: List<Reminder> get() = _reminders

    fun toggleTheme() { isDarkMode = !isDarkMode }
    fun clearError() { apiErrorMessage = null }
    fun clearAiSummary() { aiSummary = null }

    fun loginUser(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.loginUser(email.trim(), password)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        body.access_token?.let { RetrofitClient.authToken = it }
                        val user = body.user
                        currentUser = user
                        userName = user?.name ?: "User"
                        user?.id?.let { fetchInitialData(it) }
                        onSuccess()
                    } else {
                        apiErrorMessage = body?.message ?: "Login failed"
                    }
                } else {
                    val errorBodyStr = response.errorBody()?.string()
                    val message = try {
                        if (!errorBodyStr.isNullOrBlank()) {
                            org.json.JSONObject(errorBodyStr).optString("message", "Invalid email or password")
                        } else "Invalid email or password"
                    } catch (e: Exception) {
                        "Invalid email or password (${response.code()})"
                    }
                    apiErrorMessage = message
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun registerUser(name: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.registerUser(name, email, password)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        onSuccess()
                    } else {
                        apiErrorMessage = body?.message ?: "Registration failed"
                    }
                } else {
                    apiErrorMessage = "Server Error: ${response.code()}"
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun updateUserInfo(gender: String, phone: String, bloodGroup: String, age: String, onSuccess: () -> Unit) {
        val userId = currentUser?.id ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.updateUserInfo(userId, gender, phone, bloodGroup, age)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        currentUser = currentUser?.copy(
                            gender = gender,
                            phone = phone,
                            bloodGroup = bloodGroup,
                            age = age
                        )
                        onSuccess()
                    } else {
                        apiErrorMessage = body?.message ?: "Update failed"
                    }
                } else {
                    apiErrorMessage = "Server Error: ${response.code()}"
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPassword(email: String, phone: String, newPass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.resetPassword(email, phone, newPass)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        onSuccess()
                    } else {
                        apiErrorMessage = body?.message ?: "Reset failed"
                    }
                } else {
                    apiErrorMessage = "Server Error: ${response.code()}"
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun handleNetworkError(e: Exception) {
        apiErrorMessage = when (e) {
            is IllegalStateException, is JsonSyntaxException -> 
                "Server returned an invalid response. Please check your network connection or server status."
            else -> "Connection Error: ${e.localizedMessage}"
        }
    }

    fun fetchInitialData(userId: String) {
        viewModelScope.launch {
            try {
                // 1. Fetch Profiles
                val pResponse = RetrofitClient.instance.getProfiles(userId)
                if (pResponse.isSuccessful) {
                    val profileList = pResponse.body() ?: emptyList()
                    val profilesWithRecords = mutableListOf<Profile>()
                    
                    // 2. For each profile, fetch its medical records
                    for (profile in profileList) {
                        val rResponse = RetrofitClient.instance.getMedicalRecords(profile.id)
                        val records = if (rResponse.isSuccessful) rResponse.body() else emptyList()
                        profilesWithRecords.add(profile.copy(records = records))
                    }
                    
                    _profiles.clear()
                    _profiles.addAll(profilesWithRecords)
                }

                // 3. Fetch Reminders
                val remResponse = RetrofitClient.instance.getReminders(userId)
                if (remResponse.isSuccessful) {
                    _reminders.clear()
                    _reminders.addAll(remResponse.body() ?: emptyList())
                }
            } catch (e: Exception) {
                // Silent fail or handle error
            }
        }
    }

    fun addProfile(name: String, age: String, gender: String, blood: String, relation: String, height: String, weight: String, onSuccess: () -> Unit) {
        val userId = currentUser?.id ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.addProfile(userId, name, age, gender, blood, relation, height, weight)
                if (response.isSuccessful) { fetchInitialData(userId); onSuccess() }
            } catch (e: Exception) { apiErrorMessage = e.localizedMessage }
            finally { isLoading = false }
        }
    }

    fun deleteProfile(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.deleteProfile(id)
                if (response.isSuccessful) { _profiles.removeAll { it.id == id }; onSuccess() }
            } catch (e: Exception) { apiErrorMessage = e.localizedMessage }
        }
    }

    fun updateProfile(profile: Profile, onSuccess: () -> Unit) {
        val userId = currentUser?.id ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.updateProfile(
                    profileId = profile.id,
                    name = profile.name,
                    ageOrDob = profile.ageOrDob,
                    gender = profile.gender,
                    bloodGroup = profile.bloodGroup,
                    relationship = profile.relationship,
                    height = profile.height,
                    weight = profile.weight
                )
                if (response.isSuccessful && response.body()?.status == "success") {
                    // Update the in-memory list to reflect changes immediately
                    val idx = _profiles.indexOfFirst { it.id == profile.id }
                    if (idx >= 0) {
                        _profiles[idx] = profile.copy(records = _profiles[idx].records)
                    }
                    // Refresh full data from server
                    fetchInitialData(userId)
                    onSuccess()
                } else {
                    apiErrorMessage = response.body()?.message ?: "Profile update failed"
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun addMedicalRecord(
        context: Context,
        profileId: String,
        hospital: String,
        reason: String,
        diagnosis: String,
        date: String,
        uri: Uri?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val profileIdBody = profileId.toRequestBody("text/plain".toMediaTypeOrNull())
                val hospitalBody = hospital.toRequestBody("text/plain".toMediaTypeOrNull())
                val reasonBody = reason.toRequestBody("text/plain".toMediaTypeOrNull())
                val dateBody = date.toRequestBody("text/plain".toMediaTypeOrNull())
                val diagnosisBody = diagnosis.toRequestBody("text/plain".toMediaTypeOrNull())

                var reportPart: MultipartBody.Part? = null
                uri?.let { fileUri ->
                    context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                        val bytes = inputStream.readBytes()
                        val requestFile = bytes.toRequestBody(
                            (context.contentResolver.getType(fileUri) ?: "application/octet-stream").toMediaTypeOrNull()
                        )
                        reportPart = MultipartBody.Part.createFormData("report", "report_file", requestFile)
                    }
                }

                val response = RetrofitClient.instance.addMedicalRecord(
                    profileIdBody, hospitalBody, reasonBody, dateBody, diagnosisBody, reportPart
                )

                if (response.isSuccessful && response.body()?.status == "success") {
                    currentUser?.id?.let { fetchInitialData(it) }
                    onSuccess()
                } else {
                    apiErrorMessage = response.body()?.message ?: "Failed to save record"
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun addReminder(reminder: Reminder) {
        val userId = currentUser?.id ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.addReminder(reminder.id, userId, reminder.title, reminder.type.name, reminder.time, reminder.date)
                if (response.isSuccessful) _reminders.add(reminder)
            } catch (e: Exception) { apiErrorMessage = e.localizedMessage }
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.deleteReminder(reminder.id)
                if (response.isSuccessful) _reminders.remove(reminder)
            } catch (e: Exception) { apiErrorMessage = e.localizedMessage }
        }
    }

    // --- AI Methods (Routed via Flask Backend) ---
    fun generateAiSummary(context: Context, uri: Uri) {
        viewModelScope.launch {
            isAiLoading = true
            aiSummary = "Extracting text..."
            try {
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                val image = InputImage.fromFilePath(context, uri)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        if (visionText.text.isNotBlank()) {
                            summarizeReportText(visionText.text)
                        } else {
                            aiSummary = "No text found in image."
                            isAiLoading = false
                        }
                    }
                    .addOnFailureListener { e ->
                        aiSummary = "OCR Error: ${e.localizedMessage}"
                        isAiLoading = false
                    }
            } catch (e: Exception) {
                aiSummary = "Error: ${e.localizedMessage}"
                isAiLoading = false
            }
        }
    }

    fun summarizeReportText(reportText: String) {
        if (reportText.isBlank()) return
        viewModelScope.launch {
            isAiLoading = true
            aiSummary = "Generating AI Summary..."
            try {
                val response = RetrofitClient.instance.summarizeReport(reportText)
                if (response.isSuccessful && response.body()?.status == "success") {
                    aiSummary = response.body()?.summary ?: "No summary generated."
                } else {
                    aiSummary = response.body()?.message ?: "Failed to generate summary."
                }
            } catch (e: Exception) {
                handleNetworkError(e)
                aiSummary = apiErrorMessage ?: "Connection error."
            } finally {
                isAiLoading = false
            }
        }
    }

    fun explainMedicalTerms(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            isAiLoading = true
            aiSummary = "Explaining medical term..."
            try {
                val response = RetrofitClient.instance.explainTerms(query)
                if (response.isSuccessful && response.body()?.status == "success") {
                    aiSummary = response.body()?.explanation ?: "No explanation available."
                } else {
                    aiSummary = response.body()?.message ?: "Failed to explain term."
                }
            } catch (e: Exception) {
                handleNetworkError(e)
                aiSummary = apiErrorMessage ?: "Connection error."
            } finally {
                isAiLoading = false
            }
        }
    }

    fun checkMedicinePurpose(medicine: String) {
        if (medicine.isBlank()) return
        viewModelScope.launch {
            isAiLoading = true
            aiSummary = "Analyzing medicine purpose..."
            try {
                val response = RetrofitClient.instance.medicinePurpose(medicine)
                if (response.isSuccessful && response.body()?.status == "success") {
                    aiSummary = response.body()?.purpose ?: "No purpose information found."
                } else {
                    aiSummary = response.body()?.message ?: "Failed to check medicine purpose."
                }
            } catch (e: Exception) {
                handleNetworkError(e)
                aiSummary = apiErrorMessage ?: "Connection error."
            } finally {
                isAiLoading = false
            }
        }
    }

}
