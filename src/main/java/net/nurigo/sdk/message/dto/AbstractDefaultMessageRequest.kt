package net.nurigo.sdk.message.dto

import kotlinx.serialization.Serializable

@Serializable
abstract class AbstractDefaultMessageRequest(
    var allowDuplicates: Boolean = false,
    val agent: DefaultAgent = DefaultAgent()
)