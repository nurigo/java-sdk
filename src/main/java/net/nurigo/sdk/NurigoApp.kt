package net.nurigo.sdk

import net.nurigo.sdk.message.service.DefaultMessageService
import java.net.URL

object NurigoApp {

    fun initialize(apiKey: String, apiSecretKey: String, domain: URL): DefaultMessageService {
        return DefaultMessageService(apiKey, apiSecretKey, domain)
    }
}