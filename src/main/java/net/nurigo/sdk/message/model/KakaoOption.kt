package net.nurigo.sdk.message.model

import kotlinx.serialization.Serializable

@Serializable
data class KakaoOption(
    var pfId: String? = null,
    var templateId: String? = null,
    var variables: MutableMap<String, String> = mutableMapOf(),
    var disableSms: Boolean = false,
    var imageId: String? = null,
    var adFlag: Boolean = false
)