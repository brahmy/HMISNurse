package com.oss.digihealth.nur.ui.dashboard.model

data class GetGenderResp(
    var req: String?,
    var responseContents: List<ResponseContent?>?,
    var statusCode: Int?
) {
    data class ResponseContent(
        var Is_default: Boolean? = false,
        var code: String? = "",
        var color: String? = "",
        var created_by: Int? = 0,
        var created_date: String? = "",
        var display_order: Int? = 0,
        var is_active: Boolean? = false,
        var language_uuid: Any? = 0,
        var modified_by: Int? = 0,
        var modified_date: String? = "",
        var name: String? = "",
        var revision: Int? = 0,
        var uuid: Int? = 0
    )
}
