package net.nurigo.sdk.message.service

import kotlinx.serialization.json.Json
import net.nurigo.sdk.message.exception.*
import net.nurigo.sdk.message.lib.Authenticator
import net.nurigo.sdk.message.lib.MapHelper
import net.nurigo.sdk.message.lib.addParameterToCriteria
import net.nurigo.sdk.message.lib.handleErrorResponse
import net.nurigo.sdk.message.lib.processSendRequest
import net.nurigo.sdk.message.model.*
import net.nurigo.sdk.message.dto.request.FileUploadRequest
import net.nurigo.sdk.message.dto.request.MessageListBaseRequest
import net.nurigo.sdk.message.dto.request.MessageListRequest
import net.nurigo.sdk.message.dto.request.MultipleDetailMessageSendingRequest
import net.nurigo.sdk.message.dto.request.SendRequestConfig
import net.nurigo.sdk.message.dto.response.ErrorResponse
import net.nurigo.sdk.message.dto.response.MessageListResponse
import net.nurigo.sdk.message.dto.response.MultipleDetailMessageSentResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.codec.binary.Base64
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.io.FileInputStream

class DefaultMessageService(apiKey: String, apiSecretKey: String, domain: String) : MessageService {
    private var messageHttpService: MessageHttpService

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val authInfo = Authenticator(apiKey, apiSecretKey).generateAuthInfo()
                val request: Request = chain.request().newBuilder().addHeader("Authorization", authInfo).build()
                chain.proceed(request)
            }.build()
        val contentType = "application/json".toMediaType()
        val jsonConfig = Json {
            coerceInputValues = true
            explicitNulls = false
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        messageHttpService =
            Retrofit.Builder().baseUrl(domain).addConverterFactory(jsonConfig.asConverterFactory(contentType))
                .client(client).build().create(MessageHttpService::class.java)
    }


    /**
     * 파일 업로드 API
     * @suppress 파일을 접근 가능한 경로로 입력하셔야 합니다.
     * */
    @Throws
    fun uploadFile(file: File, fileType: StorageType = StorageType.MMS, link: String? = null): String? {
        val length = file.length()
        val imageByte = ByteArray(length.toInt())
        val fis: FileInputStream?
        try {
            fis = FileInputStream(file)
            fis.read(imageByte)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val encodedFile = String(Base64.encodeBase64(imageByte))
        val fileRequest = FileUploadRequest(
            file = encodedFile, type = fileType, link = link
        )
        val response = this.messageHttpService.uploadFile(fileRequest).execute()
        if (response.isSuccessful) {
            return response.body()?.fileId
        } else {
            // 파일 업로드는 특별한 예외를 던지므로 공통 에러 핸들러를 사용하지 않음
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            throw SolapiFileUploadException(errorResponse.errorMessage)
        }
    }

    /**
     * 메시지 조회 API
     */
    @JvmOverloads
    fun getMessageList(parameter: MessageListRequest? = null): MessageListResponse? {
        val payload = parameter?.let { it ->
            val tempPayload = MessageListBaseRequest()

            if (it.status != null && !it.statusCode.isNullOrBlank()) {
                throw SolapiBadRequestException("status와 statusCode는 병기할 수 없습니다.")
            }

            it.status?.let { status ->
                val (criteria, cond, value) = when (status) {
                    MessageStatusType.PENDING -> Triple("statusCode", "eq", "2000")
                    MessageStatusType.SENDING -> Triple("statusCode", "eq", "3000")
                    MessageStatusType.COMPLETE -> Triple("statusCode", "eq", "4000")
                    MessageStatusType.FAILED -> Triple(
                        "statusCode,statusCode,statusCode",
                        "ne,ne,ne",
                        "2000,3000,4000"
                    )
                }
                tempPayload.criteria = criteria
                tempPayload.cond = cond
                tempPayload.value = value
            }

            it.messageIds?.takeIf { it.isNotEmpty() }?.let { ids ->
                val messageIdCriteria = "messageId"
                val eqCond = "eq"
                tempPayload.criteria =
                    (tempPayload.criteria?.let { "$it," } ?: "") + ids.joinToString(",") { messageIdCriteria }
                tempPayload.cond = (tempPayload.cond?.let { "$it," } ?: "") + ids.joinToString(",") { eqCond }
                tempPayload.value = (tempPayload.value?.let { "$it," } ?: "") + ids.joinToString(",")
            }

            listOf(
                "to" to it.to,
                "from" to it.from,
                "messageId" to it.messageId,
                "groupId" to it.groupId,
                "type" to it.type,
                "statusCode" to it.statusCode
            ).forEach { (field, value) ->
                addParameterToCriteria(tempPayload, field, value)
            }

            tempPayload.startKey = it.startKey
            tempPayload.limit = it.limit
            tempPayload.startDate = it.startDate
            tempPayload.endDate = it.endDate

            MapHelper.toMap(tempPayload)
        } ?: emptyMap<String, Any>()

        val response = this.messageHttpService.getMessageList(payload).execute()

        return if (response.isSuccessful) {
            response.body()
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }

    /**
     * 단일, 다중 메시지 발송 메소드
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     * SendRequestConfig 파라미터를 통해 예약발송, 중복 수신번호 허용, 메시지 리스트 표시 옵션을 활성화/비활성화 할 수 있습니다.
     */
    @JvmOverloads
    @Throws(
        SolapiMessageNotReceivedException::class, SolapiEmptyResponseException::class, SolapiUnknownException::class
    )
    fun send(
        message: Message,
        sendRequestConfig: SendRequestConfig = SendRequestConfig(),
    ): MultipleDetailMessageSentResponse {
        return send(listOf(message), sendRequestConfig)
    }

    /**
     * 단일, 다중 메시지 발송 메소드
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     * SendRequestConfig 파라미터를 통해 예약발송, 중복 수신번호 허용, 메시지 리스트 표시 옵션을 활성화/비활성화 할 수 있습니다.
     */
    @JvmOverloads
    @Throws(
        SolapiMessageNotReceivedException::class, SolapiEmptyResponseException::class, SolapiUnknownException::class
    )
    fun send(
        messages: List<Message>,
        sendRequestConfig: SendRequestConfig = SendRequestConfig(),
    ): MultipleDetailMessageSentResponse {
        if (messages.isEmpty()) {
            throw SolapiBadRequestException("메시지가 1건 이상 등록되어야 합니다.")
        }
        if (messages.size > 10000) {
            throw SolapiBadRequestException("10,000건 이상의 메시지는 한 번에 발송할 수 없습니다.")
        }

        val parameter = MultipleDetailMessageSendingRequest(
            messages = messages,
            scheduledDate = sendRequestConfig.scheduledDate,
            showMessageList = sendRequestConfig.showMessageList
        )
        if (sendRequestConfig.allowDuplicates) {
            parameter.allowDuplicates = true
        }
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 잔액 조회 API
     */
    @Throws
    fun getBalance(): Balance {
        val response = this.messageHttpService.getBalance().execute()
        if (response.isSuccessful) {
            return response.body() ?: throw SolapiUnknownException("잔액 조회 데이터를 불러오지 못했습니다.")
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }

    /**
     * 일일 발송량 한도 조회 API
     */
    @Throws
    fun getQuota(): Quota {
        val response = this.messageHttpService.getQuota().execute()
        if (response.isSuccessful) {
            return response.body() ?: throw SolapiUnknownException("일일 발송량 조회에 실패하였습니다.")
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }
}
