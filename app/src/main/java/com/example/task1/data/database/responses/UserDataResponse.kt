package com.example.task1.data.database.responses

import com.example.task1.data.api.models.Quiz

data class UserDataResponse(
    val result: String,
    val username: String? = null,
    val user_id: String? = null,
    val created_quizes: List<Quiz>?,
    val done_quizes: List<Quiz>?
)