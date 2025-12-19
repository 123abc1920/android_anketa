package com.example.task1.features.user.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.data.database.requests.LoginRequest
import com.example.task1.domain.authorisation.saveUserId
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

        val loginBtn = view.findViewById<Button>(R.id.login_btn)
        loginBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()

                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val response =
                            RetrofitClient.apiService.login(LoginRequest(login, password))
                        if (response.result == "success") {
                            saveUserId(response.token.toString())
                            findNavController().navigate(R.id.accountFragment)
                        } else {
                            infoText.text = response.result
                        }
                    } catch (e: Exception) {
                        Log.e("NetworkError", "Ошибка: ${e.message}")
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
            }
        })

        val signupBtn = view.findViewById<Button>(R.id.signup_btn)
        signupBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val login = loginEditText.text.toString()
                val password = passwordEditText.text.toString()

                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val response =
                            RetrofitClient.apiService.signup(LoginRequest(login, password))
                        if (response.result == "success") {
                            saveUserId(response.token.toString())
                            findNavController().navigate(R.id.accountFragment)
                        } else {
                            infoText.text = response.result
                        }
                    } catch (e: Exception) {
                        Log.e("NetworkError", "Ошибка: ${e.message}")
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
            }
        })

        return view
    }
}