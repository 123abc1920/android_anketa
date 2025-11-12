package com.example.task1.data.database.responses

import com.example.task1.data.api.models.Question
import com.example.task1.data.database.models.QuestionInQuiz

data class QuizDataResponse(
    val author_name: String,
    val quiz_name: String,
    val end_date: String,
    val start_date: String,
    val id: String,
    val questions_list: List<QuestionInQuiz>
)