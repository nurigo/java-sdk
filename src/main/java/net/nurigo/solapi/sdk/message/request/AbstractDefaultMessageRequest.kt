package net.nurigo.solapi.sdk.message.request

import kotlinx.serialization.Serializable

@Serializable
abstract class AbstractDefaultMessageRequest(
    var allowDuplicates: Boolean = false,
    val agent: DefaultAgent = DefaultAgent()
)