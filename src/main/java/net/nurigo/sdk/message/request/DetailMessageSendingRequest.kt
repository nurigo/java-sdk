package net.nurigo.sdk.message.request

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.Message

@Serializable
data class DetailMessageSendingRequest(
    val messages: Message,
    val scheduledDate: Instant?
) : AbstractDefaultMessageRequest()
