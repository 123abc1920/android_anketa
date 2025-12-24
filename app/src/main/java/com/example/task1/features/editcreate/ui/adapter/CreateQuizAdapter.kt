package com.example.task1.features.editcreate.ui.adapter

import android.text.Editable
import android.text.TextWatcher
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
import com.example.task1.data.database.models.QuestionInCreateQuiz

class CreateQuizAdapter(
    private var questions: MutableList<QuestionInCreateQuiz>?
) : RecyclerView.Adapter<CreateQuizAdapter.CreateQuizViewHolder>() {

    class CreateQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionText: EditText = itemView.findViewById(R.id.question_name)
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

        holder.questionText.setText(question?.text ?: "")
        holder.isRequired.isChecked = question?.is_required ?: false

        holder.answersView.adapter = holder.answersAdapter
        holder.answersView.layoutManager = LinearLayoutManager(holder.context)

        holder.questionText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                questions?.get(holder.absoluteAdapterPosition)?.text = s.toString()
            }
        })

        holder.isRequired.setOnCheckedChangeListener { _, isChecked ->
            questions?.get(position)?.is_required = isChecked
        }

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
        questions?.add(QuestionInCreateQuiz("", "1", false, emptyList(), null, null))
        if (position == null) {
            position = 0
        }
        notifyItemInserted(position)
    }

    fun deleteQuestion(position: Int, question: QuestionInCreateQuiz) {
        val adapterPosition = position
        if (adapterPosition != RecyclerView.NO_POSITION) {
            questions?.remove(question)
            notifyItemRemoved(position)
        }
    }

    fun getQuestions(): MutableList<QuestionInCreateQuiz>? {
        return questions
    }
}