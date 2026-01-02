package com.example.task1.features.mainpage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.requests.Filter
import com.example.task1.data.database.requests.SearchQuizRequest
import com.example.task1.common.initdatepickers.InitDatePickers
import com.example.task1.common.toasts.showToast
import com.example.task1.features.mainpage.domain.MainNavigate
import com.example.task1.features.mainpage.domain.MainRequests
import com.example.task1.features.mainpage.ui.adapter.QuizAdapter
import com.example.task1.features.mainpage.ui.vm.MainVM
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter

    private var currentPage = 1
    private var isSearch = false
    private var maxPage = 1

    private val requests: MainRequests by inject()
    private val navigate: MainNavigate by inject()

    private val mainVM: MainVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val mainScroll = view.findViewById<NestedScrollView>(R.id.main_scroll)

        mainVM.quizzesData.observe(viewLifecycleOwner) { quizzes ->
            if (isAdded) {
                quizAdapter.updateQuizzes(quizzes.toMutableList())
            }
        }

        mainVM.max.observe(viewLifecycleOwner) { max ->
            if (isAdded) {
                maxPage = max
            }
        }

        if (!isSearch) {
            mainVM.loadQuizzes(requests, currentPage)
        }

        recyclerView = view.findViewById(R.id.quiz_view)
        quizAdapter = QuizAdapter(mutableListOf())
        recyclerView.adapter = quizAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fun load() {
            if (isSearch) {
                mainVM.searchQuizzes(requests, createSearchQuizRequest(view))
            } else {
                mainVM.loadQuizzes(requests, currentPage)
            }
            mainScroll.smoothScrollTo(0, 0)
        }

        val prevBtn = view.findViewById<ImageButton>(R.id.prev)
        prevBtn.setOnClickListener {
            currentPage -= 1
            if (currentPage <= 0) {
                currentPage = 1
            }
            load()
        }
        val nextBtn = view.findViewById<ImageButton>(R.id.next)
        nextBtn.setOnClickListener {
            currentPage += 1
            if (currentPage > maxPage) {
                currentPage = maxPage
            }
            load()
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
            navigate.runQuiz(findNavController(), quizLink.text.toString())
        }
        view.findViewById<Button>(R.id.watch_quiz_link).setOnClickListener {
            navigate.watchQuiz(findNavController(), quizLink.text.toString())
        }

        view.findViewById<ImageButton>(R.id.find_btn).setOnClickListener {
            currentPage = 1
            isSearch = true
            mainVM.searchQuizzes(requests, createSearchQuizRequest(view))
            showToast(requireContext(), "Результаты получены")
        }

        var startDate = view.findViewById<EditText>(R.id.start_date)
        InitDatePickers.InitDatePicker(startDate, requireContext())

        var endDate = view.findViewById<EditText>(R.id.end_date)
        InitDatePickers.InitDatePicker(endDate, requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createFilter(
        inName: Boolean,
        inAuthor: Boolean,
        ended: Boolean,
        notEnded: Boolean,
        nearEnded: Boolean,
        nearNotEnded: Boolean,
        answerCount: String,
        startTime: String,
        endTime: String
    ): Filter {
        return Filter(
            inName,
            inAuthor,
            ended,
            notEnded,
            nearEnded,
            nearNotEnded,
            answerCount,
            startTime,
            endTime
        )
    }

    private fun createSearchQuizRequest(view: View): SearchQuizRequest {
        return SearchQuizRequest(
            currentPage,
            12,
            view.findViewById<EditText>(R.id.find_edit_text).text.toString(),
            createFilter(
                view.findViewById<CheckBox>(R.id.in_name).isChecked,
                view.findViewById<CheckBox>(R.id.in_author).isChecked,
                view.findViewById<CheckBox>(R.id.ended).isChecked,
                view.findViewById<CheckBox>(R.id.not_ended).isChecked,
                view.findViewById<CheckBox>(R.id.near_ended).isChecked,
                view.findViewById<CheckBox>(R.id.near_not_ended).isChecked,
                view.findViewById<EditText>(R.id.answers_count).text.toString().trim(),
                view.findViewById<EditText>(R.id.start_date).text.toString().trim(),
                view.findViewById<EditText>(R.id.end_date).text.toString().trim()
            )
        )
    }
}