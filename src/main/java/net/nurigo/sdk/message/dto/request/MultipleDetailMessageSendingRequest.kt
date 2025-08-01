package net.nurigo.sdk.message.dto.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.Message
import kotlin.time.Instant

@Serializable
data class MultipleDetailMessageSendingRequest(
    var messages: List<Message> = emptyList(),
    @Contextual
    var scheduledDate: Instant? = null,
    var showMessageList: Boolean = false,
) : AbstractDefaultMessageRequest()
