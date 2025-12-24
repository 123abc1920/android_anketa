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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.commondomain.initdatepickers.InitDatePickers
import com.example.task1.commondomain.toasts.showToast
import com.example.task1.features.editcreate.domain.EditRequests
import com.example.task1.features.editcreate.ui.adapter.CreateQuizAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CreateQuizFragment : Fragment() {
    private lateinit var createdQuestionView: RecyclerView
    private lateinit var createdQuestionAdapter: CreateQuizAdapter

    private var questions = mutableListOf<QuestionInQuiz>()

    private val requests: EditRequests by inject()

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
                createdQuestionAdapter.getQuestions()
            )

            viewLifecycleOwner.lifecycleScope.launch {
                val success = requests.createQuiz(createdQuiz)
                if (success) {
                    showToast(requireContext(), "Анкета создана!")
                    findNavController().navigate(R.id.accountFragment)
                } else {
                    showToast(requireContext(), "Ошибка создания анкеты")
                }
            }
        }

        return view
    }
}