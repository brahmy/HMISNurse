package com.oss.digihealth.nur.ui.emr_workflow.model.fetch_encounters_response

data class FetchEncounterResponseContent(
    val admission_request_uuid: Int? = null,
    val admission_uuid: Int? = null,
    val appointment_uuid: Int? = null,
    val created_by: Int? = null,
    val created_date: String? = null,
    val discharge_date: Any? = null,
    val discharge_type_uuid: Int? = null,
    val encounter_date: String? = null,
    val encounter_doctors: List<EncounterDoctor?>? = null,
    val encounter_identifier: Int? = null,
    val encounter_priority_uuid: Int? = null,
    val encounter_status_uuid: Int? = null,
    val encounter_type_uuid: Int? = null,
    val facility_uuid: Int? = null,
    val is_active: Boolean? = null,
    val is_active_encounter: Boolean? = null,
    val is_followup: Boolean? = null,
    val is_group_casuality: Boolean? = null,
    val is_mrd_request: Boolean? = null,
    val modified_by: Int? = null,
    val modified_date: String? = null,
    val patient_uuid: Int? = null,
    val revision: Boolean? = null,
    val status: Boolean? = null,
    val uuid: Int? = null
)