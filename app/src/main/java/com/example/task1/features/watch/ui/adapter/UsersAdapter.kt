package com.example.task1.features.watch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R

class UsersAdapter(
    private var users: MutableList<String>?
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.main_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.watch_answer, parent, false)
        return UsersViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val username = users?.get(position)

        holder.text.text = "- $username"
    }

    override fun getItemCount(): Int = users?.size ?: 0

    fun updateUsers(newList: List<String>) {
        users?.clear()
        users?.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteUser(position: Int) {
        users?.removeAt(position)
        notifyItemRemoved(position)
    }
}