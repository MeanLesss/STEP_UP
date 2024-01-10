package com.example.stepupandroid.api

import com.example.stepupandroid.model.ApiResWrapper
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.param.OrderParam
import com.example.stepupandroid.model.response.LoginResponse
import com.example.stepupandroid.model.response.MyServiceResponse
import com.example.stepupandroid.model.response.OrderSummaryResponse
import com.example.stepupandroid.model.response.ServiceResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ApiImp : ApiManager() {
    fun login(body: HashMap<String, String>): Observable<ApiResWrapper<LoginResponse>> =
        mAllService.login(Header.getHeader(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getService(body: GetServiceParam): Observable<ApiResWrapper<ServiceResponse>> =
        mAllService.getService(Header.getHeader(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMyService(): Observable<ApiResWrapper<MyServiceResponse>> =
        mAllService.getMyService(Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getOrderSummary(body: OrderParam): Observable<ApiResWrapper<OrderSummaryResponse>> {
        return mAllService.getOrderSummary(Header.getHeader(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}