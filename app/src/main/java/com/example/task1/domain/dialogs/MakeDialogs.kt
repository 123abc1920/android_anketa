package com.example.task1.domain.dialogs

import android.app.AlertDialog
import android.content.Context

fun showConfirmDialog(
    context: Context,
    question: String,
    text: String,
    onResult: (Boolean) -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(question)
        .setMessage(text)
        .setPositiveButton("Принять") { dialog, which ->
            onResult(true)
        }
        .setNegativeButton("Отказаться") { dialog, which ->
            onResult(false)
        }
        .setCancelable(false)
        .show()
}