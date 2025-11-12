package com.example.task1.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = view.findViewById(R.id.quiz_view)
        quizAdapter = QuizAdapter(emptyList())
        recyclerView.adapter = quizAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadQuizzes()
    }

    private fun loadQuizzes() {
        lifecycleScope.launch {
            try {
                val quizzes = RetrofitClient.apiService.getQuizzes()

                quizAdapter = QuizAdapter(quizzes)
                recyclerView.adapter = quizAdapter

            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки анкет", e)
            }
        }
    }
}