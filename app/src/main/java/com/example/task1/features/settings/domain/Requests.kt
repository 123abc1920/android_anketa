package com.example.task1.features.settings.domain

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.task1.data.api.RetrofitClient
import com.example.task1.domain.authorisation.getUserIdHeader
import com.example.task1.domain.toasts.showToast
import kotlinx.coroutines.launch

class Requests {

    fun loadUserData(owner: LifecycleOwner): Map<String, Any> {
        var map = mapOf<String, Any>("result" to View.GONE, "username" to "", "login" to "")

        owner.lifecycleScope.launch {
            try {
                var userData = RetrofitClient.apiService.getUserData(
                    getUserIdHeader()
                )

                if (userData.result == "success") {
                    map = mapOf(
                        "result" to View.VISIBLE,
                        "username" to userData.username.toString(),
                        "login" to userData.login.toString()
                    )
                }
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки данных", e)
            }
        }

        return map
    }

    fun saveUserData(owner: LifecycleOwner, context: Context, login: String, username: String) {
        owner.lifecycleScope.launch {
            var result = RetrofitClient.apiService.setData(
                getUserIdHeader(),
                mapOf(
                    "login" to login,
                    "name" to username
                )
            )

            if (result.result == "success") {
                showToast(context, "Данные обновлены!")
            }
        }
    }

    fun savePassword(
        owner: LifecycleOwner,
        context: Context,
        oldPassword: String,
        newPassword: String
    ): Boolean {
        var success = true

        owner.lifecycleScope.launch {
            var result = RetrofitClient.apiService.setPassword(
                getUserIdHeader(),
                mapOf(
                    "old_password" to oldPassword,
                    "new_password" to newPassword
                )
            )

            if (result.result == "success") {
                showToast(context, "Пароль изменен!")
            } else if (result.result == "Старый пароль не верен!") {
                showToast(context, "Старый пароль не верен!")
                success = false
            }
        }

        return success
    }

}