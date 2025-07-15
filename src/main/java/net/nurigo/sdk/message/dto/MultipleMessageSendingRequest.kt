package net.nurigo.sdk.message.dto

import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.Message

@Serializable
data class MultipleMessageSendingRequest(
    val messages: List<Message>
) : AbstractDefaultMessageRequest()
