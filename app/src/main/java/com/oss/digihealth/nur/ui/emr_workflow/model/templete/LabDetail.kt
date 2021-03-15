package com.oss.digihealth.nur.ui.emr_workflow.model.templete

data class LabDetail(
    var lab_code: String? = "",
    var lab_name: String? = "",
    var lab_test_description: String? = "",
    var lab_test_is_active: Boolean? = false,
    var lab_test_status: Boolean? = false,
    var lab_test_uuid: Int? = 0,
    var ischeck: Boolean = false,
    var lab_type_uuid: Int? = 0,
    var template_details_displayorder: Int? = 0,
    var template_details_uuid: Int? = 0
)