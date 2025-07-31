package net.nurigo.sdk.message.service

import net.nurigo.sdk.message.model.Balance
import net.nurigo.sdk.message.model.Quota
import net.nurigo.sdk.message.dto.request.FileUploadRequest
import net.nurigo.sdk.message.dto.request.MultipleDetailMessageSendingRequest
import net.nurigo.sdk.message.dto.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.dto.response.FileUploadResponse
import net.nurigo.sdk.message.dto.response.MessageListResponse
import net.nurigo.sdk.message.dto.response.MultipleDetailMessageSentResponse
import net.nurigo.sdk.message.dto.response.SingleMessageSentResponse
import retrofit2.Call
import retrofit2.http.*

interface MessageHttpService : MessageService {

    @JvmSuppressWildcards
    @GET("/messages/v4/list")
    fun getMessageList(@QueryMap parameter: Map<String, Any?>? = null): Call<MessageListResponse>

    @POST("/messages/v4/send")
    fun sendOne(@Body parameter: SingleMessageSendingRequest): Call<SingleMessageSentResponse>

    @POST("/messages/v4/send-many/detail")
    fun sendManyDetail(@Body parameter: MultipleDetailMessageSendingRequest): Call<MultipleDetailMessageSentResponse>

    @POST("/storage/v1/files")
    fun uploadFile(@Body fileUploadRequest: FileUploadRequest): Call<FileUploadResponse>

    @GET("/cash/v1/balance")
    fun getBalance(): Call<Balance>

    @GET("/quota/v1/me")
    fun getQuota(): Call<Quota>
}