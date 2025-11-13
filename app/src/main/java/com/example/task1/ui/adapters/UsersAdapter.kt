package com.example.task1.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.R

class UsersAdapter(
    private var users: List<String>?
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.main_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.watch_answer, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val username = users?.get(position)

        holder.text.text = "- $username"
    }

    override fun getItemCount(): Int = users?.size ?: 0

    fun updateUsers(newUsers: List<String>) {
        this.users = newUsers
        notifyDataSetChanged()
    }

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) "не указана"
        else {
            dateString
        }
    }
}