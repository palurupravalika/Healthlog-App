package com.example.healthlog.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class MedicalRecord(
    val id: String = "",
    val hospital: String,
    val reason: String,
    val date: String,
    val diagnosis: String = "",
    @SerializedName("report_uri")
    val reportUri: String? = null,
    @SerializedName("report_url")
    val reportUrl: String? = null,
    @SerializedName("document_url")
    val documentUrl: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null
) {
    val effectiveReportUri: String?
        get() = (reportUri ?: reportUrl ?: documentUrl ?: imageUrl)?.trim()
}

data class Profile(
    val id: String = "",
    val name: String,
    @SerializedName("age_or_dob")
    val ageOrDob: String = "",
    val gender: String = "",
    @SerializedName("blood_group")
    val bloodGroup: String = "",
    val relationship: String = "",
    val height: String = "",
    val weight: String = "",
    // Changed to nullable to handle cases where JSON might explicitly return null
    val records: List<MedicalRecord>? = emptyList()
) {
    val icon: ImageVector get() = Icons.Default.Person
}

enum class ReminderType {
    Medicine, DoctorAppointment
}

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: ReminderType,
    val time: String,
    val date: String
)
