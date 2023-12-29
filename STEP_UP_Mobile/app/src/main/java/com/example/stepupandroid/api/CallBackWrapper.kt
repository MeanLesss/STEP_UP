package com.example.stepupandroid.api

import android.util.Log
import okhttp3.ResponseBody
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import java.util.concurrent.TimeoutException

abstract class CallBackWrapper(e: Throwable) {
    protected abstract fun onCallbackWrapper(status: ApiManager.NetworkErrorStatus, data: String)

    // New method to handle the exception, to be called after the construction
    fun handleException(e: Throwable) {
        when (e) {
            is TimeoutException -> onCallbackWrapper(
                ApiManager.NetworkErrorStatus.ON_TIMEOUT,
                SERVER_ERROR_MESSAGE
            )
            is IOException ->  onCallbackWrapper(
                ApiManager.NetworkErrorStatus.ON_UNKNOWN_ERROR,
                DEFAULT_ERROR_MESSAGE
            )
            is HttpException -> {
                Log.d("Logger", "" + e.code())
                when (e.code()) {
                    504 -> onCallbackWrapper(
                        ApiManager.NetworkErrorStatus.ON_TIMEOUT,
                        "Gateway timeout. Please try again."
                    )
                    else -> {
                        val responseBody = e.response()?.errorBody()
                        onCallbackWrapper(ApiManager.NetworkErrorStatus.ON_ERROR, getErrorMessage(responseBody))
                    }
                }
            }
            else -> {
                var responseBody = e.message.toString()
                if(responseBody.isEmpty()){
                    responseBody = "We are encountering a technical issue. Please check at the counter."
                }
                onCallbackWrapper(ApiManager.NetworkErrorStatus.ON_ERROR, responseBody)
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val json: String? = responseBody?.string()
            val jsonObject = JSONObject(json!!)
            return jsonObject.getString("message")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "Please try again."
        const val NETWORK_ERROR_MESSAGE = "No Internet Connection!"
        const val SERVER_ERROR_MESSAGE = "We sorry your connection timeout, please try again later!"
    }

    object ErrorCode {
        const val BadRequest = 400
        const val Unauthorized = 401
        const val Forbidden = 403
        const val NotFound = 404
        const val MethodNotAllowed = 405
        const val RequestEntityTooLarge = 413
        const val UnProcessableEntity = 422
        const val InternalServerError = 500
        const val BadGateway = 502
        const val GatewayTimeout = 504
    }

}