package net.nurigo.sdk.message.dto.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.CommonMessageProperty
import java.time.Instant

@Serializable
data class MessageListBaseRequest constructor(
    override var to: String? = null,
    override var from: String? = null,
    var startKey: String? = null,
    var limit: Int? = null,
    var messageId: String? = null,
    var groupId: String? = null,
    var type: String? = null,
    var statusCode: String? = null,
    var criteria: String? = null,
    var cond: String? = null,
    var value: String? = null,

    @Contextual
    var startDate: Instant? = null,

    @Contextual
    var endDate: Instant? = null
) : CommonMessageProperty {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageListBaseRequest

        if (to != other.to) return false
        if (from != other.from) return false
        if (startKey != other.startKey) return false
        if (limit != other.limit) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (messageId != other.messageId) return false
        if (groupId != other.groupId) return false
        if (type != other.type) return false
        if (statusCode != other.statusCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = to?.hashCode() ?: 0
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (startKey?.hashCode() ?: 0)
        result = 31 * result + (limit ?: 0)
        result = 31 * result + (startDate?.hashCode() ?: 0)
        result = 31 * result + (endDate?.hashCode() ?: 0)
        result = 31 * result + (messageId?.hashCode() ?: 0)
        result = 31 * result + (groupId?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (statusCode?.hashCode() ?: 0)
        return result
    }
}