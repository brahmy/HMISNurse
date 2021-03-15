package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model

data class Req(
    val created_by: String = "",
    val modified_by: Int = 0,
    val nurse_collected_by: String = "",
    val nurse_collected_date: String = "",
    val patient_order_test_details_uuid: Int = 0,
    val ward_uuid: String = ""
)