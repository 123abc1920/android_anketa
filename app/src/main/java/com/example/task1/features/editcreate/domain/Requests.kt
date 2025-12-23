package com.example.task1.features.editcreate.domain

import android.util.Log
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.data.database.responses.Question
import com.example.task1.domain.authorisation.getUserIdHeader

class Requests {

    suspend fun createQuiz(createdQuiz: CreateQuizResponse): Boolean {
        return try {
            val response = RetrofitClient.apiService.createQuiz(
                getUserIdHeader(), createdQuiz
            )
            response.result == "success"
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка создания анкеты", e)
            false
        }
    }

    suspend fun loadQuizData(quizId: String?): Map<String, Any> {
        return try {
            if (quizId != null) {
                val response = RetrofitClient.apiService.getQuizData(
                    getUserIdHeader(),
                    quizId
                )

                mapOf<String, Any>(
                    "quizName" to response.quiz_name,
                    "startData" to response.start_date,
                    "endData" to response.end_date,
                    "questionList" to response.questions_list
                )
            } else {
                mapOf<String, Any>(
                    "quizName" to "",
                    "startData" to "",
                    "endData" to "",
                    "questionList" to mutableListOf<Question>()
                )
            }
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка загрузки анкеты", e)
            mapOf<String, Any>(
                "quizName" to "",
                "startData" to "",
                "endData" to "",
                "questionList" to mutableListOf<Question>()
            )
        }
    }

    suspend fun sendCreatedQuiz(editQuizRequest: EditQuizRequest): Boolean {
        return try {
            val response = RetrofitClient.apiService.editQuiz(editQuizRequest)
            response.result == "success"
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка обновления анкеты", e)
            false
        }
    }
}