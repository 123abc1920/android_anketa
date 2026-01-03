package com.example.task1.features.settings.domain

import android.content.Context
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import com.example.task1.data.api.RetrofitClient
import com.example.task1.common.authorisation.getUserIdHeader
import com.example.task1.common.toasts.showToast
import com.example.task1.R

class SettingsRequests {

    suspend fun loadUserData(): Map<String, Any> {
        return try {
            val userData = RetrofitClient.apiService.getUserData(
                getUserIdHeader()
            )

            if (userData.result == "success") {
                mapOf(
                    "result" to View.VISIBLE,
                    "hint_result" to View.GONE,
                    "username" to userData.username.toString(),
                    "login" to userData.login.toString()
                )
            } else {
                mapOf(
                    "result" to View.GONE,
                    "hint_result" to View.VISIBLE,
                    "username" to "",
                    "login" to ""
                )
            }
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка загрузки данных", e)
            mapOf(
                "result" to View.GONE,
                "hint_result" to View.VISIBLE,
                "username" to "",
                "login" to ""
            )
        }
    }

    suspend fun saveUserData(context: Context, login: String, username: String): Boolean {
        return try {
            val result = RetrofitClient.apiService.setData(
                getUserIdHeader(),
                mapOf(
                    "login" to login,
                    "name" to username
                )
            )

            if (result.result == "success") {
                showToast(context, "Данные обновлены!")
                true
            } else {
                showToast(context, "${result.result}")
                false
            }
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка сохранения данных", e)
            showToast(context, "${e.message}")
            false
        }
    }

    suspend fun savePassword(
        context: Context,
        oldPassword: String,
        newPassword: String
    ): Boolean {
        return try {
            if (newPassword == "") {
                showToast(context, "Новый пароль не может быть пустым!")
                return false
            }

            val result = RetrofitClient.apiService.setPassword(
                getUserIdHeader(),
                mapOf(
                    "password" to oldPassword,
                    "new_password" to newPassword
                )
            )

            when (result.result) {
                "success" -> {
                    showToast(context, "Пароль изменен!")
                    true
                }

                "Старый пароль не верен!" -> {
                    showToast(context, "Старый пароль не верен!")
                    false
                }

                else -> {
                    showToast(context, "Ошибка изменения пароля")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка изменения пароля", e)
            showToast(context, "${e.message}")
            false
        }
    }

    suspend fun deleteAccount(context: Context, navController: NavController) {
        try {
            val result = RetrofitClient.apiService.deleteAccount(
                getUserIdHeader()
            )

            if (result.result == "success") {
                showToast(context, "Аккаунт удален.")
                navController.navigate(R.id.mainFragment)
            } else {
                showToast(context, "${result.result}")
            }
        } catch (e: Exception) {
            Log.e("API ERROR", "Ошибка удаления", e)
            showToast(context, "${e.message}")
        }
    }
}