package com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request

data class Detail(
    var chief_complaint_uuid: Int? = 0,
    var drug_frequency_uuid: Int? = 0,
    var drug_instruction_uuid: Int? = 0,
    var drug_route_uuid: Int? = 0,
    var duration: Int? = 0,
    var duration_period_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_profile: Boolean? = false,
    var item_master_uuid: Int? = 0,
    var department_uuid : Int? =0,
    var revision: Boolean? = false,
    var test_master_uuid: Int? = 0,
    var profile_uuid: Int? = 0,
    var vital_master_uuid: Int? = 0,
    var tat_start_time:String?="",
    var tat_end_time : String?="",
    var diet_master_uuid: Int? = 0,
    var diet_category_uuid: Int? = 0,
    var diet_frequency_uuid: Int? = 0,
    var lab_uuid : Int?=0
)