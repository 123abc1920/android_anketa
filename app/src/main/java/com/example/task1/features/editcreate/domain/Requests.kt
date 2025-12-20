package com.example.task1.features.editcreate.domain

import android.content.Context
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Question
import com.example.task1.data.database.models.AnswerInCreateQuiz
import com.example.task1.data.database.responses.AnswerRequest
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.data.database.responses.QuestionRequest
import com.example.task1.domain.authorisation.getUserId
import kotlinx.coroutines.launch
import kotlin.random.Random

fun collectQuestions(createdQuestionView: RecyclerView): MutableList<Question> {
    val questionsList = mutableListOf<Question>()

    for (i in 0 until createdQuestionView.childCount) {
        val questionView = createdQuestionView.getChildAt(i)

        val questionName =
            questionView.findViewById<EditText>(R.id.question_name).text.toString()
        val isRequired = questionView.findViewById<CheckBox>(R.id.is_required).isChecked

        val answersRecyclerView = questionView.findViewById<RecyclerView>(R.id.answers_view)
        val answersList = mutableListOf<AnswerInCreateQuiz>()

        for (j in 0 until answersRecyclerView.childCount) {
            val answerView = answersRecyclerView.getChildAt(j)
            val answerText =
                answerView.findViewById<EditText>(R.id.answer_text).text.toString()

            if (answerText.isNotEmpty()) {
                answersList.add(AnswerInCreateQuiz(answerText))
            }
        }

        questionsList.add(
            Question(
                questionName,
                isRequired,
                answersList
            )
        )
    }

    return questionsList
}

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