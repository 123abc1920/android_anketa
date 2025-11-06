package com.example.task1.view.account

import android.R.attr.value
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.task1.MainActivity
import com.example.task1.R
import com.example.task1.controller.client.RetrofitClient
import com.example.task1.controller.models.TempUserId
import com.example.task1.controller.models.requests.LoginRequest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

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

        val loginEditText = findViewById<EditText>(R.id.login_in)
        val passwordEditText = findViewById<EditText>(R.id.password_in)

        val loginBtn = findViewById<Button>(R.id.login_btn)
        loginBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()

                lifecycleScope.launch {
                    try {
                        val response =
                            RetrofitClient.apiService.login(LoginRequest(login, password))
                        if (response.result == "success") {
                            TempUserId.id = response.id
                            val intent = Intent(this@LoginActivity, AccountActivity::class.java)
                            intent.putExtra("key", value)
                            startActivity(intent)
                        } else {
                            findViewById<TextView>(R.id.info_text).text = response.result
                        }
                    } catch (e: Exception) {
                        Log.e("NetworkError", "Ошибка: ${e.message}")
                    }
                }
            }
        })

        val signupBtn = findViewById<Button>(R.id.signup_btn)
        signupBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()

                lifecycleScope.launch {
                    try {
                        val response =
                            RetrofitClient.apiService.signup(LoginRequest(login, password))
                        if (response.result == "success") {
                            TempUserId.id = response.id
                            val intent = Intent(this@LoginActivity, AccountActivity::class.java)
                            intent.putExtra("key", value)
                            startActivity(intent)
                        } else {
                            findViewById<TextView>(R.id.info_text).text = response.result
                        }
                    } catch (e: Exception) {
                        Log.e("NetworkError", "Ошибка: ${e.message}")
                    }
                }
            }
        })
    }
}