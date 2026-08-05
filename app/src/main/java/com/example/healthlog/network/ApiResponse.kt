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

// Groq Request Models
data class GroqRequest(
    val messages: List<GroqMessage>,
    val model: String = "mixtral-8x7b-32768"
)

data class GroqMessage(
    val role: String,
    val content: String
)

// Groq Response Models
data class GroqResponse(
    val choices: List<GroqChoice>
)

data class GroqChoice(
    val message: GroqMessage
)
