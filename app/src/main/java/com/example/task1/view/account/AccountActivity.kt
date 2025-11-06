package com.example.task1.view.account

import android.R.attr.value
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.task1.MainActivity
import com.example.task1.R
import com.example.task1.controller.client.RetrofitClient
import com.example.task1.view.quiz.QuizAdapter
import kotlinx.coroutines.launch

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "preferences",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.navigationIcon = AppCompatResources.getDrawable(this, R.drawable.arrow)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = null

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("key", value)
            startActivity(intent)
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserData(
                    "Bearer ${
                        sharedPreferences.getString(
                            "id",
                            ""
                        ).toString()
                    }"
                )
                findViewById<TextView>(R.id.username).text = response.username.toString()

                val createdView = findViewById<RecyclerView>(R.id.created_recycle)
                val createdAdapter = QuizAdapter(response.created_quizes)
                createdView.adapter = createdAdapter
                createdView.layoutManager = LinearLayoutManager(this@AccountActivity)

                val doneView = findViewById<RecyclerView>(R.id.done_recycle)
                val doneAdapter = QuizAdapter(response.done_quizes)
                doneView.adapter = doneAdapter
                doneView.layoutManager = LinearLayoutManager(this@AccountActivity)
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки данных", e)
            }
        }
    }
}