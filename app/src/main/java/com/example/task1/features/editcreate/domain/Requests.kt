package com.example.task1.features.editcreate.domain

import android.content.Context
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Question
import com.example.task1.data.database.models.AnswerInCreateQuiz
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.domain.authorisation.getUserId
import kotlinx.coroutines.launch

class Requests {

    fun createQuiz(
        owner: LifecycleOwner,
        navController: NavController,
        context: Context,
        createdQuiz: CreateQuizResponse
    ) {
        owner.lifecycleScope.launch {
            val response = RetrofitClient.apiService.createQuiz(
                "Bearer ${getUserId()}", createdQuiz
            )
            if (response.result == "success") {
                navController.navigate(R.id.accountFragment)
                Toast.makeText(context, "Анкета создана!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun loadQuizData(
        owner: LifecycleOwner,
        navController: NavController,
        quizId: String?
    ): Map<String, Any> {
        var quizName: String = ""
        var startData: String = ""
        var endData: String = ""
        var questinList: MutableList<com.example.task1.data.database.responses.Question> =
            mutableListOf()

        if (quizId != null) {
            owner.lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.getQuizData(
                        "Bearer ${getUserId()}",
                        quizId
                    )

                    quizName = response.quiz_name
                    startData = response.start_date
                    endData = response.end_date
                    questinList = response.questions_list
                } catch (e: Exception) {
                    Log.e("API ERROR", "Ошибка загрузки анкеты", e)
                    navController.navigate(R.id.accountFragment)
                }
            }
        }

        return mapOf<String, Any>(
            "quizName" to quizName,
            "startData" to startData,
            "endData" to endData,
            "questionList" to questinList
        )
    }

    fun sendCreatedQuiz(
        owner: LifecycleOwner,
        context: Context,
        navController: NavController,
        editQuizRequest: EditQuizRequest
    ) {
        owner.lifecycleScope.launch {
            val response = RetrofitClient.apiService.editQuiz(
                editQuizRequest
            )
            if (response.result == "success") {
                navController.navigate(R.id.accountFragment)
                Toast.makeText(context, "Анкета обновлена!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}