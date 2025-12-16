package com.example.task1.data.api.models

import com.example.task1.data.database.models.AnswerInCreateQuiz

data class Question(
    val text: String,
    val is_required: Boolean,
    val answers: List<AnswerInCreateQuiz>
)