package net.nurigo.sdk.message.exception

import net.nurigo.sdk.message.model.FailedMessage

class NurigoMessageNotReceivedException(message: String) : NurigoException,
    Exception(message) {
    var failedMessageList: List<FailedMessage> = emptyList()

}