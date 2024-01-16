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

    fun convertDate(inputPattern: String, inputDate: String): String {
        // Define the input and output date formats
        val inputFormat = android.icu.text.SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = android.icu.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        // Parse the input date string
        val parsedDate = inputFormat.parse(inputDate)

        // Format the date into the new format
        return outputFormat.format(parsedDate)
    }
}