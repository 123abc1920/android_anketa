package com.example.task1.data.database.requests

import com.example.task1.data.database.models.QuestionAnswer

data class QuizRequest(
    val quiz_id: String,
    val questions: List<QuestionAnswer>
)