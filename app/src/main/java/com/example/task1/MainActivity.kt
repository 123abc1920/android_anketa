package com.example.task1

import android.R.attr.value
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task1.controller.client.RetrofitClient
import com.example.task1.view.account.LoginActivity
import com.example.task1.view.quiz.QuizAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)

        lateinit var quizAdapter: QuizAdapter
        var recyclerView = findViewById<RecyclerView>(R.id.quiz_view)

        lifecycleScope.launch {
            try {
                val quizzes = RetrofitClient.apiService.getQuizzes()

                quizAdapter = QuizAdapter(quizzes)
                recyclerView.adapter = quizAdapter

                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки анкет", e)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.account -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("key", value)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}