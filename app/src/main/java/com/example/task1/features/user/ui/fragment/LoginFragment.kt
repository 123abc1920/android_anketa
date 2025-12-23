package com.example.task1.features.user.ui.fragment

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
import com.example.task1.features.user.domain.Requests
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginEditText = view.findViewById<EditText>(R.id.login_in)
        val passwordEditText = view.findViewById<EditText>(R.id.password_in)
        val infoText = view.findViewById<TextView>(R.id.info_text)

        val request = Requests()

        view.findViewById<Button>(R.id.login_btn).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = request.login(
                    requireContext(),
                    loginEditText.text.toString(),
                    passwordEditText.text.toString()
                )

                when (result) {
                    is Requests.Result.Success -> {
                        findNavController().navigate(R.id.accountFragment)
                    }

                    is Requests.Result.Error -> {
                        Log.e("Login Error", result.message)
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.signup_btn).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = request.signup(
                    requireContext(),
                    loginEditText.text.toString(),
                    passwordEditText.text.toString()
                )

                when (result) {
                    is Requests.Result.Success -> {
                        findNavController().navigate(R.id.accountFragment)
                    }

                    is Requests.Result.Error -> {
                        Log.e("Login Error", result.message)
                    }
                }
            }
        }

        return view
    }
}