package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model

data class PatientOrderTestDetail(
    var is_nurse_collected: Boolean? = false,
    var order_priority: OrderPriority? = OrderPriority(),
    var order_priority_uuid: Int? = 0,
    var order_status: OrderStatus? = OrderStatus(),
    var order_status_uuid: Int? = 0,
    var patient_order_uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var sample_collection_date: Any? = Any(),
    var test_master: TestMaster? = TestMaster(),
    var test_master_uuid: Int? = 0,
    var uuid: Int? = 0,
    var colorstatus : String?=""
)