package com.example.task1.features.runquiz.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task1.features.runquiz.domain.RunRequests
import kotlinx.coroutines.launch

class QuizVM : ViewModel() {
    private val _quizData = MutableLiveData<Map<String, Any>?>()
    val quizData: LiveData<Map<String, Any>?> = _quizData

    fun loadQuiz(request: RunRequests, quizId: String) {
        viewModelScope.launch {
            _quizData.value = request.loadQuiz(quizId)
        }
    }
}