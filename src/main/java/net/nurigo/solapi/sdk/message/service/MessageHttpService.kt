package net.nurigo.solapi.sdk.message.service

import kotlinx.serialization.json.JsonObject
import net.nurigo.solapi.sdk.message.request.MultipleMessageSendingRequest
import net.nurigo.solapi.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.solapi.sdk.message.response.MessageListResponse
import net.nurigo.solapi.sdk.message.response.MultipleMessageSentResponse
import net.nurigo.solapi.sdk.message.response.SingleMessageSentResponse
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