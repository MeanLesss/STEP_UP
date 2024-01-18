package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.response.OrderDetailResponse
import io.reactivex.disposables.Disposable

class OrderDetailViewModel(context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val orderDetailLiveData: MutableLiveData<OrderDetailResponse> = MutableLiveData()
    val orderDetailResultState: LiveData<OrderDetailResponse> get() = orderDetailLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun getOrderDetail(orderId: Int) {
        loadingDialog.show()
        dataSubscription = ApiImp().getOrderDetail(orderId).subscribe({
            loadingDialog.hide()
            orderDetailLiveData.value = it.data!!
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