package com.example.task1.features.settings.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task1.features.settings.domain.SettingsRequests
import kotlinx.coroutines.launch

class SettingsVM : ViewModel() {
    private val _userData = MutableLiveData<Map<String, Any>?>()
    val userData: LiveData<Map<String, Any>?> = _userData

    fun loadUserData(requests: SettingsRequests) {
        viewModelScope.launch {
            _userData.value = requests.loadUserData()
        }
    }
}