package net.nurigo.solapi.sdk.message.service

import net.nurigo.solapi.sdk.message.response.MessageListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

// TODO: 파라미터 추가 필요, Service implementation 필요
interface MessageService {

    @GET("/messages/v4/list")
    fun getMessageList(): Call<MessageListResponse?>

    @POST("/messages/v4/send")
    fun send(): Call<Any>

    @POST("/messages/v4/send-many")
    fun sendMany(): Call<Any>
}