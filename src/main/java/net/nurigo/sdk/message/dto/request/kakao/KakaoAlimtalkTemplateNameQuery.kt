package net.nurigo.sdk.message.dto.request.kakao

data class KakaoAlimtalkTemplateNameQuery(
    val name: String,
    val queryCondition: KakaoAlimtalkTemplateNameQueryCondition,
) {
    enum class KakaoAlimtalkTemplateNameQueryCondition {
        EQUALS,
        NOT_EQUALS,
        LIKE
    }
}
