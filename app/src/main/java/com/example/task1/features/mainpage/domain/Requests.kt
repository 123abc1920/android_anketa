package com.example.task1.features.mainpage.domain

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.requests.SearchQuizRequest
import kotlinx.coroutines.launch

fun loadQuizzes(owner: LifecycleOwner, page: Int): MutableList<Quiz> {
    var quizList = mutableListOf<Quiz>()

    owner.lifecycleScope.launch {
        try {
            val response = RetrofitClient.apiService.getQuizzes(page, 12)

            quizList = response.quizes as MutableList<Quiz>
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка загрузки анкет", e)
        }
    }

    return quizList
}

fun search(owner: LifecycleOwner, searchQuizRequest: SearchQuizRequest): MutableList<Quiz> {
    var quizList = mutableListOf<Quiz>()

    owner.lifecycleScope.launch {
        try {
            val response = RetrofitClient.apiService.search(searchQuizRequest)
            quizList = response.quizes as MutableList<Quiz>
        } catch (e: Exception) {
            Log.e("SEARCH ERROR", "Ошибка поиска", e)
        }
    }

    return quizList
}