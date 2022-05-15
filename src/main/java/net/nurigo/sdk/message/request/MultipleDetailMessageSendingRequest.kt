package net.nurigo.sdk.message.request

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.Message

@Serializable
data class MultipleDetailMessageSendingRequest(
    val messages: List<Message>,
    val scheduledDate: Instant?
) : AbstractDefaultMessageRequest()
