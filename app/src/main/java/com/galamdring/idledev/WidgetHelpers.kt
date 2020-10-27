package com.galamdring.idledev

object WidgetHelpers {
    private const val numberStringLength = 5
    private const val maxNonExponentNum = 100000
    fun formatNumbers(number: Double): String {
        if (number > maxNonExponentNum) {
            return String.format("%6.2e", number).padStart(numberStringLength).filterNot { it == '+' }
        }
        return String.format("%.2f", number).padStart(numberStringLength)
    }
}
