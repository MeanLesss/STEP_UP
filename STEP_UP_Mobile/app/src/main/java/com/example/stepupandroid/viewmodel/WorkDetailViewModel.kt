package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.param.SubmitWorkParam
import com.example.stepupandroid.model.response.OrderDetailResponse
import com.google.gson.JsonElement
import io.reactivex.disposables.Disposable

class WorkDetailViewModel(context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val workDetailLiveData: MutableLiveData<OrderDetailResponse> = MutableLiveData()
    val workDetailResultState: LiveData<OrderDetailResponse> get() = workDetailLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun getWorkDetail(workId: Int) {
        loadingDialog.show()
        dataSubscription = ApiImp().getWorkDetail(workId).subscribe({
            loadingDialog.hide()
            workDetailLiveData.value = it.data!!
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

    private val acceptWorkLiveData: MutableLiveData<String> = MutableLiveData()
    val acceptWorkResultState: LiveData<String> get() = acceptWorkLiveData

    fun acceptWork(body: HashMap<String, Boolean>, workId: Int) {
        loadingDialog.show()
        dataSubscription = ApiImp().acceptOrder(body, workId).subscribe({
            loadingDialog.hide()
            acceptWorkLiveData.value = it.msg
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

    private val submitWorkLiveData: MutableLiveData<String> = MutableLiveData()
    val submitWorkResultState: LiveData<String> get() = submitWorkLiveData

    fun submitWork(body: SubmitWorkParam) {
        loadingDialog.show()
        dataSubscription = ApiImp().submitWork(body).subscribe({
            loadingDialog.hide()
            submitWorkLiveData.value = it.msg
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

    private val cancelWorkLiveData: MutableLiveData<String> = MutableLiveData()
    val cancelWorkResultState: LiveData<String> get() = cancelWorkLiveData

    fun cancelWork(body: HashMap<String, String>) {
        loadingDialog.show()
        dataSubscription = ApiImp().freelancerCancel(body).subscribe({
            loadingDialog.hide()
            cancelWorkLiveData.value = it.msg
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