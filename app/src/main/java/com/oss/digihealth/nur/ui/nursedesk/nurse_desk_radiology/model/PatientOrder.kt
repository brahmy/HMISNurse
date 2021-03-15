package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model

data class PatientOrder(
    val department: String = "",
    val department_uuid: Int = 0,
    val encounter_type: String = "",
    val encounter_type_uuid: Int = 0,
    val order_number: Int = 0,
    val order_request_date: String = "",
    val order_status_uuid: Int = 0,
    val uuid: Int = 0
)