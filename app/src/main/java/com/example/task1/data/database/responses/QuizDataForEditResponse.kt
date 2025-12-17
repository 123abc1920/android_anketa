package com.example.task1.data.database.responses

import com.example.task1.data.api.models.Answer

data class Question(
    var question_text: String,
    val id: String,
    val required: String,
    val answers: MutableList<Answer>
)

data class QuizDataForEditResponse(
    val quiz_name: String,
    val end_date: String,
    val start_date: String,
    val is_author_shown: Boolean,
    val is_shown: Boolean,
    val id: Int,
    val crypted_link: String,
    val questions_list: MutableList<Question>
)