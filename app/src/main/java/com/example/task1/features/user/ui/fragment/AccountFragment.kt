package com.example.task1.features.user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Quiz
import com.example.task1.features.mainpage.ui.adapter.QuizAdapter
import com.example.task1.features.user.domain.Requests
import com.example.task1.features.user.ui.adapter.CreatedQuizAdapter

class AccountFragment : Fragment() {

    private val requests = Requests()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        view.findViewById<Button>(R.id.create_btn).setOnClickListener {
            findNavController().navigate(R.id.createFragment)
        }

        val result = requests.loadUserData(viewLifecycleOwner, findNavController())
        view.findViewById<TextView>(R.id.username).text = result.get("username").toString()
        view.findViewById<TextView>(R.id.login).text = result.get("login").toString()

        val createdView = view.findViewById<RecyclerView>(R.id.created_recycle)
        val createdAdapter = CreatedQuizAdapter(result.get("createdQuizzes") as MutableList<Quiz>)
        createdView.adapter = createdAdapter
        createdView.layoutManager = LinearLayoutManager(requireContext())

        val doneView = view.findViewById<RecyclerView>(R.id.done_recycle)
        val doneAdapter = QuizAdapter(result.get("doneQuizzes") as MutableList<Quiz>)
        doneView.adapter = doneAdapter
        doneView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}