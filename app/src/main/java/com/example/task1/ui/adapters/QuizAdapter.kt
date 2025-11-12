package com.example.task1.ui.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class QuizAdapter(
    private var quizzes: List<Quiz>?
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizName: TextView = itemView.findViewById(R.id.quiz_name)
        val authorName: TextView = itemView.findViewById(R.id.author_name)
        val date: TextView = itemView.findViewById(R.id.data)
        val runQuiz: Button = itemView.findViewById(R.id.run_btn)
        val watchQuiz: Button = itemView.findViewById(R.id.watch_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes?.get(position)

        holder.quizName.text = quiz?.quiz_name
        holder.authorName.text = "${quiz?.author_name}"
        holder.date.text = "${formatDate(quiz?.end_date)}"

        holder.runQuiz.setOnClickListener {
            quiz?.id?.let { quizId ->
                val bundle = Bundle().apply {
                    putString("quizId", quizId.toString())
                }
                findNavController(holder.itemView).navigate(R.id.quizFragment, bundle)
            }
        }
        holder.watchQuiz.setOnClickListener {
            Log.i("Quiz", "Watch")
        }
    }

    override fun getItemCount(): Int = quizzes?.size ?: 0

    fun updateQuizzes(newQuizzes: List<Quiz>) {
        this.quizzes = newQuizzes
        notifyDataSetChanged()
    }

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) "не указана"
        else {
            dateString
        }
    }
}