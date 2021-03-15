package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual


data class TemplateMasterDetail(
    val chief_complaint_uuid: Int? = 0,
    val comments: Any? = Any(),
    val created_by: Int? = 0,
    val created_date: String? = "",
    val diet_category_uuid: Int? = 0,
    val diet_frequency_uuid: Int? = 0,
    val diet_master_uuid: Int? = 0,
    val display_order: Int? = 0,
    val drug_frequency_uuid: Int? = 0,
    val drug_instruction_uuid: Int? = 0,
    val drug_route_uuid: Int? = 0,
    val duration: Int? = 0,
    val duration_period_uuid: Int? = 0,
    val injection_room_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val item_master_uuid: Int? = 0,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val profile_master_uuid: Int? = 0,
    val quantity: Int? = 0,
    val revision: Int? = 0,
    val status: Boolean? = false,
    val strength: Any? = Any(),
    val template_master_uuid: Int? = 0,
    val test_master_type_uuid: Int? = 0,
    val test_master_uuid: Int? = 0,
    val uuid: Int? = 0,
    val vital_master: VitalMaster? = VitalMaster(),
    val vital_master_uuid: Int? = 0
)