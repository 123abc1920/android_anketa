package com.example.task1.features.watch.domain

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.task1.data.api.RetrofitClient
import kotlinx.coroutines.launch

class Requests {

    fun loadQuestions(owner: LifecycleOwner, quizId: String): Map<String, Any> {
        var map = mapOf<String, Any>(
            "quizName" to "",
            "startDate" to "",
            "endDate" to "",
            "authorName" to "",
            "link" to "",
            "questionsList" to "",
            "usersList" to ""
        )

        owner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.startPreview(quizId)
                map = mapOf<String, Any>(
                    "quizName" to response.quiz_name,
                    "startDate" to response.start_date,
                    "endDate" to response.end_date,
                    "authorName" to response.author_name,
                    "link" to response.crypted_link,
                    "questionsList" to response.questions_list,
                    "usersList" to response.users_list
                )
            } catch (e: Exception) {
                Log.e("Watch Error", "Ошибка загрузки превью анкеты", e)
            }
        }

        return map
    }

}