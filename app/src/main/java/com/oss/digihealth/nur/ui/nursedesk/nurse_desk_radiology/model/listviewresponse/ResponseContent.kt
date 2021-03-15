package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.listviewresponse

data class ResponseContent(
    var created_date: String? = "",
    var department_name: String? = "",
    var department_uuid: Int? = 0,
    var doctor_uuid: Int? = 0,
    var encounter_type_name: String? = "",
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var facility_name: String? = "",
    var facility_uuid: Int? = 0,
    var modified_date: String? = "",
    var order_request_date: String? = "",
    var order_schedule_date: Any? = Any(),
    var order_to_location_uuid: Int? = 0,
    var patient_order_test_details: List<PatientOrderTestDetail?>? = listOf(),
    var patient_uuid: Int? = 0,
    var to_location: Any? = Any(),
    var uuid: Int? = 0,
    var vw_patient_info: VwPatientInfo? = VwPatientInfo(),
    var vw_user_info: VwUserInfo? = VwUserInfo()
)