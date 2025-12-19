package com.example.task1.features.mainpage.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.requests.Filter
import com.example.task1.data.database.requests.SearchQuizRequest
import kotlinx.coroutines.launch
import java.util.Calendar

class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter

    private var currentPage = 1
    private var isSearch = false

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
            if (isSearch) {
                search(view)
            } else {
                loadQuizzes()
            }
        }
        val nextBtn = view.findViewById<ImageButton>(R.id.next)
        nextBtn.setOnClickListener {
            currentPage -= 1
            if (isSearch) {
                search(view)
            } else {
                loadQuizzes()
            }
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

        view.findViewById<ImageButton>(R.id.find_btn).setOnClickListener {
            currentPage = 1
            isSearch = true
            search(view)
        }

        var startDate = view.findViewById<EditText>(R.id.start_date)
        startDate.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val c = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(), { _, year, month, day ->
                        TimePickerDialog(
                            requireContext(), { _, hour, minute ->
                                val dateStr = String.format(
                                    "%04d-%02d-%02dT%02d:%02d",
                                    year, month + 1, day, hour, minute
                                )
                                startDate.setText(dateStr)
                            },
                            c.get(Calendar.HOUR_OF_DAY),
                            c.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            false
        }

        var endDate = view.findViewById<EditText>(R.id.end_date)
        endDate.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val c = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(), { _, year, month, day ->
                        TimePickerDialog(
                            requireContext(), { _, hour, minute ->
                                val dateStr = String.format(
                                    "%04d-%02d-%02dT%02d:%02d",  // Без секунд
                                    year, month + 1, day, hour, minute
                                )
                                endDate.setText(dateStr)
                            },
                            c.get(Calendar.HOUR_OF_DAY),
                            c.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            false
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

    private fun search(view: View) {
        val findText = view.findViewById<EditText>(R.id.find_edit_text)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.search(
                    SearchQuizRequest(
                        currentPage,
                        12,
                        findText.text.toString(),
                        Filter(
                            inName = view.findViewById<CheckBox>(R.id.in_name).isChecked,
                            inAuthor = view.findViewById<CheckBox>(R.id.in_author).isChecked,
                            ended = view.findViewById<CheckBox>(R.id.ended).isChecked,
                            notEnded = view.findViewById<CheckBox>(R.id.not_ended).isChecked,
                            nearEnded = view.findViewById<CheckBox>(R.id.near_ended).isChecked,
                            nearNotEnded = view.findViewById<CheckBox>(R.id.near_not_ended).isChecked,
                            answers = view.findViewById<EditText>(R.id.answers_count).text.toString()
                                .trim(),
                            startTime = view.findViewById<EditText>(R.id.start_date).text.toString()
                                .trim(),
                            endTime = view.findViewById<EditText>(R.id.end_date).text.toString()
                                .trim()
                        )
                    )
                )

                quizAdapter = QuizAdapter(response.quizes)
                recyclerView.adapter = quizAdapter
            } catch (e: Exception) {
                Log.e("SEARCH ERROR", "Ошибка поиска", e)
            }
        }
    }
}