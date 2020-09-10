package com.galamdring.idledev

import android.content.Context
import android.widget.Button
import androidx.core.content.ContextCompat

object helpers {
    fun formatNumbers(number: Double) : String {
        if (number > 100000){
            return String.format("%6.2e", number).padStart(5).filterNot { it == '+' }
        }
        return String.format("%.0f", number).padStart(5)
    }

    fun markButtonDisable(button: Button, context: Context) {
        button?.isEnabled = false
        button?.setTextColor(ContextCompat.getColor(context, R.color.buttonDisabledText))
        button?.setBackgroundColor(ContextCompat.getColor(context, R.color.buttonDisabledBackground))
    }

    fun markButtonEnabled(button: Button, context: Context) {
        button?.isEnabled = true
        button?.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        button?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }
}