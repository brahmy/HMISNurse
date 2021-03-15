package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model

data class ResponseContents(
    val created_by: String = "",
    val created_date: String = "",
    val is_active: Boolean = false,
    val is_nurse_collected: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val nurse_collected_by: String = "",
    val nurse_collected_date: String = "",
    val patient_order_test_details_uuid: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0,
    val ward_uuid: String = ""
)