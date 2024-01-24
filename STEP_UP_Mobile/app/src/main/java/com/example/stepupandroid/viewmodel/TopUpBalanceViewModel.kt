package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import io.reactivex.disposables.Disposable

class TopUpBalanceViewModel (context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val topUpLiveData: MutableLiveData<String> = MutableLiveData()
    val topUpResultState: LiveData<String> get() = topUpLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun topUp(body: HashMap<String, String>) {
        loadingDialog.show()
        dataSubscription = ApiImp().topUp(body).subscribe({
            loadingDialog.hide()
            topUpLiveData.value = it.msg
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
}