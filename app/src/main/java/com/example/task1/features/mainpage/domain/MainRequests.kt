package com.example.task1.features.mainpage.domain

import android.util.Log
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.requests.SearchQuizRequest

class MainRequests {
    suspend fun loadQuizzes(page: Int): Map<String, Any> {
        return try {
            val response = RetrofitClient.apiService.getQuizzes(page, 12)
            mapOf<String, Any>("quizzes" to response.quizes, "max" to response.max)
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка загрузки анкет", e)
            mapOf<String, Any>("quizzes" to emptyList<Quiz>() as List<Quiz>, "max" to 1)
        }
    }

    suspend fun search(searchQuizRequest: SearchQuizRequest): Map<String, Any> {
        return try {
            val response = RetrofitClient.apiService.search(searchQuizRequest)
            mapOf<String, Any>("quizzes" to response.quizes, "max" to response.max)
        } catch (e: Exception) {
            Log.e("SEARCH ERROR", "Ошибка поиска", e)
            mapOf<String, Any>("quizzes" to emptyList<Quiz>() as List<Quiz>, "max" to 1)
        }
    }
}