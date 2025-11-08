package com.example.task1.data.api.models

data class Question(
    val id: Int,
    val text: String,
    val quiz_id: Int,
    val is_required: Boolean
)