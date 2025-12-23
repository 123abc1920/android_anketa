package com.example.task1.features.editcreate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R
import com.example.task1.data.api.models.Answer
import com.example.task1.data.api.models.Question
import com.example.task1.data.database.models.QuestionAnswer
import com.example.task1.data.database.models.QuestionInQuiz
import kotlin.collections.remove

class CreateQuizAdapter(
    private var questions: MutableList<QuestionInQuiz>?
) : RecyclerView.Adapter<CreateQuizAdapter.CreateQuizViewHolder>() {

    class CreateQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var addAnswer: Button = itemView.findViewById(R.id.add_answer)
        var deleteQuestion: Button = itemView.findViewById(R.id.delete_question)
        var isRequired: CheckBox = itemView.findViewById(R.id.is_required)
        var answersView: RecyclerView = itemView.findViewById(R.id.answers_view)
        var answersList = mutableListOf<Answer>()
        var answersAdapter = AnswersAdapter(answersList)
        var context = itemView.context;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateQuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.created_question, parent, false)
        return CreateQuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateQuizViewHolder, position: Int) {
        val question = questions?.get(position)

        holder.answersView.adapter = holder.answersAdapter
        holder.answersView.layoutManager = LinearLayoutManager(holder.context)

        holder.addAnswer.setOnClickListener {
            holder.answersAdapter.addAnswer()
        }

        holder.deleteQuestion.setOnClickListener {
            if (question != null) {
                deleteQuestion(position, question)
            }
        }
    }

    override fun getItemCount(): Int = questions?.size ?: 0

    fun addQuestion() {
        var position = questions?.size
        questions?.add(QuestionInQuiz("", "1", false, emptyList(), null, null))
        if (position == null) {
            position = 0
        }
        notifyItemInserted(position)
    }

    fun deleteQuestion(position: Int, question: QuestionInQuiz) {
        val adapterPosition = position
        if (adapterPosition != RecyclerView.NO_POSITION) {
            questions?.remove(question)
            notifyItemRemoved(position)
        }
    }

    fun getQuestions(): MutableList<QuestionInQuiz>? {
        return questions
    }
}