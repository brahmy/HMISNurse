package com.oss.digihealth.nur.ui.emr_workflow.lab.model

data class FavAddTestNameResponseContent(
    val code: String = "",
    val department_uuid: String = "",
    val is_active: Boolean = false,
    val name: String = "",
    val profile_code: String = "",
    val short_code: Any = Any(),
    val status: Boolean = false,
    val sub_department_uuid: String = "",
    val type: String = "",
    val uuid: Int = 0
)