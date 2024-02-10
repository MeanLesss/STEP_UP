package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.response.ServiceResponse
import io.reactivex.disposables.Disposable


class ServiceViewModel(context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val getServiceLiveData: MutableLiveData<ServiceResponse> = MutableLiveData()
    val getServiceResultState: LiveData<ServiceResponse> get() = getServiceLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun getService(body: GetServiceParam) {
        loadingDialog.show()
        dataSubscription = ApiImp().getService(body).subscribe({
            loadingDialog.hide()
            getServiceLiveData.value = it.data!!
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