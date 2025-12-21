package com.example.task1.data.database.responses

import com.example.task1.data.api.models.Quiz

data class UserDataResponse(
    val result: String,
    val username: String? = null,
    val login: String?,
    val user_id: String? = null,
    val created_quizes: MutableList<Quiz>?,
    val done_quizes: MutableList<Quiz>?
)