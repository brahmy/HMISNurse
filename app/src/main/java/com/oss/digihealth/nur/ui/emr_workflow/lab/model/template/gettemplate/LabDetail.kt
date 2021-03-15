package com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.gettemplate

data class LabDetail(
    var lab_code: String? = "",
    var lab_name: String? = "",
    var lab_test_description: String? = "",
    var lab_test_is_active: Boolean? = false,
    var lab_test_status: Boolean? = false,
    var lab_test_uuid: Int? = 0,
    var lab_type_uuid: Int? = 0,
    var template_details_displayorder: Int? = 0,
    var template_details_uuid: Int? = 0,
    var profile_test_uuid : Int?=0,
    var profile_test_code : Int?=0,
    var profile_test_name : String?="",
    var profile_test_description : String ?="",
    var profile_test_status : String ?="",
    var profile_test_active : String ?=""
)