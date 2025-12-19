package com.example.task1.features.runquiz.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.database.models.QuestionAnswer
import com.example.task1.data.database.models.QuestionInQuiz

class QuestionAdapter(
    private var questions: List<QuestionInQuiz>?
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionName: TextView = itemView.findViewById(R.id.question_name)
        val questionAnswer: EditText = itemView.findViewById(R.id.answer_text)
        val answersRadioGroup: RadioGroup = itemView.findViewById(R.id.answers_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions?.get(position)

        val required = if (question?.required == true) "*" else ""

        holder.questionName.text = question?.question_text + " " + required

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

                question.selectedAnswerText = null
                question.selectedAnswerId = selectedAnswerId
            }

        } else {
            holder.answersRadioGroup.visibility = View.GONE
            holder.questionAnswer.visibility = View.VISIBLE

            holder.questionAnswer.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val answerText = holder.questionAnswer.text.toString()
                    question?.selectedAnswerText = answerText
                }
            }
        }
    }

    override fun getItemCount(): Int = questions?.size ?: 0

    fun getAnswers(): List<QuestionAnswer> {
        return questions?.map { question ->
            QuestionAnswer(
                id = question.id,
                answer_id = question.selectedAnswerId,
                answer_text = question.selectedAnswerText
            )
        } ?: emptyList()
    }

    fun updateQuizzes(newQuestions: List<QuestionInQuiz>) {
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