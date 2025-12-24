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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.AnswerRequest
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.data.database.responses.Question
import com.example.task1.data.database.responses.QuestionRequest
import com.example.task1.common.initdatepickers.InitDatePickers
import com.example.task1.common.toasts.showToast
import com.example.task1.features.editcreate.domain.EditRequests
import com.example.task1.features.editcreate.ui.adapter.EditQuizAdapter
import com.example.task1.features.editcreate.ui.vm.EditVM
import org.koin.android.ext.android.inject
import kotlin.random.Random
import kotlin.toString

class EditQuizFragment : Fragment() {

    private lateinit var createdQuestionView: RecyclerView
    private lateinit var createdQuestionAdapter: EditQuizAdapter

    private var questions = mutableListOf<Question>()

    private val requests: EditRequests by inject()
    private val viewModel: EditVM by viewModels()

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

        viewModel.quizData.observe(viewLifecycleOwner) { quizData ->
            if (quizData != null && isAdded) {
                view.findViewById<TextView>(R.id.quiz_name).text =
                    quizData.get("quizName").toString()
                view.findViewById<EditText>(R.id.start_date)
                    .setText(quizData.get("startData").toString())
                view.findViewById<EditText>(R.id.end_date)
                    .setText(quizData.get("endData").toString())
                val questionList = quizData.get("questionList") as? List<Question>
                if (questionList != null) {
                    createdQuestionAdapter.updateQuestions(questionList.toMutableList())
                }
            }
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success != null && isAdded) {
                if (success) {
                    showToast(requireContext(), "Анкета обновлена!")
                    findNavController().navigate(R.id.accountFragment)
                } else {
                    showToast(requireContext(), "Ошибка обновления анкеты")
                }
                viewModel.clearSaveResult()
            }
        }

        viewModel.loadQuizData(requests, quizId)

        view.findViewById<Button>(R.id.add_question_btn).setOnClickListener {
            createdQuestionAdapter.addQuestion()
        }

        var startDate = view.findViewById<EditText>(R.id.start_date)
        InitDatePickers.InitDatePicker(startDate, requireContext())

        var endDate = view.findViewById<EditText>(R.id.end_date)
        InitDatePickers.InitDatePicker(endDate, requireContext())

        view.findViewById<Button>(R.id.send_created_quiz).setOnClickListener {
            val quizIdArg = arguments?.getString("quizId") ?: ""
            val editQuiz = createEditQuiz(quizIdArg, view)

            viewModel.saveQuiz(requests, editQuiz)
        }

        return view
    }

    private fun createEditQuiz(quizId: String, view: View): EditQuizRequest {
        val currentQuestions = createdQuestionAdapter.getQuestions()

        return EditQuizRequest(
            quizId,
            view.findViewById<EditText>(R.id.quiz_name).text.toString(),
            view.findViewById<CheckBox>(R.id.author_shown).isChecked,
            view.findViewById<CheckBox>(R.id.quiz_shown).isChecked,
            view.findViewById<EditText>(R.id.start_date).text.toString(),
            view.findViewById<EditText>(R.id.end_date).text.toString(),
            currentQuestions.map { question ->
                QuestionRequest(
                    if (question.id.isNotEmpty()) question.id else "new-${Random.nextInt(100000)}",
                    question.question_text,
                    true,
                    question.answers.map { answer ->
                        AnswerRequest(
                            (if (answer.id != null) answer.id else "new-${Random.nextInt(100000)}").toString(),
                            answer.text
                        )
                    }
                )
            }
        )
    }
}