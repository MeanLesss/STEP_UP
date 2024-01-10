package com.example.stepupandroid.helper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Util {
    fun convertDateFormat(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Assuming the original date string is in UTC

        val date = inputFormat.parse(dateStr)
        return outputFormat.format(date ?: return "")
    }
}