package com.example.task1.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.responses.AnswerRequest
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.data.database.responses.Question
import com.example.task1.data.database.responses.QuestionRequest
import com.example.task1.domain.authorisation.getUserId
import com.example.task1.ui.adapters.EditQuizAdapter
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.random.Random

class EditQuizFragment : Fragment() {

    private lateinit var createdQuestionView: RecyclerView
    private lateinit var createdQuestionAdapter: EditQuizAdapter

    private var questions = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_quiz, container, false)

        val quizId = arguments?.getString("quizId")

        createdQuestionView = view.findViewById(R.id.create_quiz_view)
        createdQuestionAdapter = EditQuizAdapter(questions)
        createdQuestionView.adapter = createdQuestionAdapter
        createdQuestionView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                quizId?.let {
                    val response = RetrofitClient.apiService.getQuizData(
                        "Bearer ${getUserId()}",
                        quizId
                    )

                    val quizName = view.findViewById<TextView>(R.id.quiz_name)
                    quizName.text = response.quiz_name

                    val startData = view.findViewById<EditText>(R.id.start_date)
                    startData.setText(response.start_date)

                    val endData = view.findViewById<EditText>(R.id.end_date)
                    endData.setText(response.end_date)

                    questions.clear()
                    questions.addAll(response.questions_list)
                    createdQuestionAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки анкеты", e)
                findNavController().navigate(R.id.accountFragment)
            }
        }

        view.findViewById<Button>(R.id.add_question_btn).setOnClickListener {
            val newQuestion = Question(
                "", "", "", mutableListOf(),
            )
            questions.add(newQuestion)
            createdQuestionAdapter.notifyDataSetChanged()
        }

        var startDate = view.findViewById<EditText>(R.id.start_date)
        startDate.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                val c = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(), { _, year, month, day ->
                        android.app.TimePickerDialog(
                            requireContext(), { _, hour, minute ->
                                val dateStr = String.format(
                                    "%04d-%02d-%02d %02d:%02d:00",
                                    year, month + 1, day, hour, minute
                                )
                                startDate.setText(dateStr)
                            },
                            c.get(Calendar.HOUR_OF_DAY),
                            c.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            false
        }

        var endDate = view.findViewById<EditText>(R.id.end_date)
        endDate.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                val c = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(), { _, year, month, day ->
                        android.app.TimePickerDialog(
                            requireContext(), { _, hour, minute ->
                                val dateStr = String.format(
                                    "%04d-%02d-%02d %02d:%02d:00",
                                    year, month + 1, day, hour, minute
                                )
                                endDate.setText(dateStr)
                            },
                            c.get(Calendar.HOUR_OF_DAY),
                            c.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            false
        }

        view.findViewById<Button>(R.id.send_created_quiz).setOnClickListener {
            val editRequest = EditQuizRequest(
                quizId ?: "",
                view.findViewById<EditText>(R.id.quiz_name).text.toString(),
                view.findViewById<CheckBox>(R.id.author_shown).isChecked,
                view.findViewById<CheckBox>(R.id.quiz_shown).isChecked,
                view.findViewById<EditText>(R.id.start_date).text.toString(),
                view.findViewById<EditText>(R.id.end_date).text.toString(),
                questions.map { question ->
                    QuestionRequest(
                        if (question.id.isNotEmpty()) question.id else "new-${
                            Random.nextInt(
                                100000
                            )
                        }",
                        question.question_text,
                        false,
                        question.answers.map { answer ->
                            AnswerRequest(
                                if (answer.id > 0) answer.id.toString() else "new-${
                                    Random.nextInt(
                                        100000
                                    )
                                }",
                                answer.text
                            )
                        }
                    )
                }
            )

            viewLifecycleOwner.lifecycleScope.launch {
                val response = RetrofitClient.apiService.editQuiz(
                    editRequest
                )
                if (response.result == "success") {
                    findNavController().navigate(R.id.accountFragment)
                    Toast.makeText(requireContext(), "Анкета обновлена!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}