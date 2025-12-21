package com.example.task1.features.editcreate.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.AnswerRequest
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.data.database.responses.Question
import com.example.task1.data.database.responses.QuestionRequest
import com.example.task1.domain.initdatepickers.InitDatePickers
import com.example.task1.features.editcreate.domain.Requests
import com.example.task1.features.editcreate.ui.adapter.EditQuizAdapter
import kotlin.random.Random
import kotlin.toString

class EditQuizFragment : Fragment() {

    private lateinit var createdQuestionView: RecyclerView
    private lateinit var createdQuestionAdapter: EditQuizAdapter

    private var questions = mutableListOf<Question>()

    private val requests = Requests()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("CutPasteId")
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

        val quizData = requests.loadQuizData(viewLifecycleOwner, findNavController(), quizId)

        view.findViewById<TextView>(R.id.quiz_name).text = quizData.get("quizName").toString()
        view.findViewById<EditText>(R.id.start_date).setText(quizData.get("startDate").toString())
        view.findViewById<EditText>(R.id.end_date).setText(quizData.get("endDate").toString())
        createdQuestionAdapter.updateQuestions(quizData.get("questionList") as MutableList<Question>)

        view.findViewById<Button>(R.id.add_question_btn).setOnClickListener {
            createdQuestionAdapter.addQuestion()
        }

        var startDate = view.findViewById<EditText>(R.id.start_date)
        InitDatePickers.InitDatePicker(startDate, requireContext())

        var endDate = view.findViewById<EditText>(R.id.end_date)
        InitDatePickers.InitDatePicker(endDate, requireContext())

        view.findViewById<Button>(R.id.send_created_quiz).setOnClickListener {
            val editQuiz = createEditQuiz(quizId.toString(), view)
            requests.sendCreatedQuiz(
                viewLifecycleOwner,
                requireContext(),
                findNavController(),
                editQuiz
            )
        }

        return view
    }

    private fun createEditQuiz(quizId: String, view: View): EditQuizRequest {
        return EditQuizRequest(
            quizId ?: "",
            view.findViewById<EditText>(R.id.quiz_name).text.toString(),
            view.findViewById<CheckBox>(R.id.author_shown).isChecked,
            view.findViewById<CheckBox>(R.id.quiz_shown).isChecked,
            view.findViewById<EditText>(R.id.start_date).text.toString(),
            view.findViewById<EditText>(R.id.end_date).text.toString(),
            questions.map { question ->
                QuestionRequest(
                    if (question.id.isNotEmpty()) question.id else "new-${
                        Random.Default.nextInt(
                            100000
                        )
                    }",
                    question.question_text,
                    false,
                    question.answers.map { answer ->
                        AnswerRequest(
                            if (answer.id > 0) answer.id.toString() else "new-${
                                Random.Default.nextInt(
                                    100000
                                )
                            }",
                            answer.text
                        )
                    }
                )
            }
        )
    }
}