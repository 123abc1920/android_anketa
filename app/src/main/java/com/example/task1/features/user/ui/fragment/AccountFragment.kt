package com.example.task1.features.user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Quiz
import com.example.task1.features.mainpage.ui.adapter.QuizAdapter
import com.example.task1.features.user.domain.UserNavigation
import com.example.task1.features.user.domain.UserRequests
import com.example.task1.features.user.ui.adapter.CreatedQuizAdapter
import org.koin.android.ext.android.inject

class AccountFragment : Fragment() {

    private val requests: UserRequests by inject()
    private val navigation: UserNavigation by inject()

    private val accountVM: AccountVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        view.findViewById<Button>(R.id.create_btn).setOnClickListener {
            findNavController().navigate(R.id.createFragment)
        }

        accountVM.userData.observe(viewLifecycleOwner) { result ->
            if (result != null && isAdded) {
                view.findViewById<TextView>(R.id.username).text = result["username"].toString()
                view.findViewById<TextView>(R.id.login).text = result["login"].toString()

                val createdView = view.findViewById<RecyclerView>(R.id.created_recycle)
                val createdQuizzes = result["createdQuizzes"] as? List<Quiz> ?: mutableListOf()
                val createdAdapter =
                    CreatedQuizAdapter(createdQuizzes.toMutableList(), requests, navigation)
                createdView.adapter = createdAdapter
                createdView.layoutManager = LinearLayoutManager(requireContext())

                val doneView = view.findViewById<RecyclerView>(R.id.done_recycle)
                val doneQuizzes = result["doneQuizzes"] as? List<Quiz> ?: mutableListOf()
                val doneAdapter = QuizAdapter(doneQuizzes.toMutableList())
                doneView.adapter = doneAdapter
                doneView.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        accountVM.error.observe(viewLifecycleOwner) { hasError ->
            if (hasError && isAdded) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        accountVM.loadUserData(requests)

        return view
    }
}