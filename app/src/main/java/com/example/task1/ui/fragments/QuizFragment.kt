package com.example.task1.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.requests.QuizRequest
import com.example.task1.domain.authorisation.getUserId
import com.example.task1.ui.adapters.QuestionAdapter
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {
    private lateinit var questionView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter

    private lateinit var link: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        questionView = view.findViewById<RecyclerView>(R.id.questions_view)
        questionAdapter = QuestionAdapter(emptyList())
        questionView.adapter = questionAdapter
        questionView.layoutManager = LinearLayoutManager(requireContext())

        val quizId = arguments?.getString("quizId")

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                quizId?.let {
                    val response = RetrofitClient.apiService.startAnketa(
                        "Bearer ${getUserId()}",
                        quizId
                    )

                    if (response.result == "success") {
                        val quizName = view.findViewById<TextView>(R.id.quiz_name)
                        quizName.text = response.quiz_name
                        val startData = view.findViewById<TextView>(R.id.start_data)
                        startData.text = response.start_date
                        val endData = view.findViewById<TextView>(R.id.end_data)
                        endData.text = response.end_date
                        val authorName = view.findViewById<TextView>(R.id.author_name)
                        authorName.text = response.author_name
                        link = response.crypted_link

                        questionAdapter = QuestionAdapter(response.questions_list)
                        questionView.adapter = questionAdapter
                    }

                    if (response.result == "end date") {
                        findNavController().navigate(R.id.mainFragment)
                        Toast.makeText(
                            requireContext(),
                            "Анкета уже завершена!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки анкеты", e)
                findNavController().navigate(R.id.accountFragment)
            }
        }

        val sendQuiz = view.findViewById<Button>(R.id.send_quiz)
        sendQuiz.setOnClickListener {
            val requestData = QuizRequest(
                quiz_id = quizId.toString(),
                questions = questionAdapter.getAnswers()
            )

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.sendQuiz(
                        "Bearer ${getUserId()}", requestData
                    )
                    findNavController().navigate(R.id.mainFragment)
                    Toast.makeText(requireContext(), "Анкета отправлена!", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Log.e("Ошибка", e.toString())
                }
            }
        }

        view.findViewById<ImageButton>(R.id.copy_link).setOnClickListener {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("", link))
            Toast.makeText(requireContext(), "Ссылка скопирована!", Toast.LENGTH_SHORT)
                .show()
        }

        return view
    }
}