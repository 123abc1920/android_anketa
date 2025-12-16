package com.example.task1.ui.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.responses.ResultResponse
import kotlinx.coroutines.launch

class CreatedQuizAdapter(
    private var quizzes: MutableList<Quiz>?,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CreatedQuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizName: TextView = itemView.findViewById(R.id.quiz_name)
        val authorName: TextView = itemView.findViewById(R.id.author_name)
        val date: TextView = itemView.findViewById(R.id.data)
        val runQuiz: Button = itemView.findViewById(R.id.run_btn)
        val watchQuiz: Button = itemView.findViewById(R.id.watch_btn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.delete_quiz)
        val editBtn: ImageButton = itemView.findViewById(R.id.edit_quiz)
        val context = itemView.context;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.created_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes?.get(position)

        holder.quizName.text = quiz?.quiz_name
        holder.authorName.text = "${quiz?.author_name}"
        holder.date.text = "${formatDate(quiz?.end_date)}"

        holder.runQuiz.setOnClickListener {
            quiz?.crypted_link?.let { quizId ->
                val bundle = Bundle().apply {
                    putString("quizId", quizId.toString())
                }
                findNavController(holder.itemView).navigate(R.id.quizFragment, bundle)
            }
        }
        holder.watchQuiz.setOnClickListener {
            quiz?.crypted_link?.let { quizId ->
                val bundle = Bundle().apply {
                    putString("quizId", quizId.toString())
                }
                findNavController(holder.itemView).navigate(R.id.watchFragment, bundle)
            }
        }

        holder.deleteBtn.setOnClickListener {
            val adapterPosition = position
            if (adapterPosition != RecyclerView.NO_POSITION) {
                var response: ResultResponse = ResultResponse("unsuccess")
                lifecycleOwner.lifecycleScope.launch {
                    response = RetrofitClient.apiService.deleteQuiz(mapOf("id" to quiz?.id))
                    if (response.result == "success") {
                        Toast.makeText(holder.context, "Анкета удалена!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                quizzes?.remove(quiz)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int = quizzes?.size ?: 0

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) "не указана"
        else {
            dateString
        }
    }
}