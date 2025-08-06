package net.nurigo.sdk.message.model.kakao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 할당 유형을 정의합니다.
 */
@Serializable
enum class KakaoAlimtalkAssignType {
    /**
     * 채널 할당
     */
    @SerialName("CHANNEL")
    CHANNEL
}
