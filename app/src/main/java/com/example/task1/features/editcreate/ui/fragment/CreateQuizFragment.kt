package com.example.task1.features.editcreate.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Question
import com.example.task1.data.database.models.AnswerInCreateQuiz
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.domain.authorisation.getUserId
import com.example.task1.features.editcreate.domain.InitDatePickers
import com.example.task1.features.editcreate.ui.adapter.CreateQuizAdapter
import kotlinx.coroutines.launch
import java.util.Calendar

class CreateQuizFragment : Fragment() {
    private lateinit var createdQuestionView: RecyclerView
    private lateinit var createdQuestionAdapter: CreateQuizAdapter

    private var questions = mutableListOf<QuestionInQuiz>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_quiz, container, false)

        createdQuestionView = view.findViewById<RecyclerView>(R.id.create_quiz_view)
        createdQuestionAdapter = CreateQuizAdapter(questions)
        createdQuestionView.adapter = createdQuestionAdapter
        createdQuestionView.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<Button>(R.id.add_question_btn).setOnClickListener {
            val position = questions.size
            questions.add(QuestionInQuiz("", "1", false, emptyList(), null, null))
            createdQuestionAdapter.notifyItemInserted(position)
        }

        var startDate = view.findViewById<EditText>(R.id.start_date)
        InitDatePickers.InitDatePicker(startDate, requireContext())

        var endDate = view.findViewById<EditText>(R.id.end_date)
        InitDatePickers.InitDatePicker(endDate, requireContext())

        view.findViewById<Button>(R.id.send_created_quiz).setOnClickListener {
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

            val createdQuiz = CreateQuizResponse(
                view.findViewById<EditText>(R.id.quiz_name).text.toString(),
                view.findViewById<CheckBox>(R.id.author_shown).isChecked,
                view.findViewById<CheckBox>(R.id.quiz_shown).isChecked,
                view.findViewById<EditText>(R.id.start_date).text.toString(),
                view.findViewById<EditText>(R.id.end_date).text.toString(),
                questionsList
            )

            viewLifecycleOwner.lifecycleScope.launch {
                val response = RetrofitClient.apiService.createQuiz(
                    "Bearer ${getUserId()}", createdQuiz
                )
                if (response.result == "success") {
                    findNavController().navigate(R.id.accountFragment)
                    Toast.makeText(requireContext(), "Анкета создана!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        return view
    }
}