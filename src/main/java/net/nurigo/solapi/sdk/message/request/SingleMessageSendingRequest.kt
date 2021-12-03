package net.nurigo.solapi.sdk.message.request

import kotlinx.serialization.Serializable
import net.nurigo.solapi.sdk.message.model.Message

@Serializable
data class SingleMessageSendingRequest(
    val message: Message,
): AbstractDefaultMessageRequest()
