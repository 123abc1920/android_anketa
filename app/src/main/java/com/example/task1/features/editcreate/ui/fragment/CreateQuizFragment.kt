package com.example.task1.features.editcreate.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Question
import com.example.task1.data.database.models.AnswerInCreateQuiz
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.domain.initdatepickers.InitDatePickers
import com.example.task1.features.editcreate.domain.Requests
import com.example.task1.features.editcreate.ui.adapter.CreateQuizAdapter

class CreateQuizFragment : Fragment() {
    private lateinit var createdQuestionView: RecyclerView
    private lateinit var createdQuestionAdapter: CreateQuizAdapter

    private var questions = mutableListOf<QuestionInQuiz>()

    private val requests = Requests()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("CutPasteId")
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
            createdQuestionAdapter.addQuestion()
        }

        var startDate = view.findViewById<EditText>(R.id.start_date)
        InitDatePickers.InitDatePicker(startDate, requireContext())

        var endDate = view.findViewById<EditText>(R.id.end_date)
        InitDatePickers.InitDatePicker(endDate, requireContext())

        view.findViewById<Button>(R.id.send_created_quiz).setOnClickListener {
            val createdQuiz = CreateQuizResponse(
                view.findViewById<EditText>(R.id.quiz_name).text.toString(),
                view.findViewById<CheckBox>(R.id.author_shown).isChecked,
                view.findViewById<CheckBox>(R.id.quiz_shown).isChecked,
                view.findViewById<EditText>(R.id.start_date).text.toString(),
                view.findViewById<EditText>(R.id.end_date).text.toString(),
                collectQuestions(createdQuestionView)
            )

            requests.createQuiz(
                viewLifecycleOwner,
                findNavController(),
                requireContext(),
                createdQuiz
            )
        }

        return view
    }

    private fun collectQuestions(createdQuestionView: RecyclerView): MutableList<Question> {
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
}