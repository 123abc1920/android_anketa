package com.example.task1.features.user.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task1.features.user.domain.UserRequests
import kotlinx.coroutines.launch

class AccountVM : ViewModel() {
    private val _userData = MutableLiveData<Map<String, Any>?>()
    val userData: LiveData<Map<String, Any>?> = _userData

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun loadUserData(request: UserRequests){
        viewModelScope.launch {
            when (val result = request.loadUserData()) {
                is UserRequests.Result.Success -> {
                    _userData.value = result.data
                    _error.value = false
                }
                is UserRequests.Result.Error -> {
                    _error.value = true
                }
            }
        }
    }
}