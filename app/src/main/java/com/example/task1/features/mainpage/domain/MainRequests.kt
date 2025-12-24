package com.example.task1.features.mainpage.domain

import android.util.Log
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.requests.SearchQuizRequest

class MainRequests {
    suspend fun loadQuizzes(page: Int): List<Quiz> {
        return try {
            val response = RetrofitClient.apiService.getQuizzes(page, 12)
            response.quizes
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка загрузки анкет", e)
            emptyList()
        }
    }

    suspend fun search(searchQuizRequest: SearchQuizRequest): List<Quiz> {
        return try {
            val response = RetrofitClient.apiService.search(searchQuizRequest)
            response.quizes
        } catch (e: Exception) {
            Log.e("SEARCH ERROR", "Ошибка поиска", e)
            emptyList()
        }
    }
}