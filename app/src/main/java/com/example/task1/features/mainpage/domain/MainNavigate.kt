package com.example.task1.features.mainpage.domain

import android.os.Bundle
import androidx.navigation.NavController
import com.example.task1.R

class MainNavigate {
    private fun createQuizIdBundle(link: String): Bundle {
        return Bundle().apply {
            putString("quizId", link)
        }
    }

    fun runQuiz(navController: NavController, link: String) {
        if (link != "") {
            navController.navigate(R.id.quizFragment, createQuizIdBundle(link))
        }
    }

    fun watchQuiz(navController: NavController, link: String) {
        if (link != "") {
            navController.navigate(R.id.watchFragment, createQuizIdBundle(link))
        }
    }
}