package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.helper.ApiKey
import com.example.stepupandroid.helper.Constants
import com.example.stepupandroid.helper.SharedPreferenceUtil
import com.example.stepupandroid.model.response.LoginResponse
import io.reactivex.disposables.Disposable

class LoginViewModel(context: Context) : BaseViewModel(context) {
    private var loginDataSubscription: Disposable? = null

    private val loginLiveData: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginResultState: LiveData<LoginResponse> get() = loginLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun login(body: HashMap<String, String>) {
        loadingDialog.show()
        loginDataSubscription = ApiImp().login(body).subscribe({
            loadingDialog.hide()
            Constants.UserRole = 0
            SharedPreferenceUtil().removeFromSp(ApiKey.SharedPreferenceKey.isGuest)
            it.data?.user_token?.let { token ->
                SharedPreferenceUtil().addToSp(
                    ApiKey.SharedPreferenceKey.token,
                    token
                )
            }
            loginLiveData.value = it.data!!
        }, { throwable ->
            object : CallBackWrapper() {
                override fun onCallbackWrapper(
                    status: ApiManager.NetworkErrorStatus,
                    data: String
                ) {
                    loadingDialog.hide()
                    errorLiveData.value = data
                }
            }.handleException(throwable)
        })
    }

    override fun onCleared() {
        super.onCleared()
        loginDataSubscription?.dispose()
    }
}