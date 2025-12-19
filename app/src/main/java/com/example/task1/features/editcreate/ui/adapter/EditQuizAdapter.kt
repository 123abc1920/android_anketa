package com.example.task1.features.editcreate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Answer
import com.example.task1.data.database.responses.Question

class EditQuizAdapter(
    private var questions: MutableList<Question>
) : RecyclerView.Adapter<EditQuizAdapter.EditQuizViewHolder>() {

    class EditQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var addAnswer: Button = itemView.findViewById(R.id.add_answer)
        var deleteQuestion: Button = itemView.findViewById(R.id.delete_question)
        var isRequired: CheckBox = itemView.findViewById(R.id.is_required)
        var answersView: RecyclerView = itemView.findViewById(R.id.answers_view)
        var context = itemView.context
        var questionName = itemView.findViewById<EditText>(R.id.question_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditQuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.created_question, parent, false)
        return EditQuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditQuizViewHolder, position: Int) {
        val question = questions[position]

        holder.questionName.setText(question.question_text)

        val answersAdapter = AnswersAdapter(question.answers)
        holder.answersView.adapter = answersAdapter
        holder.answersView.layoutManager = LinearLayoutManager(holder.context)

        holder.addAnswer.setOnClickListener {
            val newAnswer = Answer(
                question.answers.size + 1,
                "Новый вариант",
                question.id.toIntOrNull() ?: -1
            )
            question.answers.add(newAnswer)
            answersAdapter.notifyDataSetChanged()
        }

        holder.deleteQuestion.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                questions.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

        // Для изменения текста вопроса
        holder.questionName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                question.question_text = holder.questionName.text.toString()
            }
        }
    }

    override fun getItemCount(): Int = questions.size

    fun updateQuizzes(newQuestions: MutableList<Question>) {
        this.questions = newQuestions
        notifyDataSetChanged()
    }
}