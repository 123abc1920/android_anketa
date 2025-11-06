package com.example.task1.controller.models.app_models

data class UserAnswer(
    val id: Int,
    val question_id: Int,
    val answer_id: Int?,
    val result_id: Int,
    val text: String?
)