package com.oss.digihealth.nur.ui.emr_workflow.lab.model

data class FavAddResponseContent(
    val clinical_type_uuid: Int = 0,
    val code: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val department_type_uuid: Int = 0,
    val end_age: Int = 0,
    val is_active: Boolean = false,
    val is_gender_based: Boolean = false,
    val modified_by: Int = 0,
    val is_speciality:Any?=null,
    val modified_date: String = "",
    val name: String = "",
    val revision: Int = 0,
    val short_code: String = "",
    val start_age: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)