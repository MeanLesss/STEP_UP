package com.example.stepupandroid.helper

import android.content.Context
import com.example.stepupandroid.model.Attachment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Util {

    fun formatStringToDecimal(inputString: String): String {
        return try {
            val number = inputString.toDouble()
            String.format("%.2f", number)
        } catch (e: NumberFormatException) {
            "Invalid Input" // Return this or handle the exception as needed
        }
    }

    fun formatCurrency(input: String): String {
        val numberString = input.removePrefix("$") // Remove the dollar sign
        val number = numberString.toDoubleOrNull() ?: return input // Return original if not a number
        return "$%.2f".format(number) // Format to 2 decimal places
    }

    fun convertDate(inputPattern: String, outputPattern: String, inputDate: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
        val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())

        // Parse the input date string
        val parsedDate = inputFormat.parse(inputDate)

        // Format the date into the new format
        return outputFormat.format(parsedDate?: return "")
    }

    fun prepareStringParts(params: Map<String, Any>): Map<String, RequestBody> {
        val partMap = mutableMapOf<String, RequestBody>()

        params.forEach { (key, value) ->
            when (value) {
                is String -> partMap[key] = value.toRequestBody("text/plain".toMediaTypeOrNull())
            }
        }

        return partMap
    }

    fun prepareFileParts(context: Context, attachments: List<Attachment>): List<MultipartBody.Part> {
        return attachments.mapNotNull { attachment ->
            context.contentResolver.openInputStream(attachment.fileUri)?.use { inputStream ->
                val requestFile = inputStream.readBytes().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val fileName = attachment.fileName
                MultipartBody.Part.createFormData("attachment_files[]", fileName, requestFile)
            }
        }
    }

}