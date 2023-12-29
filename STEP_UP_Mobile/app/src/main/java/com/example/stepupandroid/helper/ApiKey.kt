package com.example.stepupandroid.helper

interface ApiKey {
    interface ApiKey {
        companion object {
            const val ContentType = "Content-Type"
            const val Accept = "Accept"
            const val Bearer = "Bearer"
            const val Auth = "Authorization"
        }
    }

    interface ApiValue {
        companion object {
            const val ContentType = "application/json"
            const val Accept = "application/json"
        }
    }

    interface SharedPreferenceKey {
        companion object {
            const val token = "token"
        }
    }
}