package com.example.stepupandroid.viewmodel

import CallBackWrapper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stepupandroid.api.ApiImp
import com.example.stepupandroid.api.ApiManager
import com.example.stepupandroid.base.BaseViewModel
import com.example.stepupandroid.model.param.CreateServiceParam
import com.google.gson.JsonElement
import io.reactivex.disposables.Disposable

class CreateServiceViewModel(context: Context) : BaseViewModel(context) {
    private var dataSubscription: Disposable? = null

    private val createServiceLiveData: MutableLiveData<JsonElement> = MutableLiveData()
    val createServiceResultState: LiveData<JsonElement> get() = createServiceLiveData
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorResultState: LiveData<String> get() = errorLiveData

    fun createService(body: CreateServiceParam) {
        loadingDialog.show()
        dataSubscription = ApiImp().createService(body).subscribe({
            loadingDialog.hide()
            createServiceLiveData.value = it.data!!
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