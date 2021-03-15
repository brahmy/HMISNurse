package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model

data class ResponseContentX(
    val analyte_uom: String = "",
    val cancel_reason: Any = Any(),
    val canceled_by: Any = Any(),
    val canceled_datetime: Any = Any(),
    val patient_order_test_detail_uuid: Int = 0,
    val qualifier_value: Any = Any(),
    val result_value: String = "",
    val test_or_analyte: String = "",
    val test_or_analyte_ref_max: Any = Any(),
    val test_or_analyte_ref_min: Any = Any(),
    val uuid: Int = 0
)