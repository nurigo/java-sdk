package net.nurigo.sdk.message.dto.request.kakao

import kotlinx.serialization.Contextual
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class KakaoAlimtalkTemplateDateQuery(
    @Contextual
    val date: LocalDateTime,
    val queryCondition: KakaoAlimtalkTemplateDateQueryCondition,
) {
    enum class KakaoAlimtalkTemplateDateQueryCondition {
        EQUALS,
        GREATER_THEN_OR_EQUAL,
        GREATER_THEN,
        LESS_THEN_OR_EQUAL,
        LESS_THEN
    }
}
