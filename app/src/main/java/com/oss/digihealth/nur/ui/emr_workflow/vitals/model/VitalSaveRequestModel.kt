package com.oss.digihealth.nur.ui.emr_workflow.vitals.model

data class VitalSaveRequestModel(
    var comments: String = "",
    var consultation_uuid: Int = 1,
    var department_uuid: String = "",
    var encounter_type_uuid: Int = 1,
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var loinc_code: String = "abc",
    var mnemonic_code: String = "abc",
    var patient_uuid: String = "",
    var patient_vital_status_uuid: Int = 1,
    var performed_date: String = "",
    var reference_range_from: String = "100",
    var reference_range_to: String = "1000",
    var revision: Int = 1,
    var tat_end_time: String = "",
    var tat_start_time: String = "",
    var vital_group_uuid: Int = 0,
    var vital_master_uuid: Int = 0,
    var vital_qualifier_uuid: Int = 0,
    var vital_type_uuid: Int = 0,
    var vital_uom_uuid: Int = 0,
    var vital_value: String = "",
    var vital_value_type_uuid: Int = 0
)