package com.example.stepupandroid.api

import com.example.stepupandroid.model.ApiResWrapper
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.param.OrderServiceSummaryParam
import com.example.stepupandroid.model.param.SignUpAsGuestParam
import com.example.stepupandroid.model.param.SignUpParam
import com.example.stepupandroid.model.response.GetUserResponse
import com.example.stepupandroid.model.response.LoginResponse
import com.example.stepupandroid.model.response.MyOrderResponse
import com.example.stepupandroid.model.response.MyServiceResponse
import com.example.stepupandroid.model.response.MyWorkResponse
import com.example.stepupandroid.model.response.OrderDetailResponse
import com.example.stepupandroid.model.response.OrderServiceSummaryResponse
import com.example.stepupandroid.model.response.ServiceDetailResponse
import com.example.stepupandroid.model.response.ServiceResponse
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path

interface ServiceAPI {
    @POST("api/login")
    fun login(
        @HeaderMap headers: Map<String, String>,
        @Body body: HashMap<String, String>
    ): Observable<ApiResWrapper<LoginResponse>>

    @GET("api/logout")
    fun logout(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<JsonElement>>

    @POST("api/service/data")
    fun getService(
        @HeaderMap headers: Map<String, String>,
        @Body body: GetServiceParam
    ): Observable<ApiResWrapper<ServiceResponse>>

    @GET("api/my-service/view")
    fun getMyService(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<MyServiceResponse>>

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

    @POST("api/signup")
    fun signUpAsGuest(
        @HeaderMap headers: Map<String, String>,
        @Body body: SignUpAsGuestParam
    ): Observable<ApiResWrapper<LoginResponse>>

    @GET("api/user")
    fun getUser(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<GetUserResponse>>


    @GET("api/service/ordered/freelancer/false")
    fun getMyWork(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<MyWorkResponse>>

    @GET("api/service/ordered/freelancer/true")
    fun getMyOrder(
        @HeaderMap headers: Map<String, String>,
    ): Observable<ApiResWrapper<MyOrderResponse>>

    @GET("api/service/{serviceId}/view")
    fun getServiceDetail(
        @Path("serviceId") serviceId: Int,
        @HeaderMap headers: Map<String, String>
    ): Observable<ApiResWrapper<ServiceDetailResponse>>

    @POST("api/service/purchase-summary")
    fun getOrderSummary(
        @HeaderMap headers: Map<String, String>,
        @Body body: OrderServiceSummaryParam
    ): Observable<ApiResWrapper<OrderServiceSummaryResponse>>

    @Multipart
    @POST("api/service/confirm-purchase")
    fun orderService(
        @HeaderMap headers: Map<String, String>,
        @Part parts: List<MultipartBody.Part>
    ): Observable<ApiResWrapper<JsonElement>>

    @GET("api/order-service/{orderId}/view")
    fun getOrderDetail(
        @HeaderMap headers: Map<String, String>,
        @Path("orderId") orderId: Int
    ): Observable<ApiResWrapper<OrderDetailResponse>>

    @POST("api/order-service/{orderId}/accept")
    fun acceptOrder(
        @HeaderMap headers: Map<String, String>,
        @Body body: HashMap<String, Boolean>,
        @Path("orderId") orderId: Int,
    ): Observable<ApiResWrapper<JsonElement>>

    @POST("api/freelancer/cancellationBeforeDueDate")
    fun freelancerCancel(
        @HeaderMap headers: Map<String, String>,
        @Body body: Map<String, String>
    ): Observable<ApiResWrapper<JsonElement>>
}