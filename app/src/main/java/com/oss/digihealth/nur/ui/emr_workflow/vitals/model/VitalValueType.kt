package com.oss.digihealth.nur.ui.emr_workflow.vitals.model

data class VitalValueType(
    val code: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "",
    val revision: Int? =0,
    val status: Boolean = false,
    val uuid: Int = 0
)