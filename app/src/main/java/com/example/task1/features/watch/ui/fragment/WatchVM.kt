package com.example.task1.features.watch.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task1.features.watch.domain.WatchRequests
import kotlinx.coroutines.launch

class WatchVM : ViewModel() {
    private val _quizData = MutableLiveData<Map<String, Any>?>()
    val quizData: LiveData<Map<String, Any>?> = _quizData

    fun loadData(requests: WatchRequests, quizId: String) {
        viewModelScope.launch {
            val data = requests.loadQuestions(quizId)
            _quizData.value = data
        }
    }
}