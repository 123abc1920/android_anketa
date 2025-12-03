package com.example.task1.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.ui.adapters.QuizAdapter
import com.example.task1.data.api.RetrofitClient
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter

    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = view.findViewById(R.id.quiz_view)
        quizAdapter = QuizAdapter(emptyList())
        recyclerView.adapter = quizAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadQuizzes()

        val prevBtn = view.findViewById<ImageButton>(R.id.prev)
        prevBtn.setOnClickListener {
            currentPage += 1
            loadQuizzes()
        }
        val nextBtn = view.findViewById<ImageButton>(R.id.next)
        nextBtn.setOnClickListener {
            currentPage -= 1
            loadQuizzes()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadQuizzes() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getQuizzes(currentPage, 12)

                quizAdapter = QuizAdapter(response.quizes)
                recyclerView.adapter = quizAdapter
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки анкет", e)
            }
        }
    }
}