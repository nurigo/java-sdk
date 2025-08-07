package net.nurigo.sdk.message.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.nurigo.sdk.message.model.kakao.KakaoOption
import net.nurigo.sdk.message.model.naver.NaverOption
import net.nurigo.sdk.message.model.rcs.RcsOption
import kotlin.time.Instant

@Serializable
data class Message (
    /**
     * 카카오 알림톡, 친구톡 발송을 위한 파라미터
     */
    var kakaoOptions: KakaoOption? = null,

    /**
     * 네이버 스마트알림 발송을 위한 파라미터
     */
    var naverOptions: NaverOption? = null,

    /**
     * RCS 발송을 위한 파라미터
     */
    var rcsOptions: RcsOption? = null,

    /**
     * 메시지 발송 타입
     * 예) SMS, LMS, MMS, ATA...
     */
    var type: MessageType? = null,

    /**
     * 국가번호
     * +82, +1...
     */
    var country: String? = null,

    /**
     * LMS, MMS 발송 시 문자 내 제목
     */
    var subject: String? = null,

    /**
     * MMS 발송 시 시스템에 업로드 된 image ID
     */
    var imageId: String? = null,

    /**
     *     * 발송 접수일자
     */
    @Contextual
    var dateProcessed: Instant? = null,

    /**
     * 통신사 결과 값 통보일자
     */
    @Contextual
    var dateReported: Instant? = null,

    /**
     * 실제 메시지 발송 완료일자
     */
    @Contextual
    var dateReceived: Instant? = null,

    /**
     * 메시지 상태코드
     */
    var statusCode: String? = null,

    /**
     * 대체 발송 여부
     */
    var replacement: Boolean? = null,

    /**
     * 메시지 타입 자동 구분 여부
     * 기본값: 허용(true)
     */
    var autoTypeDetect: Boolean? = null,

    /**
     * 문자 상태
     * 예) 대기, 접수, 발송완료 등
     * 해당 프로퍼티는 MessageStatusType과 다른 값입니다!
     */
    var status: String? = null,

    /**
     * 메시지 ID
     */
    var messageId: String? = null,

    /**
     * 그룹 ID
     */
    var groupId: String? = null,

    /**
     * 계정 고유번호
     */
    var accountId: String? = null,

    /**
     * 발송 할(된) 메시지 내용
     */
    var text: String? = null,

    /**
     * 메시지 생성일자
     */
    @Contextual
    var dateCreated: Instant? = null,

    /**
     * 메시지 수정일자
     */
    @Contextual
    var dateUpdated: Instant? = null,

    /**
     * 수신번호
     */
    var to: String? = null,

    /**
     * 발신번호, 반드시 계정 내 등록하신 발신번호를 입력하셔야 합니다.
     * 예) 029302266
     */
    var from: String? = null,

    /**
     * 사용자(누리고 서비스 이용자)를 위한 발송 요청 시 커스텀 값을 넣을 수 있는 필드
     * 메시지 조회 시에도 표시됩니다!
     */
    var customFields: Map<String, String>? = null,

    /**
     * 대체발송 파라미터
     */
    var replacements: List<Message>? = null
) {

    init {
        from = from?.replace("-", "")
        to = to?.replace("-", "")
    }

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
        if (customFields != other.customFields) return false

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
        result = 31 * result + (customFields?.hashCode() ?: 0)
        return result
    }
}
