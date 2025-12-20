package com.example.task1.domain.initdatepickers

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.MotionEvent
import android.widget.EditText
import java.util.Calendar

object InitDatePickers {

    @SuppressLint("ClickableViewAccessibility", "DefaultLocale")
    fun InitDatePicker(datePicker: EditText, context: Context) {
        datePicker.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val c = Calendar.getInstance()
                DatePickerDialog(
                    context, { _, year, month, day ->
                        TimePickerDialog(
                            context, { _, hour, minute ->
                                val dateStr = String.format(
                                    "%04d-%02d-%02dT%02d:%02d",
                                    year, month + 1, day, hour, minute
                                )
                                datePicker.setText(dateStr)
                            },
                            c.get(Calendar.HOUR_OF_DAY),
                            c.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            false
        }
    }

}