package com.example.task1.features.watch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.QuestionInWatch
import com.example.task1.commondomain.copy.copyToClip
import com.example.task1.commondomain.toasts.showToast
import com.example.task1.features.watch.domain.WatchRequests
import com.example.task1.features.watch.ui.adapter.UsersAdapter
import com.example.task1.features.watch.ui.adapter.WatchQuestionAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class WatchFragment : Fragment() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var questionsView: RecyclerView
    private lateinit var questionsAdapter: WatchQuestionAdapter

    private lateinit var link: String

    private val requests: WatchRequests by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_watch, container, false)

        val quizId = arguments?.getString("quizId")

        questionsView = view.findViewById<RecyclerView>(R.id.questions_view)
        questionsAdapter = WatchQuestionAdapter(mutableListOf())
        questionsView.adapter = questionsAdapter
        questionsView.layoutManager = LinearLayoutManager(requireContext())

        usersRecyclerView = view.findViewById<RecyclerView>(R.id.users_list)
        usersAdapter = UsersAdapter(mutableListOf())
        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            if (quizId != null) {
                val result = requests.loadQuestions(quizId)

                if (isAdded) {
                    view.findViewById<TextView>(R.id.quiz_name).text = result["quizName"].toString()
                    view.findViewById<TextView>(R.id.start_data).text =
                        result["startDate"].toString()
                    view.findViewById<TextView>(R.id.end_data).text = result["endDate"].toString()
                    view.findViewById<TextView>(R.id.author_name).text =
                        result["authorName"].toString()
                    link = result["link"].toString()

                    val questionsList = result["questionsList"] as? List<QuestionInWatch>
                    if (questionsList != null) {
                        questionsAdapter.updateQuestions(questionsList.toMutableList())
                    }

                    val usersList = result["usersList"] as? List<String>
                    if (usersList != null) {
                        usersAdapter.updateUsers(usersList.toMutableList())
                    }
                }
            }
        }

        view.findViewById<ImageButton>(R.id.copy_link).setOnClickListener {
            copyToClip(requireContext(), link)
            showToast(requireContext(), "Ссылка скопирована!")
        }

        return view
    }
}