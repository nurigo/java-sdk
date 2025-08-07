package net.nurigo.sdk.message.dto.request.kakao

import net.nurigo.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.*
import net.nurigo.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.EQUALS as DATE_EQUALS
import net.nurigo.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateNameQuery.KakaoAlimtalkTemplateNameQueryCondition.*
import net.nurigo.sdk.message.model.kakao.KakaoAlimtalkTemplateStatus

data class KakaoAlimtalkTemplateListRequest(
    var channelId: String? = null,
    var channelGroupId: String? = null,
    var name: KakaoAlimtalkTemplateNameQuery? = null,
    var templateId: String? = null,
    var isHidden: Boolean? = false,
    var status : KakaoAlimtalkTemplateStatus? = null,
    var isMine: Boolean? = false,
    var startKey: String? = null,
    var limit: Int? = null,
    var dateCreated: KakaoAlimtalkTemplateDateQuery? = null,
    var dateUpdated: KakaoAlimtalkTemplateDateQuery? = null,
) {
    fun generateToQueryParams(): Map<String, String> {
        val params = mutableMapOf<String, String>()

        this.channelGroupId?.let { params["channelGroupId"] = it }
        this.channelId?.let { params["channelId"] = it }
        this.name?.let { it ->
            when (it.queryCondition) {
                EQUALS -> params["name[eq]"] = it.name
                NOT_EQUALS -> params["name[ne]"] = it.name
                LIKE -> params["name[like]"] = it.name
            }
        }
        this.templateId?.let { params["templateId"] = it }
        this.isHidden?.let { params["isHidden"] = it.toString() }
        this.status?.let { params["status"] = it.toString() }
        this.isMine?.let { params["isMine"] = it.toString() }
        this.startKey?.let { params["startKey"] = it }
        this.limit?.let { params["limit"] = it.toString() }
        this.dateCreated?.let { it ->
            when (it.queryCondition) {
                DATE_EQUALS -> params["dateCreated[eq]"] = it.toString()
                GREATER_THEN_OR_EQUAL -> params["dateCreated[gte]"] = it.toString()
                GREATER_THEN -> params["dateCreated[gt]"] = it.toString()
                LESS_THEN_OR_EQUAL -> params["dateCreated[lte]"] = it.toString()
                LESS_THEN -> params["dateCreated[lt]"] = it.toString()
            }
        }
        this.dateUpdated?.let { it ->
            when (it.queryCondition) {
                DATE_EQUALS -> params["dateUpdated[eq]"] = it.toString()
                GREATER_THEN_OR_EQUAL -> params["dateUpdated[gte]"] = it.toString()
                GREATER_THEN -> params["dateUpdated[gt]"] = it.toString()
                LESS_THEN_OR_EQUAL -> params["dateUpdated[lte]"] = it.toString()
                LESS_THEN -> params["dateUpdated[lt]"] = it.toString()
            }
        }

        return params
    }
}
