package net.nurigo.solapi.sdk.message.response

import kotlinx.serialization.Serializable
import net.nurigo.solapi.sdk.message.model.Message

@Serializable
data class MessageListResponse(
    var startKey: String? = null,
    var limit: Int? = null,
    var messageList: Map<String, Message> = emptyMap()
)
