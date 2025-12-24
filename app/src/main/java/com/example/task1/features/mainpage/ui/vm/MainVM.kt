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

    fun loadQuizzes(requests: MainRequests, page: Int) {
        viewModelScope.launch {
            _quizzesData.value = requests.loadQuizzes(page)
        }
    }

    fun searchQuizzes(requests: MainRequests, searchRequest: SearchQuizRequest) {
        viewModelScope.launch {
            _quizzesData.value = requests.search(searchRequest)
        }
    }
}