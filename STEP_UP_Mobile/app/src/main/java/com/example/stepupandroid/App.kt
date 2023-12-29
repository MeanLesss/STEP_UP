package com.example.stepupandroid

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication


class App: MultiDexApplication() {
    companion object {
        lateinit var context: App
    }

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
        context = this
    }
}