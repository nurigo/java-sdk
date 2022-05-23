package net.nurigo.sdk.message.response

import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.FailedMessage

@Serializable
data class MultipleDetailMessageSentResponse(
    var failedMessageList: List<FailedMessage> = emptyList(),
    var groupInfo: MultipleMessageSentResponse? = null
)
