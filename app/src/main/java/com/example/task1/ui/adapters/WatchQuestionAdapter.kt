package com.example.task1.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.QuestionInWatch

class WatchQuestionAdapter(
    private var questions: List<QuestionInWatch>?
) : RecyclerView.Adapter<WatchQuestionAdapter.WatchQuestionViewHolder>() {

    class WatchQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText = itemView.findViewById<TextView>(R.id.question_name)
        val answersView = itemView.findViewById<RecyclerView>(R.id.answers_view)
        var answersAdapter = UsersAdapter(emptyList())

        init {
            answersAdapter = UsersAdapter(emptyList())
            answersView.adapter = answersAdapter
            answersView.layoutManager = LinearLayoutManager(itemView.context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchQuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.watch_question, parent, false)

        return WatchQuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchQuestionViewHolder, position: Int) {
        val question = questions?.get(position)

        holder.questionText.text = question?.question_text

        if (question?.statistics?.size != 0) {
            val statics = mutableListOf<String>()
            for (i in ((0..((question?.answers?.size?.minus(1)) ?: 0)))) {
                statics.add(question?.answers[i] + " = " + question?.statistics!!?.get(i).toString())
            }
            holder.answersAdapter = UsersAdapter(statics)
        } else {
            holder.answersAdapter = UsersAdapter(question.answers)
        }

        holder.answersView.adapter = holder.answersAdapter
    }

    override fun getItemCount(): Int = questions?.size ?: 0

    fun updateQuizzes(newQuestions: List<QuestionInWatch>) {
        this.questions = newQuestions
        notifyDataSetChanged()
    }

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) "не указана"
        else {
            dateString
        }
    }
}