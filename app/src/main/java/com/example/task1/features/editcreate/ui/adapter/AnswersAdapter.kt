package com.example.task1.features.editcreate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Answer
import kotlin.collections.remove

class AnswersAdapter(
    private var answers: MutableList<Answer>?
) : RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder>() {

    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerText: EditText = itemView.findViewById(R.id.answer_text)
        val button: Button = itemView.findViewById(R.id.delete_answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.answer, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answers?.get(position)

        holder.answerText.setText(answer?.text.toString())

        holder.button.setOnClickListener {
            deleteAnswer(position)
        }
    }

    override fun getItemCount(): Int = answers?.size ?: 0

    fun addAnswer(id: Int = 1, questionId: Int = 2) {
        answers?.add(Answer(id, "", questionId))
        notifyDataSetChanged()
    }

    fun addAnswer(answer: Answer) {
        answers?.add(answer)
        notifyDataSetChanged()
    }

    fun deleteAnswer(position: Int) {
        answers?.removeAt(position)
        notifyItemRemoved(position)
    }

}