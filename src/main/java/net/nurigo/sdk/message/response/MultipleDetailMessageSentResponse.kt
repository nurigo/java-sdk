package net.nurigo.sdk.message.response

import kotlinx.serialization.Serializable

@Serializable
data class MultipleDetailMessageSentResponse(
    var failedMessageList: List<Map<String, String>?> = emptyList(),
    var groupInfo: MultipleMessageSentResponse? = null
)
