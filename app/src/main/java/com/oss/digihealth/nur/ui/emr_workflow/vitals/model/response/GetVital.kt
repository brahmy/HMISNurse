package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response

data class GetVital(
    var created_by: Int = 0,
    var created_date: String = "",
    var description: String = "",
    var is_active: Boolean = false,
    var is_default: Boolean = false,
    var loinc_code_master_uuid: Int = 0,
    var mnemonic: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var reference_range_from: Long = 0,
    var reference_range_to: Long = 0,
    var revision: Any = Any(),
    var status: Boolean = false,
    var uom_master_uuid: Int = 0,
    var uuid: Int = 0,
    var vital_type_uuid: Int = 0,
    var vital_value_type_uuid: Int = 0
)