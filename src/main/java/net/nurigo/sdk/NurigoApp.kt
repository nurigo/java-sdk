package net.nurigo.sdk

import net.nurigo.sdk.message.service.DefaultMessageService

object NurigoApp {

    fun initialize(apiKey: String, apiSecretKey: String): DefaultMessageService {
        return DefaultMessageService(apiKey, apiSecretKey, "https://api.solapi.com")
    }
}