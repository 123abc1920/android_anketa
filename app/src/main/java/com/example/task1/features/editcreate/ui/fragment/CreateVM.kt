package com.example.task1.features.editcreate.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.features.editcreate.domain.EditRequests
import kotlinx.coroutines.launch

class CreateVM : ViewModel() {
    private val _saveResult = MutableLiveData<Boolean?>()
    val saveResult: LiveData<Boolean?> = _saveResult

    fun createQuiz(requests: EditRequests, createdQuiz: CreateQuizResponse) {
        viewModelScope.launch {
            val success = requests.createQuiz(createdQuiz)
            _saveResult.value = success
        }
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }
}