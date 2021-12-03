package net.nurigo.solapi.sdk.message.request

import kotlinx.serialization.Serializable

@Serializable
data class DefaultAgent(
    var sdkVersion: String? = "java/4.0.0",
    var osPlatform: String? = System.getProperty("os.name")
)
