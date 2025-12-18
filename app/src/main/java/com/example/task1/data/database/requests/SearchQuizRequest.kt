package com.example.task1.data.database.requests

data class Filter(
    val inName: Boolean,
    val inAuthor: Boolean,
    val ended: Boolean,
    val notEnded: Boolean,
    val nearEnded: Boolean,
    val nearNotEnded: Boolean,
    val answers: String = "",
    val startTime: String = "",
    val endTime: String = ""
)

data class SearchQuizRequest(
    var page: Int,
    var per_page: Int,
    var text: String,
    var filters: Filter
)