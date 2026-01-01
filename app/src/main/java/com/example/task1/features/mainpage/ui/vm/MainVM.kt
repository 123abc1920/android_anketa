package com.example.task1.features.mainpage.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.requests.SearchQuizRequest
import com.example.task1.features.mainpage.domain.MainRequests
import kotlinx.coroutines.launch

class MainVM : ViewModel() {
    private val _quizzesData = MutableLiveData<List<Quiz>>()
    val quizzesData: LiveData<List<Quiz>> = _quizzesData
    private val _max = MutableLiveData<Int>(1)
    val max: LiveData<Int> = _max

    fun loadQuizzes(requests: MainRequests, page: Int) {
        viewModelScope.launch {
            val data = requests.loadQuizzes(page)
            _quizzesData.value = data.get("quizzes") as List<Quiz>?
            _max.value = data.get("max") as Int?
        }
    }

    fun searchQuizzes(requests: MainRequests, searchRequest: SearchQuizRequest) {
        viewModelScope.launch {
            val data = requests.search(searchRequest)
            _quizzesData.value = data.get("quizzes") as List<Quiz>?
            _max.value = data.get("max") as Int?
        }
    }
}