package net.nurigo.sdk.message.service

import net.nurigo.sdk.message.request.MultipleMessageSendingRequest
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.response.MessageListResponse
import net.nurigo.sdk.message.response.MultipleMessageSentResponse
import net.nurigo.sdk.message.response.SingleMessageSentResponse
import retrofit2.Call
import retrofit2.http.*

interface MessageHttpService : MessageService {

    @GET("/messages/v4/list")
    fun getMessageList(@QueryMap parameter: Map<String, String>? = null): Call<MessageListResponse>

    @POST("/messages/v4/send")
    fun sendOne(@Body parameter: SingleMessageSendingRequest): Call<SingleMessageSentResponse>

    @POST("/messages/v4/send-many")
    fun sendMany(@Body parameter: MultipleMessageSendingRequest): Call<MultipleMessageSentResponse>
}