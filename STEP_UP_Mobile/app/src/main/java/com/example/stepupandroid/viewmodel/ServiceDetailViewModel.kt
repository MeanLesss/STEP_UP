package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.response.ServiceDetailResponse
import io.reactivex.disposables.Disposable

class ServiceDetailViewModel(context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val serviceDetailLiveData: MutableLiveData<ServiceDetailResponse> = MutableLiveData()
    val serviceDetailResultState: LiveData<ServiceDetailResponse> get() = serviceDetailLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun getServiceDetail(serviceId: Int) {
        loadingDialog.show()
        dataSubscription = ApiImp().getServiceDetails(serviceId).subscribe({
            loadingDialog.hide()
            serviceDetailLiveData.value = it.data
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