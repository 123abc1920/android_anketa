package com.example.task1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Answer
import com.example.task1.data.api.models.Question
import com.example.task1.data.database.models.AnswerInQuiz
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.ui.adapters.CreateQuizAdapter
import com.example.task1.ui.adapters.QuestionAdapter

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

        return view
    }
}