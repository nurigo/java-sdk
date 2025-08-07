package net.nurigo.sdk.message.dto.request.kakao

import kotlin.time.Instant

data class KakaoAlimtalkTemplateDateQuery(
    val date: Instant,
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
