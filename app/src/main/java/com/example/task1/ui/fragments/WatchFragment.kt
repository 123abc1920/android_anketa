package com.example.task1.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.ui.adapters.UsersAdapter
import com.example.task1.ui.adapters.WatchQuestionAdapter
import kotlinx.coroutines.launch

class WatchFragment : Fragment() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var questionsView: RecyclerView
    private lateinit var questionsAdapter: WatchQuestionAdapter

    private lateinit var link: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_watch, container, false)

        usersRecyclerView = view.findViewById<RecyclerView>(R.id.users_list)
        usersAdapter = UsersAdapter(emptyList())
        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        questionsView = view.findViewById<RecyclerView>(R.id.questions_view)
        questionsAdapter = WatchQuestionAdapter(emptyList())
        questionsView.adapter = questionsAdapter
        questionsView.layoutManager = LinearLayoutManager(requireContext())

        val quizId = arguments?.getString("quizId")

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                quizId?.let {
                    val response = RetrofitClient.apiService.startPreview(quizId)

                    val quizName = view.findViewById<TextView>(R.id.quiz_name)
                    quizName.text = response.quiz_name
                    val startData = view.findViewById<TextView>(R.id.start_data)
                    startData.text = response.start_date
                    val endData = view.findViewById<TextView>(R.id.end_data)
                    endData.text = response.end_date
                    val authorName = view.findViewById<TextView>(R.id.author_name)
                    authorName.text = response.author_name
                    link = response.crypted_link

                    questionsAdapter = WatchQuestionAdapter(response.questions_list)
                    questionsView.adapter = questionsAdapter

                    usersAdapter = UsersAdapter(response.users_list)
                    usersRecyclerView.adapter = usersAdapter
                }
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки превью анкеты", e)
            }
        }

        view.findViewById<ImageButton>(R.id.copy_link).setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("", link))
            Toast.makeText(requireContext(), "Ссылка скопирована!", Toast.LENGTH_SHORT)
                .show()
        }

        return view
    }
}