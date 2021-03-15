package com.oss.digihealth.nur.ui.emr_workflow.model

data class StoreMasterresponseContent(
    val created_by: Int? = 0,
    val created_date: String? = "",
    val department_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val store_master: StoreMaster? = StoreMaster(),
    val store_master_uuid: Int? = 0,
    val store_type_uuid: Int? = 0,
    val uuid: Int? = 0
)