package com.oss.digihealth.doc.ui.login.model.login_response_model

data class ActivityDetail(
    var active_from: String? = "",
    var active_to: String? = "",
    var activity: Activity? = Activity(),
    var activity_uuid: Int? = 0,
    var created_by: Long? = null,
    var created_date: Any? = Any(),
    var is_active: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var revision: Int? = 0,
    var role_code: String? = "",
    var role_description: String? = "",
    var role_name: String? = "",
    var status: Boolean? = false,
    var uuid: Int? = 0
)