package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.samplecollection

data class Req(
    var created_by: String? = "",
    var modified_by: Int? = 0,
    var nurse_collected_by: String? = "",
    var nurse_collected_date: String? = "",
    var patient_order_test_details_uuid: Int? = 0,
    var ward_uuid: String? = ""
)