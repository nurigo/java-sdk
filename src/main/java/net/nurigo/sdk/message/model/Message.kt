package net.nurigo.sdk.message.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    var kakaoOptions: KakaoOption? = null,
    var naverOptions: NaverOption? = null,
    var rcsOptions: RcsOption? = null,
    var type: String? = null,
    var country: String? = null,
    var subject: String? = null,
    var imageId: String? = null,
    var dateProcessed: String? = null,
    var dateReported: String? = null,
    var statusCode: String? = null,
    var replacement: Boolean? = null,
    var autoTypeDetect: Boolean? = null,
    var status: String? = null,
    var messageId: String? = null,
    var groupId: String? = null,
    var accountId: String? = null,
    var text: String? = null,
    var dateCreated: Instant? = null,
    var dateUpdated: Instant? = null,
    var to: String? = null,
    var from: String? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (kakaoOptions != other.kakaoOptions) return false
        if (naverOptions != other.naverOptions) return false
        if (rcsOptions != other.rcsOptions) return false
        if (type != other.type) return false
        if (country != other.country) return false
        if (subject != other.subject) return false
        if (imageId != other.imageId) return false
        if (dateProcessed != other.dateProcessed) return false
        if (dateReported != other.dateReported) return false
        if (statusCode != other.statusCode) return false
        if (replacement != other.replacement) return false
        if (autoTypeDetect != other.autoTypeDetect) return false
        if (status != other.status) return false
        if (messageId != other.messageId) return false
        if (groupId != other.groupId) return false
        if (accountId != other.accountId) return false
        if (text != other.text) return false
        if (dateCreated != other.dateCreated) return false
        if (dateUpdated != other.dateUpdated) return false
        if (from != other.from) return false
        if (to != other.to) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kakaoOptions?.hashCode() ?: 0
        result = 31 * result + (naverOptions?.hashCode() ?: 0)
        result = 31 * result + (rcsOptions?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + country.hashCode()
        result = 31 * result + (subject?.hashCode() ?: 0)
        result = 31 * result + (imageId?.hashCode() ?: 0)
        result = 31 * result + (dateProcessed?.hashCode() ?: 0)
        result = 31 * result + (dateReported?.hashCode() ?: 0)
        result = 31 * result + (statusCode?.hashCode() ?: 0)
        result = 31 * result + (replacement?.hashCode() ?: 0)
        result = 31 * result + (autoTypeDetect?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (messageId?.hashCode() ?: 0)
        result = 31 * result + (groupId?.hashCode() ?: 0)
        result = 31 * result + (accountId?.hashCode() ?: 0)
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (dateCreated?.hashCode() ?: 0)
        result = 31 * result + (dateUpdated?.hashCode() ?: 0)
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (to?.hashCode() ?: 0)
        return result
    }
}
