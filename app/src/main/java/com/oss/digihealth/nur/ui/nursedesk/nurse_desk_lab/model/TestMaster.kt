package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model

data class TestMaster(
    var code: String? = "",
    var department_uuid: Int? = 0,
    var description: String? = "",
    var is_active: Boolean? = false,
    var name: String? = "",
    var sample_type_uuid: Int? = 0,
    var sub_department_uuid: Int? = 0,
    var uuid: Int? = 0,
    var value_type_uuid: Int? = 0
)