package com.oss.digihealth.nur.ui.emr_workflow.vitals.model

data class VitalTemplateMaster(
    val created_by: Int = 0,
    val created_date: String = "",
    val description: String = "",
    val is_active: Boolean = false,
    val is_default: Boolean = false,
    val loinc_code_master_uuid: Int = 0,
    val mnemonic: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "",
    val reference_range_from: String = "",
    val reference_range_to: String = "",
    val revision: Any = Any(),
    val status: Boolean = false,
    var uom_master_uuid: Int = 0,
    val uuid: Int = 0,
    val vital_type_uuid: Int = 0,
    val vital_value_type_uuid: Int = 0,
    var vitals_value:String=""

)


