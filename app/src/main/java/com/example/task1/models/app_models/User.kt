package com.example.task1.models.app_models

data class User(
    val id: Int,
    val name: String?,
    val login: String,
    val password_hash: String
)