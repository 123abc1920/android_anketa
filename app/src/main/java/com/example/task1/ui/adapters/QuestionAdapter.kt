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
import com.example.task1.data.database.models.QuestionAnswer
import com.example.task1.data.database.models.QuestionInQuiz

class QuestionAdapter(
    private var questions: List<QuestionInQuiz>?
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addBtn: Button = itemView.findViewById(R.id.add_answer)
        val delBtn: Button = itemView.findViewById(R.id.delete_question)
        val answersView: RecyclerView = itemView.findViewById(R.id.answers_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions?.get(position)
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