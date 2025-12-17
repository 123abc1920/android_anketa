package com.example.task1.data.database.responses

data class EditQuizRequest(
    val id: String,
    val name: String,
    val is_author_shown: Boolean,
    val is_shown: Boolean,
    val start_date: String,
    val end_date: String,
    val questions: List<QuestionRequest>
)

data class QuestionRequest(
    val id: String,
    val text: String,
    val is_required: Boolean,
    val answers: List<AnswerRequest>
)

data class AnswerRequest(
    val id: String,
    val text: String
)