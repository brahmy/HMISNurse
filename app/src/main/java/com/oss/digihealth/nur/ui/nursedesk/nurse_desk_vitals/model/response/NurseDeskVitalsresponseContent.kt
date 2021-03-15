package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.model.response

import com.oss.digihealth.nur.ui.nurse_desk.bedmangement.model.Model.request.WardTrasanferStatus

data class NurseDeskVitalsresponseContent(
    val admission_admission_status_name: String? = "",
    val admission_bed_uuid: Int? = 0,
    val admission_date: String? = "",
    val admission_department_uuid: Int? = 0,
    val admission_discharge_date: Any? = Any(),
    val admission_encounter_type_uuid: Int? = 0,
    val admission_encounter_uuid: Int? = 0,
    val admission_is_active: Boolean? = false,
    val admission_is_casualty: Boolean? = false,
    val admission_reason: String? = "",
    val admission_room_uuid: Int? = 0,
    val admission_status: Boolean? = false,
    val admission_status_uuid: Int? = 0,
    val admission_uuid: Int? = 0,
    val admission_ward_uuid: Int? = 0,
    val gender_uuid: Int? = 0,
    val patient_age: Int? = 0,
    val patient_first_name: String? = "",
    val patient_gender_code: String? = "",
    val patient_gender_name: String? = "",
    val patient_info: String = "",
    val patient_last_name: String? = "",
    val patient_middle_name: String? = "",
    val patient_mobile: String? = "",
    var patient_title_name: String? = "",
    val patient_uhid: String? = "",
    val patients_uuid: Int? = 0,
    val ward_master_name: String? = "",
    val ward_transfer_status: WardTrasanferStatus? =null,
    val wbm_bed_number: Int? = 0
)