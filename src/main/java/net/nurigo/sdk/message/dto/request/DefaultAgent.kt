package net.nurigo.sdk.message.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class DefaultAgent(
    val sdkVersion: String = "java/4.4.0",
    val osPlatform: String = "${System.getProperty("os.name")} | ${System.getProperty("java.version")} | ${System.getProperty("os.arch")}"
)
