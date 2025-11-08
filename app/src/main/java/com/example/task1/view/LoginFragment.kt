package com.example.task1.view

import android.R.attr.value
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.task1.R
import com.example.task1.models.requests.LoginRequest
import com.example.task1.view.quiz.QuizAdapter
import com.example.task1.viewmodel.client.RetrofitClient
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var infoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loginEditText = view.findViewById(R.id.login_in)
        passwordEditText = view.findViewById(R.id.password_in)
        infoText = view.findViewById(R.id.info_text)

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "preferences",
            masterKeyAlias,
            requireContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val loginBtn = view.findViewById<Button>(R.id.login_btn)
        loginBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()

                lifecycleScope.launch {
                    try {
                        val response =
                            RetrofitClient.apiService.login(LoginRequest(login, password))
                        if (response.result == "success") {
                            sharedPreferences.edit()
                                .putString("id", response.token.toString())
                                .apply()
                        } else {
                            infoText.text = response.result
                        }
                    } catch (e: Exception) {
                        Log.e("NetworkError", "Ошибка: ${e.message}")
                        findNavController().navigate(R.id.accountFragment)
                    }
                }
            }
        })

        val signupBtn = view.findViewById<Button>(R.id.signup_btn)
        signupBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()

                lifecycleScope.launch {
                    try {
                        val response =
                            RetrofitClient.apiService.signup(LoginRequest(login, password))
                        if (response.result == "success") {
                            sharedPreferences.edit()
                                .putString("id", response.token.toString())
                                .apply()
                        } else {
                            infoText.text = response.result
                        }
                    } catch (e: Exception) {
                        Log.e("NetworkError", "Ошибка: ${e.message}")
                        findNavController().navigate(R.id.accountFragment)
                    }
                }
            }
        })

        return view
    }
}