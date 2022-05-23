package net.nurigo.sdk.message.model

import kotlinx.serialization.Serializable

@Serializable
data class Count(
    /**
     * 그룹에 적재된 총 메시지 건 수
     */
    var total: Int? = null,
    /**
     * 실제 발송접수 된 메시지 건 수
     */
    var sentTotal: Int? = null,
    /**
     * 발송 접수 후 발송실패 된 메시지 건 수
     */
    var sentFailed: Int? = null,
    /**
     * 발송 접수 후 발송성공 된 메시지 건 수
     */
    var sentSuccess: Int? = null,
    /**
     * 발송 접수 후 발송 중인 메시지 건 수
     */
    var sentPending: Int? = null,
    /**
     * 발송 접수 후 대체발송 된 메시지 건 수
     */
    var sentReplacement: Int? = null,
    /**
     * 발송 실패 후 환급될 메시지 건 수
     */
    var refund: Int? = null,
    /**
     * 발송 접수에 실패한 메시지 건 수
     */
    var registeredFailed: Int? = null,
    /**
     * 발송 접수에 성공한 메시지 건 수
     */
    var registeredSuccess: Int? = null
)
