package com.example.stepupandroid.api

import android.net.Uri
import android.provider.OpenableColumns
import com.example.stepupandroid.App
import com.example.stepupandroid.model.ApiResWrapper
import com.example.stepupandroid.model.Attachment
import com.example.stepupandroid.model.param.CreateServiceParam
import com.example.stepupandroid.model.param.GetServiceParam
import com.example.stepupandroid.model.param.OrderParam
import com.example.stepupandroid.model.param.SignUpParam
import com.example.stepupandroid.model.response.GetUserResponse
import com.example.stepupandroid.model.response.LoginResponse
import com.example.stepupandroid.model.response.MyServiceResponse
import com.example.stepupandroid.model.response.MyWorkResponse
import com.example.stepupandroid.model.response.OrderSummaryResponse
import com.example.stepupandroid.model.response.ServiceDetailResponse
import com.example.stepupandroid.model.response.ServiceResponse
import com.google.gson.JsonElement
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ApiImp : ApiManager() {
    private var context = App.context

    fun login(body: HashMap<String, String>): Observable<ApiResWrapper<LoginResponse>> =
        mAllService.login(Header.getHeader(), body)
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

    fun getOrderSummary(body: OrderParam): Observable<ApiResWrapper<OrderSummaryResponse>> {
        return mAllService.getOrderSummary(Header.getHeaderWithAuth(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun createService(createServiceParam: CreateServiceParam): Observable<ApiResWrapper<JsonElement>> {
        val partMap = prepareRequest(createServiceParam)
        val fileParts = prepareFileParts(createServiceParam.attachments)

        // Combine text parts and file parts
        val allParts = mutableListOf<MultipartBody.Part>()
        for ((key, value) in partMap) {
            allParts.add(MultipartBody.Part.createFormData(key, null, value))
        }
        allParts.addAll(fileParts)

        return mAllService.createService(Header.getHeaderAuthOnly(), allParts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    private fun prepareRequest(createServiceParam: CreateServiceParam): Map<String, RequestBody> {
        val partMap = mutableMapOf<String, RequestBody>()

        // Convert each field to RequestBody
        partMap["title"] = createServiceParam.title.toRequestBody("text/plain".toMediaTypeOrNull())
        partMap["description"] = createServiceParam.description.toRequestBody("text/plain".toMediaTypeOrNull())
        partMap["price"] = createServiceParam.price.toRequestBody("text/plain".toMediaTypeOrNull())
        partMap["service_type"] = createServiceParam.service_type.toRequestBody("text/plain".toMediaTypeOrNull())
        partMap["start_date"] = createServiceParam.start_date.toRequestBody("text/plain".toMediaTypeOrNull())
        partMap["end_date"] = createServiceParam.end_date.toRequestBody("text/plain".toMediaTypeOrNull())

        return partMap
    }

    private fun prepareFileParts(attachment: List<Attachment>): List<MultipartBody.Part> {
        return attachment.mapNotNull {
            prepareFilePart(it)
        }
    }

    private fun prepareFilePart(attachment: Attachment): MultipartBody.Part? {
        context.contentResolver.openInputStream(attachment.fileUri)?.use { inputStream ->
            val requestFile = inputStream.readBytes().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val fileName = attachment.fileName
            return MultipartBody.Part.createFormData("attachment_files[]", fileName, requestFile)
        }
        return null
    }

    fun signUp(body: SignUpParam): Observable<ApiResWrapper<LoginResponse>> =
        mAllService.signUp(Header.getHeader(), body)
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

    fun getServiceDetails(serviceId: Int): Observable<ApiResWrapper<ServiceDetailResponse>> =
        mAllService.getServiceDetail(serviceId, Header.getHeaderWithAuth())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}