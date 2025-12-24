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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.common.initdatepickers.InitDatePickers
import com.example.task1.common.toasts.showToast
import com.example.task1.data.database.models.AnswerInQuiz
import com.example.task1.data.database.models.QuestionInCreateQuiz
import com.example.task1.features.editcreate.domain.EditRequests
import com.example.task1.features.editcreate.ui.adapter.AnswersAdapter.AnswerViewHolder
import com.example.task1.features.editcreate.ui.adapter.CreateQuizAdapter
import com.example.task1.features.editcreate.ui.adapter.CreateQuizAdapter.CreateQuizViewHolder
import org.koin.android.ext.android.inject

class CreateQuizFragment : Fragment() {
    private lateinit var createdQuestionView: RecyclerView
    private lateinit var createdQuestionAdapter: CreateQuizAdapter

    private var questions = mutableListOf<QuestionInCreateQuiz>()

    private val requests: EditRequests by inject()

    private val createVM: CreateVM by viewModels()

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

        createVM.saveResult.observe(viewLifecycleOwner) { success ->
            if (success != null && isAdded) {
                if (success) {
                    showToast(requireContext(), "Анкета создана!")
                    findNavController().navigate(R.id.accountFragment)
                } else {
                    showToast(requireContext(), "Ошибка создания анкеты")
                }
                createVM.clearSaveResult()
            }
        }

        view.findViewById<Button>(R.id.send_created_quiz).setOnClickListener {
            val questionsWithAnswers = mutableListOf<QuestionInCreateQuiz>()

            for (i in 0 until createdQuestionView.childCount) {
                val child = createdQuestionView.getChildAt(i)
                val viewHolder =
                    createdQuestionView.getChildViewHolder(child) as? CreateQuizViewHolder

                viewHolder?.let { vh ->
                    val questionText = vh.questionText.text.toString()
                    val isRequired = vh.isRequired.isChecked

                    // Собираем ответы из answersView
                    val answers = mutableListOf<AnswerInQuiz>()
                    for (j in 0 until vh.answersView.childCount) {
                        val answerChild = vh.answersView.getChildAt(j)
                        val answerHolder =
                            vh.answersView.getChildViewHolder(answerChild) as? AnswerViewHolder

                        answerHolder?.let { ah ->
                            val answerText = ah.answerText.text.toString()
                            if (answerText.isNotEmpty()) {
                                answers.add(AnswerInQuiz(answerText, ""))
                            }
                        }
                    }

                    questionsWithAnswers.add(
                        QuestionInCreateQuiz(
                            text = questionText,
                            id = "",
                            is_required = isRequired,
                            answers = answers,
                            selectedAnswerId = null,
                            selectedAnswerText = null
                        )
                    )
                }
            }

            val createdQuiz = CreateQuizResponse(
                view.findViewById<EditText>(R.id.quiz_name).text.toString(),
                view.findViewById<CheckBox>(R.id.author_shown).isChecked,
                view.findViewById<CheckBox>(R.id.quiz_shown).isChecked,
                view.findViewById<EditText>(R.id.start_date).text.toString(),
                view.findViewById<EditText>(R.id.end_date).text.toString(),
                questionsWithAnswers
            )

            createVM.createQuiz(requests, createdQuiz)
        }

        return view
    }
}