package com.example.task1.controller.models.responses

import com.example.task1.controller.models.app_models.Quiz

data class UserDataResponse(
    val result: String,
    val username: String? = null,
    val user_id: String? = null,
    val created_quizes: List<Quiz>?,
    val done_quizes: List<Quiz>?
)