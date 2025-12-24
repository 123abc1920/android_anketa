package com.example.task1.features.watch.domain

import android.util.Log
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.responses.QuestionInWatch

class WatchRequests {

    suspend fun loadQuestions(quizId: String): Map<String, Any> {
        return try {
            val response = RetrofitClient.apiService.startPreview(quizId)
            mapOf<String, Any>(
                "quizName" to response.quiz_name,
                "startDate" to response.start_date,
                "endDate" to response.end_date,
                "authorName" to response.author_name,
                "link" to response.crypted_link,
                "questionsList" to (response.questions_list ?: mutableListOf<QuestionInWatch>()),
                "usersList" to (response.users_list ?: mutableListOf<String>())
            )
        } catch (e: Exception) {
            Log.e("Watch Error", "Ошибка загрузки превью анкеты", e)
            mapOf<String, Any>(
                "quizName" to "",
                "startDate" to "",
                "endDate" to "",
                "authorName" to "",
                "link" to "",
                "questionsList" to mutableListOf<QuestionInWatch>(),
                "usersList" to mutableListOf<String>()
            )
        }
    }
}