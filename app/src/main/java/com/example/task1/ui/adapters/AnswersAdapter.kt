package com.example.task1.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Answer
import com.example.task1.data.database.models.QuestionAnswer
import com.example.task1.data.database.models.QuestionInQuiz

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
            answers?.remove(answer)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = answers?.size ?: 0

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) "не указана"
        else {
            dateString
        }
    }
}