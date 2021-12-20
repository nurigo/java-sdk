package net.nurigo.sdk.message.request

import kotlinx.serialization.Serializable

@Serializable
data class DefaultAgent(
    val sdkVersion: String = "java/4.1.3",
    val osPlatform: String = System.getProperty("os.name")
)
