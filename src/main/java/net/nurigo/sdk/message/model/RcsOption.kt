package net.nurigo.sdk.message.model

import kotlinx.serialization.Serializable

@Serializable
data class RcsOption(
    var brandId: String? = null,
    // TODO: Enum으로 해야 함
    var mmsType: String? = null,
    var templateId: String? = null,
    var variables: MutableMap<String, String> = mutableMapOf()
)
