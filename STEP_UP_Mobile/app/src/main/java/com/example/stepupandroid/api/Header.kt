package com.example.stepupandroid.api

import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.SharedPreferenceUtil

class Header {
    companion object {
        fun getHeader(): Map<String, String> {
            return mapOf(
                ApiKey.ApiKey.ContentType to ApiKey.ApiValue.ContentType,
                ApiKey.ApiKey.Accept to ApiKey.ApiValue.Accept
            )
        }

        fun getHeaderWithAuth(): Map<String, String> {
            return mapOf(
                ApiKey.ApiKey.ContentType to ApiKey.ApiValue.ContentType,
                ApiKey.ApiKey.Accept to ApiKey.ApiValue.Accept,
                ApiKey.ApiKey.Auth to ApiKey.ApiKey.Bearer + " " + SharedPreferenceUtil().getFromSp(ApiKey.SharedPreferenceKey.token)
            )
        }
    }
}