package com.example.task1.features.runquiz.domain

import android.util.Log
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.data.database.requests.QuizRequest
import com.example.task1.commondomain.authorisation.getUserIdHeader

class RunRequests {

    suspend fun loadQuiz(quizId: String?): Map<String, Any> {
        if (quizId == null || quizId.isEmpty()) {
            return mapOf("shouldNavigate" to true)
        }

        return try {
            val response = RetrofitClient.apiService.startAnketa(
                getUserIdHeader(),
                quizId
            )

            when (response.result) {
                "success" -> mapOf(
                    "quizName" to response.quiz_name,
                    "startDate" to response.start_date,
                    "endDate" to response.end_date,
                    "authorName" to response.author_name,
                    "cryptedLink" to response.crypted_link,
                    "questionsList" to (response.questions_list ?: mutableListOf<QuestionInQuiz>()),
                    "shouldNavigate" to false
                )
                "end date" -> mapOf("shouldNavigate" to true)
                else -> mapOf("shouldNavigate" to true)
            }
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка загрузки анкеты", e)
            mapOf("shouldNavigate" to true)
        }
    }

    suspend fun sendQuiz(requestData: QuizRequest): Boolean {
        return try {
            val response = RetrofitClient.apiService.sendQuiz(
                getUserIdHeader(),
                requestData
            )
            response.result == "success"
        } catch (e: Exception) {
            Log.e("Ошибка отправки", e.toString())
            false
        }
    }
}