package com.oss.digihealth.doc.ui.login.model.login_response_model

data class ModuleDetail(
    var code: String? = "",
    var display_order: Int? = 0,
    var icon: String? = "",
    var is_report: Boolean? = false,
    var name: String? = "",
    var role_activities: List<RoleActivity?>? = listOf(),
    var route_url: String? = "",
    var uuid: Int? = 0
)