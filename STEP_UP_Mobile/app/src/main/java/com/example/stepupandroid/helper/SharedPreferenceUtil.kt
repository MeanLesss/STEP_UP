package com.example.stepupandroid.helper

import androidx.appcompat.app.AppCompatActivity
import com.example.stepupandroid.App


class SharedPreferenceUtil {

    private var context = App.context

    fun addToSp(
        key: String,
        data: String,
        mode: Int = AppCompatActivity.MODE_PRIVATE
    ) {
        try {
            val sharedPreferences = context.getSharedPreferences(key, mode)

            val myEdit = sharedPreferences.edit()
            myEdit.putString(key, data)
            myEdit.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeFromSp(
        key: String
    ) {
        try {
            val sharedPreferences = context.getSharedPreferences(key, AppCompatActivity.MODE_PRIVATE)

            val myEdit = sharedPreferences.edit()
            myEdit.remove(key)
            myEdit.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFromSp(
        key: String,
        mode: Int = AppCompatActivity.MODE_PRIVATE
    ): String? {
        return try {
            val sharedPreferences = context.getSharedPreferences(key, mode)
            return sharedPreferences.getString(key, null)?.replace("\"\"", "\"")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}