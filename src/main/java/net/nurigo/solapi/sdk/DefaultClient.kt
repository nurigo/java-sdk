package net.nurigo.solapi.sdk

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import net.nurigo.solapi.sdk.message.service.MessageService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import kotlin.jvm.Throws

object DefaultClient {

    private const val PROTOCOL = "https"
    private const val DOMAIN = "api.solapi.com"

    @Throws
    fun initialize(
        service: Class<MessageService> = MessageService::class.java,
        apiKey: String,
        apiSecretKey: String
    ): MessageService {
        val interceptor = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val authInfo = Authenticator(apiKey, apiSecretKey).generateAuthInfo()
                val request: Request = chain.request().newBuilder().addHeader("Authorization", authInfo).build()
                chain.proceed(request)
            }
            .build()
        val contentType = "application/json".toMediaType()
        val jsonConfig = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl("${PROTOCOL}://${DOMAIN}")
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .client(client)
            .build()
            .create(service)
    }
}