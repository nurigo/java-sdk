package net.nurigo.solapi.sdk.message.response

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(
    val errorCode: ErrorCode,
    val errorMessage: String
)
