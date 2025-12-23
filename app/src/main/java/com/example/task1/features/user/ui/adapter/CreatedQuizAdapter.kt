package com.example.task1.features.user.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Quiz
import androidx.navigation.findNavController
import com.example.task1.domain.dialogs.showConfirmDialog
import com.example.task1.features.user.domain.Requests
import kotlinx.coroutines.launch

class CreatedQuizAdapter(
    private var quizzes: MutableList<Quiz>?
) : RecyclerView.Adapter<CreatedQuizAdapter.QuizViewHolder>() {

    val navigation = com.example.task1.features.user.domain.Navigation()
    val requests = Requests()

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
        holder.date.text = formatDate(quiz?.end_date)

        holder.runQuiz.setOnClickListener {
            quiz?.crypted_link?.let { quizId ->
                navigation.runQuiz(holder.itemView.findNavController(), quizId)
            }
        }
        holder.watchQuiz.setOnClickListener {
            quiz?.crypted_link?.let { quizId ->
                navigation.watchQuiz(holder.itemView.findNavController(), quizId)
            }

        }

        holder.deleteBtn.setOnClickListener {
            showConfirmDialog(
                holder.context,
                "Удалить эту анкету?",
                "Это действие нельзя будет отменить",
                { result ->
                    if (result) {
                        deleteQuiz(
                            position,
                            holder.itemView.findViewTreeLifecycleOwner(),
                            holder.itemView.context
                        )
                    }
                })
        }

        holder.editBtn.setOnClickListener {
            quiz?.crypted_link?.let { quizId ->
                navigation.editQuiz(
                    holder.itemView.findNavController(),
                    quiz.crypted_link.toString()
                )
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

    fun deleteQuiz(position: Int, owner: LifecycleOwner?, context: Context) {
        if (owner != null) {
            val adapterPosition = position
            if (adapterPosition != RecyclerView.NO_POSITION) {
                owner.lifecycleScope.launch {
                    val result = requests.deleteQuiz(context, quizzes?.get(position)?.id)
                    if (result) {
                        quizzes?.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
            }
        }
    }
}