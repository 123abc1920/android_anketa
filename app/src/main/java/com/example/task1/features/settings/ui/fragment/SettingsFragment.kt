package com.example.task1.features.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.task1.R
import com.example.task1.features.settings.domain.Requests
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private val requests = Requests()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            val result = requests.loadUserData()

            if (isAdded) {
                view.findViewById<ConstraintLayout>(R.id.for_logged).visibility =
                    result["result"] as Int
                view.findViewById<EditText>(R.id.name_text).setText(result["username"].toString())
                view.findViewById<EditText>(R.id.login_text).setText(result["login"].toString())
            }
        }

        view.findViewById<Button>(R.id.save_userdata).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                requests.saveUserData(
                    requireContext(),
                    view.findViewById<EditText>(R.id.login_text).text.toString(),
                    view.findViewById<EditText>(R.id.name_text).text.toString()
                )
            }
        }

        view.findViewById<Button>(R.id.save_password).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val isSuccess = requests.savePassword(
                    requireContext(),
                    view.findViewById<EditText>(R.id.password_old).text.toString(),
                    view.findViewById<EditText>(R.id.password_new).text.toString()
                )

                if (isSuccess) {
                    view.findViewById<EditText>(R.id.password_old).setText("")
                    view.findViewById<EditText>(R.id.password_new).setText("")
                }
            }
        }

        return view
    }
}