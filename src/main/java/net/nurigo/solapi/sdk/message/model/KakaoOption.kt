package net.nurigo.solapi.sdk.message.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class KakaoOption(
    var pfId: String? = null,
    var templateId: String? = null,
    var variables: JsonObject? = null
)