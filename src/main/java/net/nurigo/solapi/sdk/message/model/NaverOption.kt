package net.nurigo.solapi.sdk.message.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class NaverOption(
    var talkId: String? = null,
    var templateId: String? = null,
    var variables: JsonObject? = null
)
