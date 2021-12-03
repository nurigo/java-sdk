package net.nurigo.solapi.sdk.message.request

data class DefaultAgent(
    val sdkVersion: String = "java/4.0.0",
    val osPlatform: String = System.getProperty("os.name")
)
