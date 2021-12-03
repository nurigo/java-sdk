package net.nurigo.solapi.sdk.message.request

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import net.nurigo.solapi.sdk.message.model.CommonMessageProperty

@Serializable
data class MessageListRequest(
    override var to: String? = null,
    override var from: String? = null,
    var startKey: String? = null,
    var limit: Int? = null,
    var dateType: String? = null,
    var startDate: Instant? = null,
    var endDate: Instant? = null,
    var messageId: String? = null,
    var messageIds: List<String>? = null,
    var groupId: String? = null,
    var type: String? = null,
    var statusCode: String? = null,
    var dateCreated: Instant? = null,
    var dateUpdated: Instant? = null
) : CommonMessageProperty {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageListRequest

        if (to != other.to) return false
        if (from != other.from) return false
        if (startKey != other.startKey) return false
        if (limit != other.limit) return false
        if (dateType != other.dateType) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (messageId != other.messageId) return false
        if (messageIds != other.messageIds) return false
        if (groupId != other.groupId) return false
        if (type != other.type) return false
        if (statusCode != other.statusCode) return false
        if (dateCreated != other.dateCreated) return false
        if (dateUpdated != other.dateUpdated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = to?.hashCode() ?: 0
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (startKey?.hashCode() ?: 0)
        result = 31 * result + (limit ?: 0)
        result = 31 * result + (dateType?.hashCode() ?: 0)
        result = 31 * result + (startDate?.hashCode() ?: 0)
        result = 31 * result + (endDate?.hashCode() ?: 0)
        result = 31 * result + (messageId?.hashCode() ?: 0)
        result = 31 * result + (messageIds?.hashCode() ?: 0)
        result = 31 * result + (groupId?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (statusCode?.hashCode() ?: 0)
        result = 31 * result + (dateCreated?.hashCode() ?: 0)
        result = 31 * result + (dateUpdated?.hashCode() ?: 0)
        return result
    }
}