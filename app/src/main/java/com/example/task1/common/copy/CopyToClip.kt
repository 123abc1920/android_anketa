package com.example.task1.common.copy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun copyToClip(context: Context, text: String){
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("", text))
}