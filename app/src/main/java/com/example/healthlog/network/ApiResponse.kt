package com.example.healthlog.network

import com.example.healthlog.User

data class LoginResponse(
    val status: String,
    val message: String,
    val user: User? = null
)

data class RegisterResponse(
    val status: String,
    val message: String
)

data class AiSummarizeResponse(
    val status: String,
    val summary: String? = null,
    val message: String? = null
)

data class AiExplainTermsResponse(
    val status: String,
    val explanation: String? = null,
    val message: String? = null
)

data class AiMedicinePurposeResponse(
    val status: String,
    val purpose: String? = null,
    val message: String? = null
)
