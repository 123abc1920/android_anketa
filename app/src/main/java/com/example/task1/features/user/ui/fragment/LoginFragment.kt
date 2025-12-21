package com.example.task1.features.user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.task1.R
import com.example.task1.features.user.domain.Requests

class LoginFragment : Fragment() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var infoText: TextView

    private val request = Requests()

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
                request.login(
                    viewLifecycleOwner,
                    findNavController(),
                    requireContext(),
                    loginEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        })

        val signupBtn = view.findViewById<Button>(R.id.signup_btn)
        signupBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                request.signup(
                    viewLifecycleOwner,
                    findNavController(),
                    requireContext(),
                    loginEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        })

        return view
    }
}