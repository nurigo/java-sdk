package net.nurigo.sdk.message.response

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String
)
