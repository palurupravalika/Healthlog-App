package com.example.healthlog

import com.google.gson.annotations.SerializedName

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val gender: String = "",
    val phone: String = "",
    @SerializedName("blood_group")
    val bloodGroup: String = "",
    val age: String = ""
)
