package com.example.stepupandroid.api

import com.example.stepupandroid.App
import com.example.stepupandroid.helper.Util
import com.example.stepupandroid.model.ApiResWrapper
import com.example.stepupandroid.model.param.CreateServiceParam
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.param.OrderServiceSummaryParam
import com.example.stepupandroid.model.param.OrderServiceParam
import com.example.stepupandroid.model.param.SignUpAsGuestParam
import com.example.stepupandroid.model.param.SignUpParam
import com.example.stepupandroid.model.param.SubmitWorkParam
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class ApiImp : ApiManager() {
    private var context = App.context

    fun login(body: HashMap<String, String>): Observable<ApiResWrapper<LoginResponse>> =
        mAllService.login(Header.getHeader(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun logout(): Observable<ApiResWrapper<JsonElement>> =
        mAllService.logout(Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getService(body: GetServiceParam): Observable<ApiResWrapper<ServiceResponse>> =
        mAllService.getService(Header.getHeaderWithAuth(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMyService(): Observable<ApiResWrapper<MyServiceResponse>> =
        mAllService.getMyService(Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun createService(createServiceParam: CreateServiceParam): Observable<ApiResWrapper<JsonElement>> {
        // Prepare params map
        val params = mapOf(
            "title" to createServiceParam.title,
            "description" to createServiceParam.description,
            "price" to createServiceParam.price,
            "service_type" to createServiceParam.service_type,
            "start_date" to createServiceParam.start_date,
            "end_date" to createServiceParam.end_date
        )
        val stringParts = Util.prepareStringParts(params)
        val fileParts = Util.prepareFileParts(context, createServiceParam.attachments)

        // Combine string parts and file parts
        val allParts = stringParts.map { MultipartBody.Part.createFormData(it.key, null, it.value) } + fileParts

        return mAllService.createService(Header.getHeaderAuthOnly(), allParts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun signUp(body: SignUpParam): Observable<ApiResWrapper<LoginResponse>> =
        mAllService.signUp(Header.getHeader(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun signUpAsGuest(body: SignUpAsGuestParam): Observable<ApiResWrapper<LoginResponse>> =
        mAllService.signUpAsGuest(Header.getHeader(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getUser(): Observable<ApiResWrapper<GetUserResponse>> =
        mAllService.getUser(Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMyWork(): Observable<ApiResWrapper<MyWorkResponse>> =
        mAllService.getMyWork(Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getMyOrder(): Observable<ApiResWrapper<MyOrderResponse>> =
        mAllService.getMyOrder(Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getServiceDetails(serviceId: Int): Observable<ApiResWrapper<ServiceDetailResponse>> =
        mAllService.getServiceDetail(serviceId, Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getOrderSummary(body: OrderServiceSummaryParam): Observable<ApiResWrapper<OrderServiceSummaryResponse>> {
        return mAllService.getOrderSummary(Header.getHeaderWithAuth(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun orderService(orderServiceParam: OrderServiceParam): Observable<ApiResWrapper<JsonElement>> {
        // Prepare params map
        val params = mapOf(
            "service_id" to orderServiceParam.service_id,
            "order_title" to orderServiceParam.order_title,
            "order_description" to orderServiceParam.order_description,
            "expected_start_date" to orderServiceParam.expected_start_date,
            "expected_end_date" to orderServiceParam.expected_end_date,
            "isAgreementAgreed" to orderServiceParam.isAgreementAgreed
        )
        val stringParts = Util.prepareStringParts(params)
        val fileParts = Util.prepareFileParts(context, orderServiceParam.attachments)

        // Combine string parts and file parts
        val allParts = stringParts.map { MultipartBody.Part.createFormData(it.key, null, it.value) } + fileParts

        return mAllService.orderService(Header.getHeaderAuthOnly(), allParts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getOrderDetail(orderId: Int): Observable<ApiResWrapper<OrderDetailResponse>> =
        mAllService.getOrderDetail(Header.getHeaderWithAuth(), orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getWorkDetail(orderId: Int): Observable<ApiResWrapper<OrderDetailResponse>> =
        mAllService.getWorkDetail(Header.getHeaderWithAuth(), orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun acceptOrder(body: HashMap<String, Boolean>, orderId: Int): Observable<ApiResWrapper<JsonElement>> =
        mAllService.acceptOrder(Header.getHeaderWithAuth(), body, orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun submitWork(submitWorkParam: SubmitWorkParam): Observable<ApiResWrapper<JsonElement>> {
        // Prepare params map
        val params = mapOf(
            "order_id" to submitWorkParam.order_id,
            "service_id" to submitWorkParam.service_id
        )
        val stringParts = Util.prepareStringParts(params)
        val fileParts = Util.prepareFileParts(context, submitWorkParam.attachments)

        // Combine string parts and file parts
        val allParts = stringParts.map { MultipartBody.Part.createFormData(it.key, null, it.value) } + fileParts

        return mAllService.submitWork(Header.getHeaderAuthOnly(), allParts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun freelancerCancel(body: HashMap<String, String>): Observable<ApiResWrapper<JsonElement>> =
        mAllService.freelancerCancel(Header.getHeaderWithAuth(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun clientCancelPending(body: HashMap<String, String>): Observable<ApiResWrapper<JsonElement>> =
        mAllService.clientCancelPending(Header.getHeaderWithAuth(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun clientCancelInProgress(body: HashMap<String, String>): Observable<ApiResWrapper<JsonElement>> =
        mAllService.clientCancelInProgress(Header.getHeaderWithAuth(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun topUp(body: HashMap<String, String>): Observable<ApiResWrapper<JsonElement>> =
        mAllService.topUp(Header.getHeaderWithAuth(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}