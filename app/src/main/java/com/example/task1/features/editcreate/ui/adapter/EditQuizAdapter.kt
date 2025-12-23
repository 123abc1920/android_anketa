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
            /*val newAnswer = Answer(
                question.answers.size + 1,
                "",
                question.id.toIntOrNull() ?: -1
            )
            question.answers.add(newAnswer)*/
            answersAdapter.addAnswer(question.answers.size + 1, question.id.toIntOrNull() ?: -1)
        }

        holder.deleteQuestion.setOnClickListener {
            deleteQuestion(position)
        }

        holder.questionName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                question.question_text = holder.questionName.text.toString()
            }
        }
    }

    override fun getItemCount(): Int = questions.size

    fun updateQuestions(newQuestions: MutableList<Question>) {
        this.questions = newQuestions
        notifyDataSetChanged()
    }

    fun addQuestion() {
        val newQuestion = Question(
            "", "", "", mutableListOf(),
        )
        questions.add(newQuestion)
        notifyDataSetChanged()
    }

    fun deleteQuestion(position: Int) {
        val adapterPosition = position
        if (adapterPosition != RecyclerView.NO_POSITION) {
            questions.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }

    fun getQuestions(): List<Question> {
        return questions
    }
}