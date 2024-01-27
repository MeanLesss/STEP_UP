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
import com.example.stepupandroid.model.param.SignUpAsFreelancerParam
import com.example.stepupandroid.model.param.SignUpAsGuestParam
import com.example.stepupandroid.model.param.SignUpParam
import com.example.stepupandroid.model.response.LoginResponse
import io.reactivex.disposables.Disposable

class SignUpViewModel (context: Context) : BaseViewModel(context) {
    private var signUpDataSubscription: Disposable? = null

    private val signUpLiveData: MutableLiveData<LoginResponse> = MutableLiveData()
    val signUpResultState: LiveData<LoginResponse> get() = signUpLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun signUp(body: SignUpParam) {
        loadingDialog.show()
        signUpDataSubscription = ApiImp().signUp(body).subscribe({
            loadingDialog.hide()
            Constants.UserRole = 0
            SharedPreferenceUtil().removeFromSp(ApiKey.SharedPreferenceKey.isGuest)
            it.data?.user_token?.let { token ->
                SharedPreferenceUtil().addToSp(
                    ApiKey.SharedPreferenceKey.token,
                    token
                )
            }
            signUpLiveData.value = it.data!!
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

    private val signUpAsFreelancerLiveData: MutableLiveData<String> = MutableLiveData()
    val signUpAsFreelancerResultState: LiveData<String> get() = signUpAsFreelancerLiveData
    fun signUpAsFreelancer(body: SignUpAsFreelancerParam) {
        loadingDialog.show()
        signUpDataSubscription = ApiImp().signUpAsFreelancer(body).subscribe({
            loadingDialog.hide()
            signUpAsFreelancerLiveData.value = it.msg
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

    fun signUpAsGuest(body: SignUpAsGuestParam) {
        loadingDialog.show()
        signUpDataSubscription = ApiImp().signUpAsGuest(body).subscribe({
            loadingDialog.hide()
            Constants.UserRole = 0
            SharedPreferenceUtil().addToSp(
                ApiKey.SharedPreferenceKey.isGuest,
                "true"
            )
            it.data?.user_token?.let { token ->
                SharedPreferenceUtil().addToSp(
                    ApiKey.SharedPreferenceKey.token,
                    token
                )
            }
            signUpLiveData.value = it.data!!
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
        signUpDataSubscription?.dispose()
    }
}