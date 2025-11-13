package com.example.task1.data.database.responses

data class QuestionInWatch(
    val question_text: String,
    val id: String,
    val required: String,
    val answers: List<String>,
    val statistics: List<Int>?
)

data class QuizStatisticsResponse (
    val author_name: String,
    val quiz_name: String,
    val end_date: String,
    val start_date: String,
    val id: String,
    val questions_list: List<QuestionInWatch>,
    var users_list: List<String>
)