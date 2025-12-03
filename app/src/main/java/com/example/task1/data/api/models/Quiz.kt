package com.example.task1.data.api.models

data class Quiz(
    val quiz_name: String,
    val author_name: String,
    val end_date: String,
    val id: Int,
    val crypted_link: String
)