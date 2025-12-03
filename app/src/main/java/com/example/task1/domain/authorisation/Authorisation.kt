package com.example.task1.domain.authorisation

import android.content.Context
import com.example.task1.data.encryptedprefs.EncryptedPrefsRepository

fun getUserId(): String {
    return EncryptedPrefsRepository.getUserId()
}

fun saveUserId(id: String) {
    EncryptedPrefsRepository.saveUserId(id)
}