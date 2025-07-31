package net.nurigo.sdk.message.dto.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.Message
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class DetailMessageSendingRequest @OptIn(ExperimentalTime::class) constructor(
    val messages: Message,

    @Contextual
    val scheduledDate: Instant?
) : AbstractDefaultMessageRequest()
