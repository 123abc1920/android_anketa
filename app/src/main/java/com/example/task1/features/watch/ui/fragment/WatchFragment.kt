package com.example.task1.features.watch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.QuestionInWatch
import com.example.task1.domain.copy.copyToClip
import com.example.task1.domain.toasts.showToast
import com.example.task1.features.watch.domain.Requests
import com.example.task1.features.watch.ui.adapter.UsersAdapter
import com.example.task1.features.watch.ui.adapter.WatchQuestionAdapter

class WatchFragment : Fragment() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var questionsView: RecyclerView
    private lateinit var questionsAdapter: WatchQuestionAdapter

    private lateinit var link: String

    private val requests = Requests()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_watch, container, false)

        val quizId = arguments?.getString("quizId")

        var result = requests.loadQuestions(viewLifecycleOwner, quizId.toString())
        view.findViewById<TextView>(R.id.quiz_name).text = result.get("quizName").toString()
        view.findViewById<TextView>(R.id.start_data).text = result.get("startDate").toString()
        view.findViewById<TextView>(R.id.end_data).text = result.get("endDate").toString()
        view.findViewById<TextView>(R.id.author_name).text = result.get("authorName").toString()
        link = result.get("link").toString()

        questionsView = view.findViewById<RecyclerView>(R.id.questions_view)
        questionsAdapter =
            WatchQuestionAdapter(result.get("questionsList") as MutableList<QuestionInWatch>)
        questionsView.adapter = questionsAdapter
        questionsView.layoutManager = LinearLayoutManager(requireContext())

        usersRecyclerView = view.findViewById<RecyclerView>(R.id.users_list)
        usersAdapter = UsersAdapter(result.get("usersList") as MutableList<String>)
        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<ImageButton>(R.id.copy_link).setOnClickListener {
            copyToClip(requireContext(), link)
            showToast(requireContext(), "Ссылка скопирована!")
        }

        return view
    }
}