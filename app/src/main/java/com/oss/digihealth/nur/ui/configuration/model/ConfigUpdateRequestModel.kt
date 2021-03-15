package com.oss.digihealth.nur.ui.configuration.model

data class ConfigUpdateRequestModel(
    var role_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var context_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var activity_uuid: Int? = 0,
    var work_flow_order: Int? = 0,
    var facility_uuid: Int? = 0,
    var ward_master_uuid:Int?=0,
    var user_uuid : Int?=0,
    var nurse_desk_order : Int?=0,
    var status : Boolean = false,

    var revision : Int? = 1

)