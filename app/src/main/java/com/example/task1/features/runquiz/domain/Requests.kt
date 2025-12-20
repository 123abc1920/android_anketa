package com.example.task1.features.runquiz.domain

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.data.database.requests.QuizRequest
import com.example.task1.domain.authorisation.getUserIdHeader
import com.example.task1.domain.toasts.showToast
import kotlinx.coroutines.launch

class Requests {
    fun loadQuiz(
        owner: LifecycleOwner,
        navController: NavController,
        context: Context,
        quizId: String?
    ): Map<String, Any> {
        var map = mapOf<String, Any>(
            "quizName" to "",
            "startDate" to "",
            "endDate" to "",
            "authorName" to "",
            "cryptedLink" to "",
            "questionsList" to mutableListOf<QuestionInQuiz>()
        )

        if (quizId != null) {
            owner.lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.startAnketa(
                        getUserIdHeader(),
                        quizId
                    )

                    if (response.result == "success") {
                        map = mapOf(
                            "quizName" to response.quiz_name,
                            "startDate" to response.start_date,
                            "endDate" to response.end_date,
                            "authorName" to response.author_name,
                            "cryptedLink" to response.crypted_link,
                            "questionsList" to response.questions_list as MutableList<QuestionInQuiz>
                        )
                    }

                    if (response.result == "end date") {
                        navController.navigate(R.id.mainFragment)
                        showToast(context, "Анкета уже завершена!")
                    }
                } catch (e: Exception) {
                    Log.e("API ERROR", "Ошибка загрузки анкеты", e)
                    navController.navigate(R.id.accountFragment)
                }
            }
        }

        return map
    }

    fun sendQuiz(
        owner: LifecycleOwner,
        context: Context,
        navController: NavController,
        requestData: QuizRequest
    ) {
        owner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.sendQuiz(
                    getUserIdHeader(), requestData
                )
                if (response.result == "success") {
                    navController.navigate(R.id.mainFragment)
                    showToast(context, "Анкета отправлена!")
                }
            } catch (e: Exception) {
                Log.e("Ошибка отправки", e.toString())
            }
        }
    }
}