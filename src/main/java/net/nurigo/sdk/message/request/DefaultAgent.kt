package net.nurigo.sdk.message.request

import kotlinx.serialization.Serializable

@Serializable
data class DefaultAgent(
    var sdkVersion: String? = "java/4.0.6",
    var osPlatform: String? = System.getProperty("os.name")
)
