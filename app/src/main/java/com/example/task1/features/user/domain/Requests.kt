package com.example.task1.features.user.domain

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.requests.LoginRequest
import com.example.task1.data.database.responses.ResultResponse
import com.example.task1.domain.authorisation.getUserIdHeader
import com.example.task1.domain.authorisation.saveUserId
import com.example.task1.domain.toasts.showToast
import kotlinx.coroutines.launch

class Requests {

    fun login(
        owner: LifecycleOwner,
        navController: NavController,
        context: Context,
        login: String,
        password: String
    ) {
        owner.lifecycleScope.launch {
            try {
                val response =
                    RetrofitClient.apiService.login(LoginRequest(login, password))
                if (response.result == "success") {
                    saveUserId(response.token.toString())
                    navController.navigate(R.id.accountFragment)
                } else {
                    showToast(context, response.result)
                }
            } catch (e: Exception) {
                Log.e("Login Error", "Ошибка: ${e.message}")
                navController.navigate(R.id.loginFragment)
            }
        }
    }

    fun signup(
        owner: LifecycleOwner,
        navController: NavController,
        context: Context,
        login: String,
        password: String
    ) {
        owner.lifecycleScope.launch {
            try {
                val response =
                    RetrofitClient.apiService.signup(LoginRequest(login, password))
                if (response.result == "success") {
                    saveUserId(response.token.toString())
                    navController.navigate(R.id.accountFragment)
                } else {
                    showToast(context, response.result)
                }
            } catch (e: Exception) {
                Log.e("Signup Error", "Ошибка: ${e.message}")
                navController.navigate(R.id.loginFragment)
            }
        }
    }

    fun loadUserData(
        owner: LifecycleOwner,
        navController: NavController
    ): Map<String, Any> {
        var map = mapOf<String, Any>(
            "username" to "",
            "login" to "",
            "createdQuizzes" to mutableListOf<Quiz>(),
            "doneQuiz" to mutableListOf<Quiz>()
        )

        owner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserData(
                    getUserIdHeader()
                )

                if (response.result == "success") {
                    map = mapOf<String, Any>(
                        "username" to response.username.toString(),
                        "login" to response.login.toString(),
                        "createdQuizzes" to response.created_quizes as MutableList<Quiz>,
                        "doneQuizzes" to response.done_quizes as MutableList<Quiz>
                    )
                }
            } catch (e: Exception) {
                Log.e("Account Error", "Ошибка загрузки данных пользователя", e)
                navController.navigate(R.id.loginFragment)
            }
        }

        return map
    }

    fun deleteQuiz(owner: LifecycleOwner, context: Context, quizId: Int?): Boolean {
        var success = true

        if (quizId == null) {
            return false
        }

        owner.lifecycleScope.launch {
            var response = ResultResponse("unsuccess")
            response = RetrofitClient.apiService.deleteQuiz(mapOf("id" to quizId))
            if (response.result == "success") {
                showToast(context, "Анкета удалена!")
            } else {
                success = false
            }
        }

        return success
    }

}