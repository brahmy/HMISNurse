package com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.model

data class NurseUpdateRequestModule(
    var activity_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var context_uuid: Int? = 0,
    var department_uuid: String? = "",
    var facility_uuid: Int? = 0,
    var nurse_desk_order: Int? = 0,
    var role_uuid: String? = "",
    var ward_master_uuid: Int? = 0
)