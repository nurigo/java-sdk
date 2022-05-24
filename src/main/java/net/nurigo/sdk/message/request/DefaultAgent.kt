package net.nurigo.sdk.message.request

import kotlinx.serialization.Serializable

@Serializable
data class DefaultAgent(
    val sdkVersion: String = "java/4.2.1",
    val osPlatform: String = "${System.getProperty("os.name")} | ${System.getProperty("java.version")}"
)
