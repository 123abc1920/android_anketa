package com.example.task1.data.database.responses

import com.example.task1.data.api.models.Quiz

data class QuizzesResponse(
    val quizes: List<Quiz>,
    val pagination: String,
    val max: Int
)