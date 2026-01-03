package com.example.task1.features.watch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.QuestionInWatch

class WatchQuestionAdapter(
    private var questions: MutableList<QuestionInWatch>?
) : RecyclerView.Adapter<WatchQuestionAdapter.WatchQuestionViewHolder>() {

    class WatchQuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText = itemView.findViewById<TextView>(R.id.question_name)
        val answersView = itemView.findViewById<RecyclerView>(R.id.answers_view)
        var answersAdapter = UsersAdapter(mutableListOf())

        init {
            answersAdapter = UsersAdapter(mutableListOf())
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
                statics.add(
                    question?.answers[i] + " = " + question?.statistics!!?.get(i).toString()
                )
            }
            holder.answersAdapter = UsersAdapter(statics)
        } else {
            holder.answersAdapter =
                UsersAdapter(question.answers.filterNotNull() as MutableList<String>?)
        }

        holder.answersView.adapter = holder.answersAdapter
    }

    override fun getItemCount(): Int = questions?.size ?: 0

    fun updateQuestions(newList: List<QuestionInWatch>) {
        questions?.clear()
        questions?.addAll(newList)
        notifyDataSetChanged()
    }
}