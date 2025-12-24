package com.example.task1.features.editcreate.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.features.editcreate.domain.EditRequests
import kotlinx.coroutines.launch

class EditVM : ViewModel() {
    private val _quizData = MutableLiveData<Map<String, Any>?>()
    val quizData: LiveData<Map<String, Any>?> = _quizData

    private val _saveResult = MutableLiveData<Boolean?>()
    val saveResult: LiveData<Boolean?> = _saveResult

    fun loadQuizData(requests: EditRequests, quizId: String?) {
        viewModelScope.launch {
            if (quizId != null) {
                val data = requests.loadQuizData(quizId)
                _quizData.value = data
            }
        }
    }

    fun saveQuiz(requests: EditRequests, editQuiz: EditQuizRequest) {
        viewModelScope.launch {
            val success = requests.sendCreatedQuiz(editQuiz)
            _saveResult.value = success
        }
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }
}