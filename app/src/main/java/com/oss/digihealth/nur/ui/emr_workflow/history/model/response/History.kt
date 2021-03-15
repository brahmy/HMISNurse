package com.oss.digihealth.nur.ui.emr_workflow.history.model.response

data class History(
    val activity_code: String? = "",
    val activity_icon: String? = "",
    val activity_id: Int? = 0,
    val activity_name: String? = "",
    val activity_route_url: String? = "",
    val ehs_is_active: Boolean? = false,
    val emr_history_settings_id: Int? = 0,
    val facility_uuid: Int? = 0,
    val role_uuid: Int? = 0,
    val user_uuid: Int? = 0,
    val work_flow_order: Int? = 0
)