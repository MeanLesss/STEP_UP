package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.param.OrderServiceParam
import com.example.stepupandroid.model.param.OrderServiceSummaryParam
import com.example.stepupandroid.model.response.OrderServiceSummaryResponse
import com.google.gson.JsonElement
import io.reactivex.disposables.Disposable

class OrderServiceViewModel(context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val orderServiceLiveData: MutableLiveData<JsonElement> = MutableLiveData()
    val orderServiceResultState: LiveData<JsonElement> get() = orderServiceLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun orderService(body: OrderServiceParam) {
        loadingDialog.show()
        dataSubscription = ApiImp().orderService(body).subscribe({
            loadingDialog.hide()
            orderServiceLiveData.value = it.data
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

    private val getOrderSummaryLiveData: MutableLiveData<OrderServiceSummaryResponse> = MutableLiveData()
    val getOrderSummaryResultState: LiveData<OrderServiceSummaryResponse> get() = getOrderSummaryLiveData

    fun getOrderSummary(body: OrderServiceSummaryParam) {
        loadingDialog.show()
            dataSubscription = ApiImp().getOrderSummary(body).subscribe({
            loadingDialog.hide()
            getOrderSummaryLiveData.value = it.data
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
        dataSubscription?.dispose()
    }
}