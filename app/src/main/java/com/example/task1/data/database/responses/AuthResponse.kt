package com.example.task1.data.database.responses

data class AuthResponse(
    val result: String,
    val token: String? = null
)