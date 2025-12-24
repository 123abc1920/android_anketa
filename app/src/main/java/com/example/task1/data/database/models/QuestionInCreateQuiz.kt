package com.example.task1.data.database.models

data class QuestionInCreateQuiz(
    var text: String,
    val id: String,
    var is_required: Boolean,
    val answers: List<AnswerInQuiz>?,
    var selectedAnswerId: String? = null,
    var selectedAnswerText: String? = null
)