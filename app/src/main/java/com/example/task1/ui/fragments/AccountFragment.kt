package com.example.task1.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.task1.R
import com.example.task1.ui.adapters.QuizAdapter
import com.example.task1.data.api.RetrofitClient
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "preferences",
            masterKeyAlias,
            requireContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

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
                view.findViewById<TextView>(R.id.username).text = response.username.toString()

                val createdView = view.findViewById<RecyclerView>(R.id.created_recycle)
                val createdAdapter = QuizAdapter(response.created_quizes)
                createdView.adapter = createdAdapter
                createdView.layoutManager = LinearLayoutManager(requireContext())

                val doneView = view.findViewById<RecyclerView>(R.id.done_recycle)
                val doneAdapter = QuizAdapter(response.done_quizes)
                doneView.adapter = doneAdapter
                doneView.layoutManager = LinearLayoutManager(requireContext())
            } catch (e: Exception) {
                Log.e("API ERROR", "Ошибка загрузки данных", e)
                findNavController().navigate(R.id.loginFragment)
            }
        }

        return view
    }
}