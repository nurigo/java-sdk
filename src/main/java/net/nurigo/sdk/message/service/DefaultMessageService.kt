package net.nurigo.sdk.message.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.nurigo.sdk.message.exception.NurigoBadRequestException
import net.nurigo.sdk.message.exception.NurigoFileUploadException
import net.nurigo.sdk.message.exception.NurigoInvalidApiKeyException
import net.nurigo.sdk.message.exception.NurigoUnknownException
import net.nurigo.sdk.message.extension.toStringValueMap
import net.nurigo.sdk.message.lib.Authenticator
import net.nurigo.sdk.message.model.Balance
import net.nurigo.sdk.message.model.StorageType
import net.nurigo.sdk.message.request.*
import net.nurigo.sdk.message.response.*
import net.nurigo.sdk.message.response.ErrorResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.codec.binary.Base64
import retrofit2.Retrofit
import java.io.File
import java.io.FileInputStream


@OptIn(ExperimentalSerializationApi::class)
class DefaultMessageService(apiKey: String, apiSecretKey: String, domain: String) : MessageService {
    private var messageHttpService: MessageHttpService

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val authInfo = Authenticator(apiKey, apiSecretKey).generateAuthInfo()
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", authInfo)
                    .build()
                chain.proceed(request)
            }
            .build()
        val contentType = "application/json".toMediaType()
        val jsonConfig = Json {
            coerceInputValues = true
            explicitNulls = false
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        messageHttpService = Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .client(client)
            .build()
            .create(MessageHttpService::class.java)
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
            file = encodedFile,
            type = fileType,
            link = link
        )
        val response = this.messageHttpService.uploadFile(fileRequest).execute()
        if (response.isSuccessful) {
            return response.body()?.fileId
        } else {
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            throw NurigoFileUploadException(errorResponse.errorMessage)
        }
    }

    /**
     * 메시지 조회 API
     * */
    @Throws
    fun getMessageList(parameter: MessageListRequest = MessageListRequest()): MessageListResponse? {
        val mappedParameter = parameter.toStringValueMap()
        val response = this.messageHttpService.getMessageList(mappedParameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            throw NurigoUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
        }
    }

    /**
     * 단일 메시지 발송 API
     * */
    @Throws
    fun sendOne(parameter: SingleMessageSendingRequest): SingleMessageSentResponse? {
        val response = this.messageHttpService.sendOne(parameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            when (errorResponse.errorCode) {
                "ValidationError" -> throw NurigoBadRequestException(errorResponse.errorMessage)
                "InvalidApiKey" -> throw NurigoInvalidApiKeyException(errorResponse.errorMessage)
                "FailedToAddMessage" -> throw NurigoBadRequestException(errorResponse.errorMessage)
                else -> throw NurigoUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
            }
        }
    }

    /**
     * 단일 메시지 발송 및 다중 메시지(2건 이상) 예약 발송 API
     */
    @Throws
    fun send(parameter: DetailMessageSendingRequest): MultipleDetailMessageSentResponse? {
        val multipleParameter = MultipleDetailMessageSendingRequest(
            messages = listOf(parameter.messages),
            scheduledDate = parameter.scheduledDate
        )

        val response = this.messageHttpService.sendManyDetail(multipleParameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorString = response.errorBody()?.string() ?: "Server error encountered";
            throw NurigoUnknownException(errorString)
        }
    }

    /**
     * 다중 메시지(2건 이상) 발송 및 예약 발송 API
     */
    @Throws
    fun send(parameter: MultipleDetailMessageSendingRequest): MultipleDetailMessageSentResponse? {
        val response = this.messageHttpService.sendManyDetail(parameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorString = response.errorBody()?.string() ?: "Server error encountered";
            throw NurigoUnknownException(errorString)
        }
    }

    /**
     * 다중 메시지(2건 이상) 발송 API
     * @deprecated use Send method
     * */
    @Throws
    fun sendMany(parameter: MultipleMessageSendingRequest): MultipleMessageSentResponse? {
        val response = this.messageHttpService.sendMany(parameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            when (errorResponse.errorCode) {
                "ValidationError" -> throw NurigoBadRequestException(errorResponse.errorMessage)
                "InvalidApiKey" -> throw NurigoInvalidApiKeyException(errorResponse.errorMessage)
                "FailedToAddMessage" -> throw NurigoBadRequestException(errorResponse.errorMessage)
                else -> throw NurigoUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
            }
        }
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
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            throw NurigoUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
        }
    }
}