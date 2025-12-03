package com.example.task1.data.encryptedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlin.apply

object EncryptedPrefsRepository {

    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = EncryptedSharedPreferences.create(
            "preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getUserId(): String {
        return sharedPreferences.getString("id", "").toString()
    }

    fun saveUserId(id: String) {
        sharedPreferences.edit().putString("id", id).apply()
    }

}