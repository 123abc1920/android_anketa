package com.example.task1.commondomain.authorisation

import com.example.task1.data.encryptedprefs.EncryptedPrefsRepository

fun getUserId(): String {
    return EncryptedPrefsRepository.getUserId()
}

fun getUserIdHeader(): String{
    return "Bearer ${getUserId()}"
}

fun saveUserId(id: String) {
    EncryptedPrefsRepository.saveUserId(id)
}