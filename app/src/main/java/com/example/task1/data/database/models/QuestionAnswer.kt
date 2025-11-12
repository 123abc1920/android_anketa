package com.example.task1.data.database.models

import com.google.gson.annotations.SerializedName

data class QuestionAnswer(
    val id: String,
    val answer_id: String?,
    val answer_text: String?
)