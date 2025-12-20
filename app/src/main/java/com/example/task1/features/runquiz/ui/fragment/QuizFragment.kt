package com.example.task1.features.runquiz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.data.database.requests.QuizRequest
import com.example.task1.domain.copy.copyToClip
import com.example.task1.domain.toasts.showToast
import com.example.task1.features.runquiz.domain.Requests
import com.example.task1.features.runquiz.ui.adapter.QuestionAdapter

class QuizFragment : Fragment() {
    private lateinit var questionView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter

    private lateinit var link: String

    private val requests = Requests()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        questionView = view.findViewById<RecyclerView>(R.id.questions_view)
        questionAdapter = QuestionAdapter(mutableListOf())
        questionView.adapter = questionAdapter
        questionView.layoutManager = LinearLayoutManager(requireContext())

        val quizId = arguments?.getString("quizId")

        val result =
            requests.loadQuiz(viewLifecycleOwner, findNavController(), requireContext(), quizId)
        view.findViewById<TextView>(R.id.quiz_name).text = result.get("quizName").toString()
        view.findViewById<TextView>(R.id.start_data).text = result.get("startDate").toString()
        view.findViewById<TextView>(R.id.end_data).text = result.get("endDate").toString()
        view.findViewById<TextView>(R.id.author_name).text = result.get("authorName").toString()
        questionAdapter.updateQuizzes(result.get("questionsList") as MutableList<QuestionInQuiz>)
        link = result.get("cryptedLink").toString()

        val sendQuiz = view.findViewById<Button>(R.id.send_quiz)
        sendQuiz.setOnClickListener {
            requests.sendQuiz(
                viewLifecycleOwner, requireContext(), findNavController(), QuizRequest(
                    quiz_id = quizId.toString(),
                    questions = questionAdapter.getAnswers()
                )
            )
        }

        view.findViewById<ImageButton>(R.id.copy_link).setOnClickListener {
            copyToClip(requireContext(), link)
            showToast(requireContext(), "Ссылка скопирована!")
        }

        return view
    }
}