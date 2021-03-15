package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.samplecollection

data class ResponseContentsnurselabsamplecollection(
    var created_by: String? = "",
    var created_date: String? = "",
    var is_active: Boolean? = false,
    var is_nurse_collected: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var nurse_collected_by: String? = "",
    var nurse_collected_date: String? = "",
    var patient_order_test_details_uuid: Int? = 0,
    var status: Boolean? = false,
    var uuid: Int? = 0,
    var ward_uuid: String? = ""
)