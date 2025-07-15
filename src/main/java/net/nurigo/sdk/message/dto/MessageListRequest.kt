package net.nurigo.sdk.message.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.CommonMessageProperty
import net.nurigo.sdk.message.model.MessageStatusType
import java.time.Instant

@Serializable
data class MessageListRequest constructor(
    /**
     * 수신번호
     */
    override var to: String? = null,

    /**
     * 발신번호
     */
    override var from: String? = null,

    /**
     * Pagination을 위한 key
     * 조회 후 다음 페이지로 넘어가려면 조회 당시 마지막의 messageId를 입력해주셔야 합니다.
     */
    var startKey: String? = null,

    /**
     * 조회할 건 수
     */
    var limit: Int? = null,

    /**
     * 메시지 ID
     */
    var messageId: String? = null,

    /**
     * 메시지 ID 목록
     */
    var messageIds: List<String>? = null,

    /**
     * 메시지 그룹 ID
     */
    var groupId: String? = null,

    /**
     * 메시지 유형 (예) ATA, SMS 등)
     */
    var type: String? = null,

    /**
     * 상태코드 (예) -> 2000: 발송 대기, 3000: 발송 중, 4000: 발송완료)
     */
    var statusCode: String? = null,

    /**
     * 조회 할 시작 날짜
     */
    @Contextual
    var startDate: Instant? = null,

    /**
     * 조회 할 종료 날짜
     */
    @Contextual
    var endDate: Instant? = null,

    /**
     * 발송 상태
     */
    var status: MessageStatusType? = null
) : CommonMessageProperty {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageListRequest

        if (to != other.to) return false
        if (from != other.from) return false
        if (startKey != other.startKey) return false
        if (limit != other.limit) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (messageId != other.messageId) return false
        if (messageIds != other.messageIds) return false
        if (groupId != other.groupId) return false
        if (type != other.type) return false
        if (statusCode != other.statusCode) return false
        if (status != other.status) return false

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
        result = 31 * result + (messageIds?.hashCode() ?: 0)
        result = 31 * result + (groupId?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (statusCode?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        return result
    }
}