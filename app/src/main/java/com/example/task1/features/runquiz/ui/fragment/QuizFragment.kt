package com.example.task1.features.runquiz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.models.QuestionInQuiz
import com.example.task1.data.database.requests.QuizRequest
import com.example.task1.common.copy.copyToClip
import com.example.task1.common.toasts.showToast
import com.example.task1.features.runquiz.domain.RunRequests
import com.example.task1.features.runquiz.ui.adapter.QuestionAdapter
import com.example.task1.features.runquiz.vm.QuizVM
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class QuizFragment : Fragment() {
    private lateinit var questionView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var link: String

    private val requests: RunRequests by inject()

    private val quizVM: QuizVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
        val quizId = arguments?.getString("quizId")

        questionView = view.findViewById<RecyclerView>(R.id.questions_view)
        questionAdapter = QuestionAdapter(mutableListOf())
        questionView.adapter = questionAdapter
        questionView.layoutManager = LinearLayoutManager(requireContext())

        quizVM.quizData.observe(viewLifecycleOwner) { result ->
            if (result != null && isAdded) {
                if (result["shouldNavigate"] == true) {
                    findNavController().navigate(R.id.mainFragment)
                } else {
                    view.findViewById<TextView>(R.id.quiz_name).text = result["quizName"].toString()
                    view.findViewById<TextView>(R.id.start_data).text =
                        result["startDate"].toString()
                    view.findViewById<TextView>(R.id.end_data).text = result["endDate"].toString()
                    view.findViewById<TextView>(R.id.author_name).text =
                        result["authorName"].toString()

                    val questions = result["questionsList"] as? List<QuestionInQuiz>
                    if (questions != null) {
                        questionAdapter.updateQuizzes(questions.toMutableList())
                    }

                    link = result["cryptedLink"].toString()
                }
            }
        }

        val sendQuiz = view.findViewById<Button>(R.id.send_quiz)
        sendQuiz.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val success = requests.sendQuiz(
                    QuizRequest(
                        quiz_id = quizId ?: "",
                        questions = questionAdapter.getAnswers()
                    )
                )

                if (success) {
                    showToast(requireContext(), "Анкета отправлена!")
                    findNavController().navigate(R.id.mainFragment)
                }
            }
        }

        quizVM.loadQuiz(requests, quizId.toString())

        view.findViewById<ImageButton>(R.id.copy_link).setOnClickListener {
            copyToClip(requireContext(), link)
            showToast(requireContext(), "Ссылка скопирована!")
        }

        return view
    }
}