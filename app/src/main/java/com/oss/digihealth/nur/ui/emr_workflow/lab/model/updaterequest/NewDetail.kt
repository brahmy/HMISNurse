package com.oss.digihealth.nur.ui.emr_workflow.lab.model.updaterequest

data class NewDetail(
    var chief_complaint_uuid: Int? = 0,
    var display_order: Int? = 0,
    var drug_duration: Int? = 0,
    var drug_frequency_uuid: Int? = 0,
    var drug_id: Int? = 0,
    var drug_instruction_uuid: Int? = 0,
    var drug_period_uuid: Int? = 0,
    var drug_route_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var quantity: Int? = 0,
    var revision: Boolean? = false,
    var template_master_uuid: Int? = 0,
    var test_master_uuid: Int? = 0,
    var vital_master_uuid: Int? = 0,
    var diet_category_code: String ="",
    var diet_category_id: Int= 0,
    var diet_category_name: String="",
    var diet_frequency_code: String="",
    var diet_frequency_id: Int=0,
    var diet_frequency_name: String ="",
    var diet_master_code: String ="",
    var diet_master_id: Int =0,
    var diet_master_name: String="",
    var diet_quantity: Int=0
)