package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model

import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.ImageUrl

data class NUrseDeskRadiologyResultResponseContent(
    val cancel_reason: Any = Any(),
    val canceled_by: Any = Any(),
    val canceled_datetime: Any = Any(),
    val created_date: String = "",
    val encounter_uuid: Int = 0,
    var image_url: List<ImageUrl?>? = listOf(),
    val patient_order: PatientOrder = PatientOrder(),
    val patient_order_test_detail_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val patient_work_order_uuid: Int = 0,
    val result_value: String = "",
    val test_master: TestMaster = TestMaster(),
    val test_master_uuid: Int = 0,
    val uuid: Int = 0,
    val work_order_attachment_uuid: Any = Any(),
    val work_order_status: WorkOrderStatus = WorkOrderStatus(),
    val work_order_status_uuid: Int = 0
)