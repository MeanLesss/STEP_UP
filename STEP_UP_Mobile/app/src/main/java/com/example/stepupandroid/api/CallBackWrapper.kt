import android.util.Log
import com.example.stepupandroid.api.ApiManager
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

abstract class CallBackWrapper {
    protected abstract fun onCallbackWrapper(status: ApiManager.NetworkErrorStatus, data: String)

    fun handleException(e: Throwable) {
        when (e) {
            is TimeoutException -> onCallbackWrapper(
                ApiManager.NetworkErrorStatus.ON_TIMEOUT,
                SERVER_ERROR_MESSAGE
            )

            is IOException -> onCallbackWrapper(
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
                        onCallbackWrapper(
                            ApiManager.NetworkErrorStatus.ON_ERROR,
                            getErrorMessage(responseBody)
                        )
                    }
                }
            }

            else -> {
                var responseBody = e.message ?: ""
                if (responseBody.isEmpty()) {
                    responseBody =
                        "We are encountering a technical issue. Please check at the counter."
                }
                onCallbackWrapper(ApiManager.NetworkErrorStatus.ON_ERROR, responseBody)
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val json: String? = responseBody?.string()
            val jsonObject = JSONObject(json!!)
            try {
                jsonObject.getString("msg")
            } catch (e: JSONException) {
                jsonObject.getString("message")
            }
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
}
