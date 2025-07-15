package net.nurigo.sdk

import net.nurigo.sdk.message.service.DefaultMessageService

object SolapiClient {

    fun createInstance(apiKey: String, apiSecretKey: String): DefaultMessageService =
        DefaultMessageService(apiKey, apiSecretKey, "https://api.solapi.com")
}