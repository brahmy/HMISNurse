package com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.response

data class Headers(
    var active_from: String? = "",
    var active_to: String? = "",
    var created_by: String? = "",
    var created_date: String? = "",
    var department_uuid: String? = "",
    var description: String? = "",
    var diagnosis_uuid: Int? = 0,
    var display_order: String? = "",
    var facility_uuid: String? = "",
    var is_active: Int? = 0,
    var is_public: String? = "",
    var modified_by: String? = "",
    var modified_date: String? = "",
    var name: String? = "",
    var revision: Int? = 0,
    var status: Int? = 0,
    var template_type_uuid: Int? = 0,
    var user_uuid: String? = ""
)