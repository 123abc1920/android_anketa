package com.example.task1.features.user.domain

import android.content.Context
import android.util.Log
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.requests.LoginRequest
import com.example.task1.common.authorisation.getUserIdHeader
import com.example.task1.common.authorisation.saveUserId
import com.example.task1.common.toasts.showToast

class UserRequests {

    suspend fun login(
        login: String,
        password: String
    ): Result<String> {
        return try {
            val response = RetrofitClient.apiService.login(LoginRequest(login, password))
            if (response.result == "success") {
                saveUserId(response.token.toString())
                Result.Success(response.result)
            } else {
                Result.Error(response.result)
            }
        } catch (e: Exception) {
            Log.e("Login Error", "Ошибка: ${e.message}")
            Result.Error("Ошибка соединения")
        }
    }

    suspend fun signup(
        login: String,
        password: String
    ): Result<String> {
        return try {
            val response = RetrofitClient.apiService.signup(LoginRequest(login, password))
            if (response.result == "success") {
                saveUserId(response.token.toString())
                Result.Success(response.result)
            } else {
                Result.Error(response.result)
            }
        } catch (e: Exception) {
            Log.e("Signup Error", "Ошибка: ${e.message}")
            Result.Error("Ошибка соединения")
        }
    }

    suspend fun loadUserData(): Result<Map<String, Any>> {
        return try {
            val response = RetrofitClient.apiService.getUserData(getUserIdHeader())

            if (response.result == "success") {
                Result.Success(mapOf<String, Any>(
                    "username" to response.username.toString(),
                    "login" to response.login.toString(),
                    "createdQuizzes" to (response.created_quizes as? List<Quiz> ?: mutableListOf<Quiz>()),
                    "doneQuizzes" to (response.done_quizes as? List<Quiz> ?: mutableListOf<Quiz>())
                ))
            } else {
                Result.Error("Ошибка загрузки данных")
            }
        } catch (e: Exception) {
            Log.e("Account Error", "Ошибка загрузки данных пользователя", e)
            Result.Error("Ошибка соединения")
        }
    }

    suspend fun deleteQuiz(context: Context, quizId: Int?): Boolean {
        if (quizId == null) return false

        return try {
            val response = RetrofitClient.apiService.deleteQuiz(mapOf("id" to quizId))
            if (response.result == "success") {
                showToast(context, "Анкета удалена!")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("Delete Error", "Ошибка удаления анкеты", e)
            false
        }
    }

    sealed class Result<T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error<T>(val message: String) : Result<T>()
    }
}