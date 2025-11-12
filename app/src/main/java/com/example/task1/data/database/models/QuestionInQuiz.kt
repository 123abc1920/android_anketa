package com.example.task1.data.database.models

data class QuestionInQuiz(
    val question_text: String,
    val id: String,
    val required: Boolean,
    val answers: List<AnswerInQuiz>,
    var selectedAnswerId: String? = null,
    var selectedAnswerText: String? = null
)