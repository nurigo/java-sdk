package net.nurigo.sdk

import net.nurigo.sdk.message.service.DefaultMessageService

object NurigoApp {

    fun initialize(apiKey: String, apiSecretKey: String, domain: String): DefaultMessageService {
        return DefaultMessageService(apiKey, apiSecretKey, domain)
    }
}