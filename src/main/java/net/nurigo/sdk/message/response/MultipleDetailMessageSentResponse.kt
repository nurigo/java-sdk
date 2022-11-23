package net.nurigo.sdk.message.response

import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.FailedMessage

@Serializable
data class MultipleDetailMessageSentResponse(
    var failedMessageList: List<FailedMessage> = emptyList(),
    var groupInfo: MultipleMessageSentResponse? = null,
    var messageList: List<MessageList>? = null
) {
    @Serializable
    data class MessageList(
        var messageId: String? = null,
        var statusCode: String? = null,
        var customFields: Map<String, String>? = null,
        var statusMessage: String? = null
    )
}
