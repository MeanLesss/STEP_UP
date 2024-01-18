package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.response.GetUserResponse
import io.reactivex.disposables.Disposable

class ProfileViewModel(context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val getUserLiveData: MutableLiveData<GetUserResponse> = MutableLiveData()
    val getUserResultState: LiveData<GetUserResponse> get() = getUserLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun getUser() {
        loadingDialog.show()
        dataSubscription = ApiImp().getUser().subscribe({
            loadingDialog.hide()
            getUserLiveData.value = it.data!!
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

    // Override the onCleared method to dispose of the subscription
    override fun onCleared() {
        super.onCleared()
        dataSubscription?.dispose()
    }
}