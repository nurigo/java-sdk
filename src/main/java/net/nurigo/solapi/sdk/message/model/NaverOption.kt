package net.nurigo.solapi.sdk.message.model

import kotlinx.serialization.Serializable

@Serializable
data class NaverOption(
    var talkId: String? = null,
    var templateId: String? = null,
    var variables: MutableMap<String, String> = mutableMapOf()
)
