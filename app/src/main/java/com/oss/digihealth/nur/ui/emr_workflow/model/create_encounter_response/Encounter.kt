package com.oss.digihealth.nur.ui.emr_workflow.model.create_encounter_response

data class Encounter(
    val admission_request_uuid: Int? = null,
    val admission_uuid: Int? = null,
    val appointment_uuid: Int? = null,
    val created_by: String? = null,
    val created_date: String? = null,
    val department_uuid: String? = null,
    val discharge_type_uuid: Int? = null,
    val encounter_date: String? = null,
    val encounter_identifier: Int? = null,
    val encounter_priority_uuid: Int? = null,
    val encounter_status_uuid: Int? = null,
    val encounter_type_uuid: Int? = null,
    val facility_uuid: String? = null,
    val is_active: Int? = null,
    val is_active_encounter: Int? = null,
    val modified_by: String? = null,
    val modified_date: String? = null,
    val patient_uuid: String? = null,
    val revision: Int? = 0,
    val status: Int? = null,
    val user_uuid: String? = null,
    val uuid: Int? = null
)