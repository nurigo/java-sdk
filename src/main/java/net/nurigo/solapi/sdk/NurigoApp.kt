package net.nurigo.solapi.sdk

import net.nurigo.solapi.sdk.message.service.DefaultMessageService

object NurigoApp {

    fun initialize(apiKey: String, apiSecretKey: String): DefaultMessageService {
        return DefaultMessageService(apiKey, apiSecretKey)
    }
}