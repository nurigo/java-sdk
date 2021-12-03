package net.nurigo.solapi.sdk.message.request

abstract class AbstractDefaultMessageRequest {
    var allowDuplicates: Boolean = false
    val agent: DefaultAgent = DefaultAgent()
}