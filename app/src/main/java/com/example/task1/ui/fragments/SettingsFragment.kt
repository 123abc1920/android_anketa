package com.example.task1.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.task1.R
import com.example.task1.data.api.RetrofitClient
import com.example.task1.domain.authorisation.getUserId
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_settings, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            var userData = RetrofitClient.apiService.getUserData(
                "Bearer ${getUserId()}"
            )

            if (userData.result == "success") {
                view.findViewById<ConstraintLayout>(R.id.for_logged).visibility = View.VISIBLE

                view.findViewById<EditText>(R.id.name_text).setText(userData.username)
                view.findViewById<EditText>(R.id.login_text).setText(userData.login)
            }

            view.findViewById<Button>(R.id.save_userdata).setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    var result = RetrofitClient.apiService.setData(
                        "Bearer ${getUserId()}",
                        mapOf(
                            "login" to view.findViewById<EditText>(R.id.login_text).text.toString(),
                            "name" to view.findViewById<EditText>(R.id.name_text).text.toString()
                        )
                    )

                    if (result.result == "success") {
                        Toast.makeText(requireContext(), "Данные обновлены!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            view.findViewById<Button>(R.id.save_password).setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    var result = RetrofitClient.apiService.setPassword(
                        "Bearer ${getUserId()}",
                        mapOf(
                            "old_password" to view.findViewById<EditText>(R.id.password_old).text.toString(),
                            "new_password" to view.findViewById<EditText>(R.id.password_new).text.toString()
                        )
                    )

                    if (result.result == "success") {
                        view.findViewById<EditText>(R.id.password_old).setText("")
                        view.findViewById<EditText>(R.id.password_new).setText("")

                        Toast.makeText(requireContext(), "Пароль изменен!", Toast.LENGTH_SHORT)
                            .show()
                    } else if (result.result == "Старый пароль не верен!") {
                        Toast.makeText(
                            requireContext(),
                            "Старый пароль не верен!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

        return view
    }
}