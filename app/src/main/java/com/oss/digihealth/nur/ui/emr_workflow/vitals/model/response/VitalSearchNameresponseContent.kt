package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response

data class VitalSearchNameresponseContent(
    val created_by: Int? = 0,
    val created_date: String? = "",
    val description: String? = "",
    val emr_uom_uuid: Any? = Any(),
    val is_active: Boolean? = false,
    val is_default: Boolean? = false,
    val loinc_code_master_uuid: Int? = 0,
    val mnemonic: String? = "",
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val reference_range_from: Int? = 0,
    val reference_range_to: Int? = 0,
    val revision: Int? = 0,
    val status: Boolean? = false,
    val uom_master_uuid: Int? = 0,
    val uuid: Int? = 0,
    val value_format: String? = "",
    val vital_type_uuid: Int? = 0,
    val vital_value_type_uuid: Int? = 0
)