package net.nurigo.sdk.message.response

import kotlinx.serialization.Serializable

@Serializable
data class FileUploadResponse(
    var fileId: String? = null
)
