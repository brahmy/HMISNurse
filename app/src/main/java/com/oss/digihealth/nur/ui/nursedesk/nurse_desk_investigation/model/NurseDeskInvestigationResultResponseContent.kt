package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model

data class NurseDeskInvestigationResultResponseContent(
    var cancel_reason: Any? = Any(),
    var canceled_by: Any? = Any(),
    var canceled_datetime: Any? = Any(),
    var cancelled_by: Any? = Any(),
    var comments: Any? = Any(),
    var created_date: String? = "",
    var encounter_uuid: Int? = 0,
    var image_url: List<ImageUrl?>? = listOf(),
//    var patient_order: PatientOrderX? = PatientOrder(),
    var patient_order_test_detail_uuid: Int? = 0,
    var patient_order_uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var patient_work_order_uuid: Int? = 0,
    var reject_category: Any? = Any(),
    var result_value: String? = "",
    var test_master: TestMasterXX? = TestMasterXX(),
    var test_master_uuid: Int? = 0,
    var uuid: Int? = 0,
    var work_order_attachment_uuid: Any? = Any(),

//    var work_order_status: WorkOrderStatusX? = WorkOrderStatus(),
    var work_order_status_uuid: Int? = 0
)