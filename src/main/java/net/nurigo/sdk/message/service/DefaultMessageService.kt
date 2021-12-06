package net.nurigo.sdk.message.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.nurigo.sdk.message.exception.SolapiBadRequestException
import net.nurigo.sdk.message.exception.SolapiInvalidApiKeyException
import net.nurigo.sdk.message.exception.SolapiUnknownException
import net.nurigo.sdk.message.extension.toStringValueMap
import net.nurigo.sdk.message.lib.Authenticator
import net.nurigo.sdk.message.request.MessageListRequest
import net.nurigo.sdk.message.request.MultipleMessageSendingRequest
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.response.ErrorCode.*
import net.nurigo.sdk.message.response.ErrorResponse
import net.nurigo.sdk.message.response.MessageListResponse
import net.nurigo.sdk.message.response.MultipleMessageSentResponse
import net.nurigo.sdk.message.response.SingleMessageSentResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.URL
import kotlin.jvm.Throws

class DefaultMessageService(apiKey: String, apiSecretKey: String, domain: URL) : MessageService {
    private var messageHttpService: MessageHttpService

    // TODO: Client initializer 셋팅해야 함
    init {
        val interceptor = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
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

    @Throws
    fun getMessageList(parameter: MessageListRequest = MessageListRequest()): MessageListResponse? {
        val mappedParameter = parameter.toStringValueMap()
        val response = this.messageHttpService.getMessageList(mappedParameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            throw SolapiUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
        }
    }

    @Throws
    fun sendOne(parameter: SingleMessageSendingRequest): SingleMessageSentResponse? {
        val response = this.messageHttpService.sendOne(parameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            when (errorResponse.errorCode) {
                ValidationError -> throw SolapiBadRequestException(errorResponse.errorMessage)
                InvalidApiKey -> throw SolapiInvalidApiKeyException(errorResponse.errorMessage)
                FailedToAddMessage -> throw SolapiBadRequestException(errorResponse.errorMessage)
                else -> throw SolapiUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
            }
        }
    }

    @Throws
    fun sendMany(parameter: MultipleMessageSendingRequest): MultipleMessageSentResponse? {
        val response = this.messageHttpService.sendMany(parameter).execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorResponse: ErrorResponse = Json.decodeFromString(response.errorBody()?.string() ?: "")
            when (errorResponse.errorCode) {
                ValidationError -> throw SolapiBadRequestException(errorResponse.errorMessage)
                InvalidApiKey -> throw SolapiInvalidApiKeyException(errorResponse.errorMessage)
                FailedToAddMessage -> throw SolapiBadRequestException(errorResponse.errorMessage)
                else -> throw SolapiUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
            }
        }
    }
}