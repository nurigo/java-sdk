package net.nurigo.sdk.message.lib

import kotlinx.serialization.json.Json
import net.nurigo.sdk.message.exception.*
import net.nurigo.sdk.message.model.Count
import net.nurigo.sdk.message.dto.MultipleDetailMessageSendingRequest
import net.nurigo.sdk.message.response.ErrorResponse
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse
import net.nurigo.sdk.message.service.MessageHttpService

/**
 * 메시지 발송 요청을 처리하는 공통 헬퍼 메소드
 */
@Throws(
    NurigoMessageNotReceivedException::class, NurigoEmptyResponseException::class, NurigoUnknownException::class
)
fun processSendRequest(
    messageHttpService: MessageHttpService,
    parameter: MultipleDetailMessageSendingRequest
): MultipleDetailMessageSentResponse {
    val response = messageHttpService.sendManyDetail(parameter).execute()

    if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
            val count: Count = responseBody.groupInfo?.count ?: Count()
            val failedMessageList = responseBody.failedMessageList

            if (failedMessageList.isNotEmpty() && count.total == failedMessageList.count()) {
                val messageNotReceivedException = NurigoMessageNotReceivedException("메시지 발송 접수에 실패했습니다.")
                messageNotReceivedException.failedMessageList = failedMessageList
                throw messageNotReceivedException
            }

            return responseBody
        }
        throw NurigoEmptyResponseException("서버로부터 아무 응답을 받지 못했습니다.")
    } else {
        val errorString = response.errorBody()?.string() ?: "Server error encountered"
        throw NurigoUnknownException(errorString)
    }
}

/**
 * 에러 응답을 처리하는 공통 헬퍼 메소드
 */
@Throws(NurigoBadRequestException::class, NurigoInvalidApiKeyException::class, NurigoUnknownException::class)
fun handleErrorResponse(errorBody: String?): Nothing {
    val errorResponse: ErrorResponse = Json.decodeFromString(errorBody ?: "")
    when (errorResponse.errorCode) {
        "ValidationError" -> throw NurigoBadRequestException(errorResponse.errorMessage)
        "InvalidApiKey" -> throw NurigoInvalidApiKeyException(errorResponse.errorMessage)
        "FailedToAddMessage" -> throw NurigoBadRequestException(errorResponse.errorMessage)
        else -> throw NurigoUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
    }
}