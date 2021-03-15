package com.oss.digihealth.nur.ui.institute.model

data class UserFacility(
    var created_by: Int? = 0,
    var created_date: String? = "",
    var facility_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var revision: Boolean? = false,
    var status: Boolean? = false,
    var user_uuid: Int? = 0,
    var uuid: Int? = 0
)