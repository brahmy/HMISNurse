package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model

data class PatientOrderTestDetail(
    val is_nurse_collected: Boolean = false,
    val order_priority: OrderPriority = OrderPriority(),
    val order_priority_uuid: Int = 0,
    val order_status: OrderStatus = OrderStatus(),
    val order_status_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val sample_collection_date: Any = Any(),
    val test_master: TestMaster = TestMaster(),
    val test_master_uuid: Int = 0,
    val uuid: Int = 0
)