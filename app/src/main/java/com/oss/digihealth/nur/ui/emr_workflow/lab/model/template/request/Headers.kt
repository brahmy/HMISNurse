package com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request
data class Headers(
    var department_uuid: String? = "",
    var description: String? = "",
    var diagnosis_uuid: Int? = 0,
    var display_order: String? = "",
    var facility_uuid: String? = "",
    var is_active: Boolean? = false,
    var is_public: String? = "",
    var name: String? = "",
    var revision: Boolean? = false,
    var template_type_uuid: Int? = 0,
    var template_id:Int?=0,
    var lab_uuid : Int?=0
)