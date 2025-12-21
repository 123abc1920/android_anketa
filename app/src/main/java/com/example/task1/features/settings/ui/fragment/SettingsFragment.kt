package com.example.task1.features.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.task1.R
import com.example.task1.features.settings.domain.Requests

class SettingsFragment : Fragment() {

    private val requests = Requests()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_settings, container, false)

        val result = requests.loadUserData(viewLifecycleOwner)
        view.findViewById<ConstraintLayout>(R.id.for_logged).visibility =
            result.get("result") as Int
        view.findViewById<EditText>(R.id.name_text).setText(result.get("username").toString())
        view.findViewById<EditText>(R.id.login_text).setText(result.get("login").toString())

        view.findViewById<Button>(R.id.save_userdata).setOnClickListener {
            requests.saveUserData(
                viewLifecycleOwner,
                requireContext(),
                view.findViewById<EditText>(R.id.login_text).text.toString(),
                view.findViewById<EditText>(R.id.name_text).text.toString()
            )
        }

        view.findViewById<Button>(R.id.save_password).setOnClickListener {
            val isSuccess = requests.savePassword(
                viewLifecycleOwner,
                requireContext(),
                view.findViewById<EditText>(R.id.password_old).text.toString(),
                view.findViewById<EditText>(R.id.password_new).text.toString()
            )
            if (isSuccess) {
                view.findViewById<EditText>(R.id.password_old).setText("")
                view.findViewById<EditText>(R.id.password_new).setText("")
            }
        }

        return view
    }
}