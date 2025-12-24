package com.example.task1.data.database.responses

data class CreateQuizResponse(
    val name: String,
    val is_author_shown: Boolean,
    val is_shown: Boolean,
    val start_date: String,
    val end_date: String,
    val questions: List<Any?>
)