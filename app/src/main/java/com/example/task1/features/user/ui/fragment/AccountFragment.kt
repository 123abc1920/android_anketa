package com.example.task1.features.user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Quiz
import com.example.task1.features.mainpage.ui.adapter.QuizAdapter
import com.example.task1.features.user.domain.Requests
import com.example.task1.features.user.ui.adapter.CreatedQuizAdapter
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private val requests = Requests()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        view.findViewById<Button>(R.id.create_btn).setOnClickListener {
            findNavController().navigate(R.id.createFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = requests.loadUserData()) {
                is Requests.Result.Success -> {
                    if (isAdded) {
                        val data = result.data
                        view.findViewById<TextView>(R.id.username).text =
                            data["username"].toString()
                        view.findViewById<TextView>(R.id.login).text = data["login"].toString()

                        val createdView = view.findViewById<RecyclerView>(R.id.created_recycle)
                        val createdQuizzes =
                            data["createdQuizzes"] as? List<Quiz> ?: mutableListOf()
                        val createdAdapter = CreatedQuizAdapter(createdQuizzes.toMutableList())
                        createdView.adapter = createdAdapter
                        createdView.layoutManager = LinearLayoutManager(requireContext())

                        val doneView = view.findViewById<RecyclerView>(R.id.done_recycle)
                        val doneQuizzes = data["doneQuizzes"] as? List<Quiz> ?: mutableListOf()
                        val doneAdapter = QuizAdapter(doneQuizzes.toMutableList())
                        doneView.adapter = doneAdapter
                        doneView.layoutManager = LinearLayoutManager(requireContext())
                    }
                }

                is Requests.Result.Error -> {
                    if (isAdded) {
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
            }
        }

        return view
    }
}