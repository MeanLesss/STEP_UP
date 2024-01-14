package com.example.stepupandroid.api

import com.example.stepupandroid.model.ApiResWrapper
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.param.OrderParam
import com.example.stepupandroid.model.param.SignUpParam
import com.example.stepupandroid.model.response.GetUserResponse
import com.example.stepupandroid.model.response.LoginResponse
import com.example.stepupandroid.model.response.MyServiceResponse
import com.example.stepupandroid.model.response.MyWorkResponse
import com.example.stepupandroid.model.response.OrderSummaryResponse
import com.example.stepupandroid.model.response.ServiceResponse
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

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

    @Multipart
    @POST("api/service/create")
    fun createService(
        @HeaderMap headers: Map<String, String>,
        @Part parts: List<MultipartBody.Part>
    ): Observable<ApiResWrapper<JsonElement>>

    @POST("api/signup")
    fun signUp(
        @HeaderMap headers: Map<String, String>,
        @Body body: SignUpParam
    ): Observable<ApiResWrapper<LoginResponse>>

    @GET("api/user")
    fun getUser(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<GetUserResponse>>

    @GET("api/service/ordered/freelancer")
    fun getMyWork(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<MyWorkResponse>>
}