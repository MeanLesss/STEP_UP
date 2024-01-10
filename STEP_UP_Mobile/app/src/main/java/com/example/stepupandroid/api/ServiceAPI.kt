package com.example.stepupandroid.api

import com.example.stepupandroid.model.ApiResWrapper
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.param.OrderParam
import com.example.stepupandroid.model.response.LoginResponse
import com.example.stepupandroid.model.response.MyServiceResponse
import com.example.stepupandroid.model.response.OrderSummaryResponse
import com.example.stepupandroid.model.response.ServiceResponse
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import io.reactivex.Observable
import retrofit2.http.GET

interface ServiceAPI {
    @POST("api/login")
    fun login(
        @HeaderMap headers: Map<String, String>,
        @Body body: HashMap<String, String>
    ): Observable<ApiResWrapper<LoginResponse>>

    @POST("api/service/data")
    fun getService(
        @HeaderMap headers: Map<String, String>,
        @Body body: GetServiceParam
    ): Observable<ApiResWrapper<ServiceResponse>>

    @GET("api/my-service/view")
    fun getMyService(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<MyServiceResponse>>

    @POST("api/service/purchase-summary/")
    fun getOrderSummary(
        @HeaderMap headers: Map<String, String>,
        @Body body: OrderParam
    ): Observable<ApiResWrapper<OrderSummaryResponse>>


}