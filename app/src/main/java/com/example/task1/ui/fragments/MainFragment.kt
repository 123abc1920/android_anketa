package com.example.task1.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

        val expandableView = view.findViewById<ConstraintLayout>(R.id.open_special)
        val expandBtn = view.findViewById<ImageButton>(R.id.expand_btn)
        expandBtn.setOnClickListener {
            expandBtn.visibility = View.GONE
            expandableView.visibility = View.VISIBLE
        }

        view.findViewById<ImageButton>(R.id.not_expand_btn).setOnClickListener {
            expandBtn.visibility = View.VISIBLE
            expandableView.visibility = View.GONE
        }

        val quizLink = view.findViewById<EditText>(R.id.quiz_link)
        view.findViewById<Button>(R.id.run_quiz_link).setOnClickListener {
            var link = quizLink.text.toString()

            if (link != "") {
                val bundle = Bundle().apply {
                    putString("quizId", link)
                }
                findNavController().navigate(R.id.quizFragment, bundle)
            }
        }
        view.findViewById<Button>(R.id.watch_quiz_link).setOnClickListener {
            var link = quizLink.text.toString()

            if (link != "") {
                val bundle = Bundle().apply {
                    putString("quizId", link)
                }
                findNavController().navigate(R.id.watchFragment, bundle)
            }
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