package com.example.stepupandroid.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.api.CallBackWrapper
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.response.ServiceResponse
import io.reactivex.disposables.Disposable

class GetServiceViewModel(context: Context) : BaseViewModel(context) {
    private var loginDataSubscription: Disposable? = null

    private val getServiceLiveData: MutableLiveData<ServiceResponse> = MutableLiveData()
    val getServiceResultState: LiveData<ServiceResponse> get() = getServiceLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun getService(body: GetServiceParam) {
        loadingDialog.show()
        loginDataSubscription = ApiImp().getService(body).subscribe({
            loadingDialog.hide()
            getServiceLiveData.value = it.data!!
        }, { throwable ->
            // Handle error response
            object : CallBackWrapper(throwable) {
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