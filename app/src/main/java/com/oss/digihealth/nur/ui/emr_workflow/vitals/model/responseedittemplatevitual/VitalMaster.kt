package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual

data class VitalMaster(
    val created_by: Int? = 0,
    val created_date: String? = "",
    val description: String? = "",
    val emr_uom_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_default: Boolean? = false,
    val loinc_code_master_uuid: Int? = 0,
    val mnemonic: String? = "",
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val reference_range_from: Any? = Any(),
    val reference_range_to: Any? = Any(),
    val revision: Int? = 0,
    val status: Boolean? = false,
    val uom_master_uuid: Int? = 0,
    val uuid: Int? = 0,
    val value_format: Any? = Any(),
    val vital_type_uuid: Int? = 0,
    val vital_value_type_uuid: Int? = 0
)