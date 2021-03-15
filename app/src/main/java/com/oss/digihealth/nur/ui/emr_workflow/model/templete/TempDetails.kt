package com.oss.digihealth.nur.ui.emr_workflow.model.templete

data class TempDetails(
    var is_public: Boolean? = false,
    var template_department: Int? = 0,
    var template_description: String? = "",
    var template_displayorder: Int? = 0,
    var template_id: Int? = 0,
    var template_is_active: Boolean? = false,
    var template_name: String? = "",
    var template_status: Boolean? = false,
    var template_type_uuid: Int? = 0,
    var user_uuid: Int? = 0,
    var isSelected: Boolean? = false,
    var itemAppendString: String? = ""
)