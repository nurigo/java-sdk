package net.nurigo.sdk.message.service

import net.nurigo.sdk.message.model.Balance
import net.nurigo.sdk.message.model.Quota
import net.nurigo.sdk.message.dto.request.FileUploadRequest
import net.nurigo.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateCreateRequest
import net.nurigo.sdk.message.dto.request.MultipleDetailMessageSendingRequest
import net.nurigo.sdk.message.dto.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.dto.response.FileUploadResponse
import net.nurigo.sdk.message.dto.response.MessageListResponse
import net.nurigo.sdk.message.dto.response.MultipleDetailMessageSentResponse
import net.nurigo.sdk.message.dto.response.SingleMessageSentResponse
import net.nurigo.sdk.message.dto.response.kakao.KakaoAlimtalkTemplateListResponse
import net.nurigo.sdk.message.dto.response.kakao.KakaoAlimtalkTemplateResponse
import net.nurigo.sdk.message.model.kakao.KakaoAlimtalkTemplateCategory
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

    @GET("/kakao/v2/templates/categories")
    fun getAlimtalkTemplateCategories(): Call<List<KakaoAlimtalkTemplateCategory>>

    @POST("/kakao/v2/templates")
    fun createAlimtalkTemplate(@Body parameter: KakaoAlimtalkTemplateCreateRequest): Call<KakaoAlimtalkTemplateResponse>

    @PUT("/kakao/v2/templates/{templateId}")
    fun updateAlimtalkTemplate(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @PUT("/kakao/v2/templates/{templateId}/inspection")
    fun inspectKakaoAlimtalkTemplate(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @GET("/kakao/v2/templates/{templateId}")
    fun getAlimtalkTemplate(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @GET("/kakao/v2/templates")
    fun getAlimtalkTemplates(): Call<KakaoAlimtalkTemplateListResponse>

    @GET("/kakao/v2/templates/sendable")
    fun getSendableAlimtalkTemplates(): Call<List<KakaoAlimtalkTemplateResponse>>

    @PUT("/kakao/v2/templates/{templateId}/approval/cancel")
    fun transitionAlimtalkTemplateToPending(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @PUT("/kakao/v2/templates/{templateId}/inspection/cancel")
    fun cancelAlimtalkTemplateInspection(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @DELETE("/kakao/v2/templates/{templateId}")
    fun removeAlimtalkTemplate(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @POST("/kakao/v2/templates/{templateId}/relese-dormant")
    fun requestAlimtalkTemplateReactivation(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @PUT("/kakao/v2/templates/{templateId}/name")
    fun updateAlimtalkTemplateName(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>
}