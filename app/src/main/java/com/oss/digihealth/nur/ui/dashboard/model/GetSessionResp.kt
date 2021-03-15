package com.oss.digihealth.nur.ui.dashboard.model

data class GetSessionResp(
    var req: String?,
    var responseContents: List<ResponseContent?>? = listOf(),
    var statusCode: Int?
) {
    data class ResponseContent(
        var Is_default: Any?= false,
        var code: String? = "",
        var color: Any? = "",
        var created_by: Int? = 0,
        var created_date: String?= "",
        var display_order: Any? = "",
        var is_active: Boolean?= false,
        var language_uuid: Any?= 0,
        var modified_by: Int?= 0,
        var modified_date: String?= "",
        var name: String?= "",
        var revision: Int?= 0,
        var status: Boolean?= false,
        var uuid: Int?= 0
    )
}