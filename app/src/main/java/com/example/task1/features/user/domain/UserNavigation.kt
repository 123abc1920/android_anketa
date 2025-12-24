package com.example.task1.features.user.domain

import android.os.Bundle
import androidx.navigation.NavController
import com.example.task1.R

class UserNavigation {

    private fun createBundle(quizId: String): Bundle {
        return Bundle().apply {
            putString("quizId", quizId)
        }
    }

    fun runQuiz(navController: NavController, quizId: String) {
        navController.navigate(R.id.quizFragment, createBundle(quizId))
    }

    fun watchQuiz(navController: NavController, quizId: String) {
        navController.navigate(R.id.watchFragment, createBundle(quizId))
    }

    fun editQuiz(navController: NavController, quizId: String) {
        navController.navigate(R.id.editQuizFragment, createBundle(quizId))
    }

}