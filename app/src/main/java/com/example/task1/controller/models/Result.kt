package com.example.task1.controller.models

import java.util.Date

data class Result(
    val id: Int,
    val quiz_id: Int,
    val user_id: Int,
    val passage_date: Date
)