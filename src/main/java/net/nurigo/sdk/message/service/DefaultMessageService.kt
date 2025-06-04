package net.nurigo.sdk.message.service

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.json.Json
import net.nurigo.sdk.message.exception.*
import net.nurigo.sdk.message.lib.Authenticator
import net.nurigo.sdk.message.lib.MapHelper
import net.nurigo.sdk.message.lib.handleErrorResponse
import net.nurigo.sdk.message.lib.processSendRequest
import net.nurigo.sdk.message.model.*
import net.nurigo.sdk.message.request.*
import net.nurigo.sdk.message.response.*
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
        val client = OkHttpClient.Builder().addInterceptor { chain ->
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
            throw NurigoFileUploadException(errorResponse.errorMessage)
        }
    }

    /**
     * 메시지 조회 API
     */
    fun getMessageList(): MessageListResponse? {
        val response = this.messageHttpService.getMessageList(emptyMap()).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }

    /**
     * 메시지 조회 API
     * */
    @Throws
    fun getMessageList(parameter: MessageListRequest): MessageListResponse? {
        val tempPayload = MessageListBaseRequest()
        val payload = mutableMapOf<String, Any?>()
        if (parameter.status != null && !parameter.statusCode.isNullOrBlank()) {
            // TODO: i18n needed
            throw NurigoBadRequestException("status와 statusCode는 병기할 수 없습니다.")
        } else if (parameter.status != null) {
            when (parameter.status) {
                MessageStatusType.PENDING -> {
                    tempPayload.criteria = "statusCode"
                    tempPayload.cond = "eq"
                    tempPayload.value = "2000"
                }

                MessageStatusType.SENDING -> {
                    tempPayload.criteria = "statusCode"
                    tempPayload.cond = "eq"
                    tempPayload.value = "3000"
                }

                MessageStatusType.COMPLETE -> {
                    tempPayload.criteria = "statusCode"
                    tempPayload.cond = "eq"
                    tempPayload.value = "4000"
                }

                MessageStatusType.FAILED -> {
                    tempPayload.criteria = "statusCode,statusCode,statusCode"
                    tempPayload.cond = "ne,ne,ne"
                    tempPayload.value = "2000,3000,4000"
                }

                else -> throw NurigoUnknownException("허용될 수 없는 status 값이 입력되었습니다.")
            }
        }

        if (!parameter.messageIds.isNullOrEmpty()) {
            var tempCriteria = ""
            var tempCond = ""

            parameter.messageIds?.forEachIndexed { index: Int, _ ->
                if (index == 0 && (tempPayload.criteria.isNullOrBlank() && tempPayload.cond.isNullOrBlank() && tempPayload.value.isNullOrBlank())) {
                    tempCriteria = "messageId"
                    tempCond = "eq"
                } else {
                    tempCriteria += ",messageId"
                    tempCond += ",eq"
                }
            }

            tempPayload.criteria = if (tempPayload.criteria.isNullOrBlank()) {
                tempCriteria
            } else {
                tempPayload.criteria + tempCriteria
            }
            tempPayload.cond = if (tempPayload.cond.isNullOrBlank()) {
                tempCond
            } else {
                tempPayload.cond + tempCond
            }
            tempPayload.value = if (tempPayload.value.isNullOrBlank()) {
                parameter.messageIds!!.joinToString(",")
            } else {
                tempPayload.value + "," + parameter.messageIds!!.joinToString(",")
            }
        }

        // TODO: Refactor needed
        if (!tempPayload.criteria.isNullOrBlank() && !tempPayload.cond.isNullOrBlank() && !tempPayload.value.isNullOrBlank()) {
            parameter.to.takeIf { !it.isNullOrBlank() }?.let {
                tempPayload.criteria += ",to"
                tempPayload.cond += ",eq"
                tempPayload.value += ",$it"
            }
            parameter.from.takeIf { !it.isNullOrBlank() }?.let {
                tempPayload.criteria += ",from"
                tempPayload.cond += ",eq"
                tempPayload.value += ",$it"
            }
            parameter.messageId.takeIf { !it.isNullOrBlank() }?.let {
                tempPayload.criteria += ",messageId"
                tempPayload.cond += ",eq"
                tempPayload.value += ",$it"
            }
            parameter.groupId.takeIf { !it.isNullOrBlank() }?.let {
                tempPayload.criteria += ",groupId"
                tempPayload.cond += ",eq"
                tempPayload.value += ",$it"
            }
            parameter.type.takeIf { !it.isNullOrBlank() }?.let {
                tempPayload.criteria += ",type"
                tempPayload.cond += ",eq"
                tempPayload.value += ",$it"
            }
            parameter.statusCode.takeIf { !it.isNullOrBlank() }?.let {
                tempPayload.criteria += ",statusCode"
                tempPayload.cond += ",eq"
                tempPayload.value += ",$it"
            }
        } else {
            tempPayload.to = parameter.to
            tempPayload.from = parameter.from
            tempPayload.messageId = parameter.messageId
            tempPayload.groupId = parameter.groupId
            tempPayload.type = parameter.type
        }
        tempPayload.startKey = parameter.startKey
        tempPayload.limit = parameter.limit
        tempPayload.startDate = parameter.startDate
        tempPayload.endDate = parameter.endDate

        payload.putAll(MapHelper.toMap(tempPayload))
        val response = this.messageHttpService.getMessageList(payload).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }

    /**
     * @deprecated 해당 메소드는 제거될 예정입니다. 향후 send 메소드를 이용해주세요.
     * @see send
     * 단일 메시지 발송 API
     * */
    @Throws
    fun sendOne(parameter: SingleMessageSendingRequest): SingleMessageSentResponse? {
        val response = this.messageHttpService.sendOne(parameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }

    /**
     * 단일 메시지 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(message: Message): MultipleDetailMessageSentResponse {
        val multipleParameter = MultipleDetailMessageSendingRequest(
            messages = listOf(message), scheduledDate = null
        )
        return processSendRequest(this.messageHttpService, multipleParameter)
    }

    /**
     * 단일 메시지 발송 및 다중 메시지(2건 이상) 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(message: Message, scheduledDateTime: java.time.Instant): MultipleDetailMessageSentResponse {
        val multipleParameter = MultipleDetailMessageSendingRequest(
            messages = listOf(message), scheduledDate = scheduledDateTime.toKotlinInstant()
        )
        return processSendRequest(this.messageHttpService, multipleParameter)
    }

    /**
     * 단일 메시지 발송 및 다중 메시지(2건 이상) 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(message: Message, scheduledDate: Instant): MultipleDetailMessageSentResponse {
        val multipleParameter = MultipleDetailMessageSendingRequest(
            messages = listOf(message), scheduledDate = scheduledDate
        )
        return processSendRequest(this.messageHttpService, multipleParameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(messages: List<Message>): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages, null
        )
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(messages: List<Message>, allowDuplicates: Boolean): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages, null
        )
        parameter.allowDuplicates = allowDuplicates
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(
        messages: List<Message>, allowDuplicates: Boolean, showMessageList: Boolean
    ): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages = messages,
            scheduledDate = null,
            showMessageList = showMessageList,
        )
        parameter.allowDuplicates = allowDuplicates
        return processSendRequest(this.messageHttpService, parameter)
    }


    /**
     * 다중 메시지(2건 이상) 발송 및 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(messages: List<Message>, scheduledDateTime: java.time.Instant): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages, scheduledDateTime.toKotlinInstant()
        )
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 및 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(
        messages: List<Message>, scheduledDateTime: java.time.Instant, allowDuplicates: Boolean
    ): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages, scheduledDateTime.toKotlinInstant()
        )
        parameter.allowDuplicates = allowDuplicates
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 및 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(
        messages: List<Message>,
        scheduledDateTime: java.time.Instant,
        allowDuplicates: Boolean,
        showMessageList: Boolean
    ): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages = messages, scheduledDate = scheduledDateTime.toKotlinInstant(), showMessageList = showMessageList
        )
        parameter.allowDuplicates = allowDuplicates
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 및 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(
        messages: List<Message>, scheduledDateTime: Instant, allowDuplicates: Boolean
    ): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages, scheduledDateTime
        )
        parameter.allowDuplicates = allowDuplicates
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 및 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(
        messages: List<Message>, scheduledDateTime: Instant, allowDuplicates: Boolean, showMessageList: Boolean
    ): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages = messages, scheduledDate = scheduledDateTime, showMessageList = showMessageList
        )
        parameter.allowDuplicates = allowDuplicates
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 다중 메시지(2건 이상) 발송 및 예약 발송 API
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     */
    @Throws(
        NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
    )
    fun send(messages: List<Message>, scheduledDateTime: Instant): MultipleDetailMessageSentResponse {
        val parameter = MultipleDetailMessageSendingRequest(
            messages, scheduledDateTime
        )
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 잔액 조회 API
     */
    @Throws
    fun getBalance(): Balance {
        val response = this.messageHttpService.getBalance().execute()
        if (response.isSuccessful) {
            return response.body() ?: throw NurigoUnknownException("잔액 조회 데이터를 불러오지 못했습니다.")
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
            return response.body() ?: throw NurigoUnknownException("일일 발송량 조회에 실패하였습니다.")
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }
}
