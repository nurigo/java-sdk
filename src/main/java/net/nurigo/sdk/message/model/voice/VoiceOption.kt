package net.nurigo.sdk.message.model.voice

import kotlinx.serialization.Serializable

@Serializable
data class VoiceOption(
    var voiceType: VoiceType = VoiceType.FEMALE,
    var headerMessage: String? = null,
    var tailMessage: String? = null,
    var replyRange: Int? = null,
    var counselorNumber: String? = null
) {
    init {
        require(!(replyRange != null && counselorNumber != null)) { "replyRange와 counselorNumber는 같이 사용할 수 없습니다." }
    }
}