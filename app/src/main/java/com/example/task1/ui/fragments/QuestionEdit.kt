package com.example.task1.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.responses.Question

class QuestionEdit(
    private var questions: List<Question>?
) : RecyclerView.Adapter<QuestionEdit.QuestionEditViewHolder>() {

    class QuestionEditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionName: TextView = itemView.findViewById(R.id.question_name)
        val questionAnswer: EditText = itemView.findViewById(R.id.answer_text)
        val answersRadioGroup: RadioGroup = itemView.findViewById(R.id.answers_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionEditViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question, parent, false)
        return QuestionEditViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionEditViewHolder, position: Int) {
        val question = questions?.get(position)

        holder.questionName.text = question?.question_text

        holder.answersRadioGroup.removeAllViews()

        if (!question?.answers.isNullOrEmpty()) {
            holder.answersRadioGroup.visibility = View.VISIBLE
            holder.questionAnswer.visibility = View.GONE

            question.answers.forEach { answer ->
                val radioButton = RadioButton(holder.itemView.context).apply {
                    text = answer.text
                    tag = answer.id
                    id = View.generateViewId()
                    textSize = 16f
                    setPadding(0, 8, 0, 8)
                }
                holder.answersRadioGroup.addView(radioButton)
            }

            holder.answersRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
                val selectedAnswerId = selectedRadioButton?.tag as? String
                val selectedAnswerText = selectedRadioButton?.text.toString()

            }

        } else {
            holder.answersRadioGroup.visibility = View.GONE
            holder.questionAnswer.visibility = View.VISIBLE

            holder.questionAnswer.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val answerText = holder.questionAnswer.text.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int = questions?.size ?: 0

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) "не указана"
        else {
            dateString
        }
    }
}