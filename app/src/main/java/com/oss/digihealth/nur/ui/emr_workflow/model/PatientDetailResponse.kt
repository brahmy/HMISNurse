package com.oss.digihealth.nur.ui.emr_workflow.model

data class PatientDetailResponse(
    val message: String? = null,
    val responseContent: PatientData? = null,
    val statusCode: Int? = null
)
data class PatientData(
    val age: Int? = null,
    val application_identifier: Any? = null,
    val application_type_uuid: Int? = null,
    val application_uuid: Any? = null,
    val block_details: BlockDetails? = null,
    val blood_group_details: BloodGroupDetails? = null,
    val blood_group_uuid: Int? = null,
    val category_details: CategoryDetails? = null,
    val country_details: CountryDetails? = null,
    val covid_patient_details: Any? = null,
    val created_by: Int? = null,
    val created_date: String? = null,
    val district_details: DistrictDetails? = null,
    val dob: String? = null,
    val facility_details: FacilityDetails? = null,
    val first_name: String? = null,
    val gender_details: GenderDetails? = null,
    val gender_uuid: Int? = null,
    val id_proof_details: IdProofDetails? = null,
    val income_details: IncomeDetails? = null,
    val is_active: Boolean? = null,
    val is_adult: Boolean? = null,
    val is_dob_auto_calculate: Boolean? = null,
    val last_name: String? = null,
    val marital_details: MaritalDetails? = null,
    val maternity_details: Any? = null,
    val middle_name: String? = null,
    val modified_by: Int? = null,
    val modified_date: String? = null,
    val occupation_details: OccupationDetails? = null,
    val old_pin: Any? = null,
    val package_uuid: Int? = null,
    val para_1: Any? = null,
    val patient_condition_details: List<Any?>? = null,
    val patient_detail: PatientDetail? = null,
    val patient_specimen_details: List<Any?>? = null,
    val patient_symptoms: List<Any?>? = null,
    val patient_type_detail: PatientTypeDetail? = null,
    val patient_type_uuid: Int? = null,
    val patient_visits: List<PatientVisit?>? = null,
    val period_detail: PeriodDetail? = null,
    val period_uuid: Int? = null,
    val professional_title_uuid: Int? = null,
    val registered_date: String? = null,
    val registred_facility_uuid: Int? = null,
    val relation_details: RelationDetails? = null,
    val revision: Boolean? = null,
    val salutation_details: SalutationDetails? = null,
    val state_details: StateDetails? = null,
    val suffix_details: SuffixDetails? = null,
    val suffix_uuid: Int? = null,
    val taluk_details: TalukDetails? = null,
    val tat_end_time: Any? = null,
    val tat_start_time: Any? = null,
    val title_uuid: Int? = null,
    val uhid: String? = null,
    val uuid: Int? = null,
    val village_details: VillageDetails? = null
)
class BlockDetails(
)

class BloodGroupDetails(
)

class CategoryDetails(
)

data class CountryDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)

data class DistrictDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)

data class FacilityDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)

data class GenderDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)

class IdProofDetails(
)

class IncomeDetails(
)

class MaritalDetails(
)

class OccupationDetails(
)

data class PatientDetail(
    val aadhaar_number: String? = null,
    val address_line1: String? = null,
    val address_line2: String? = null,
    val address_line3: String? = null,
    val address_line4: String? = null,
    val address_line5: String? = null,
    val airport_name: Any? = null,
    val alternate_number: String? = null,
    val attender_contact_number: String? = null,
    val attender_name: String? = null,
    val bill_class: Any? = null,
    val block_uuid: Int? = null,
    val city_uuid: Int? = null,
    val community_detail: Any? = null,
    val community_uuid: Int? = null,
    val complication_detail: Any? = null,
    val complication_uuid: Int? = null,
    val contact_history: Boolean? = null,
    val contact_history_details: Any? = null,
    val country_uuid: Int? = null,
    val created_by: Int? = null,
    val created_date: String? = null,
    val date_of_onset: Any? = null,
    val death_approved_by: Int? = null,
    val death_coments: Any? = null,
    val death_confirmed_by: Int? = null,
    val death_place: String? = null,
    val death_updated_by: Int? = null,
    val death_updated_date: Any? = null,
    val district_uuid: Int? = null,
    val email: String? = null,
    val height: String? = null,
    val ili: Boolean? = null,
    val income_uuid: Int? = null,
    val is_active: Boolean? = null,
    val is_death_confirmed: Any? = null,
    val is_email_communication_preference: Any? = null,
    val is_rapid_test: Boolean? = null,
    val is_sms_communication_preference: Any? = null,
    val lab_to_facility_uuid: Int? = null,
    val location_travelled: Any? = null,
    val marital_uuid: Int? = null,
    val mobile: String? = null,
    val modified_by: Int? = null,
    val modified_date: String? = null,
    val nationality_type_detail: Any? = null,
    val nationality_type_uuid: Int? = null,
    val no_symptom: Boolean? = null,
    val occupation_uuid: Int? = null,
    val op_status: Any? = null,
    val other_proof_number: String? = null,
    val other_proof_uuid: Int? = null,
    val out_come_date: Any? = null,
    val out_come_type_detail: Any? = null,
    val out_come_type_uuid: Int? = null,
    val para_1: Any? = null,
    val patient_uuid: Int? = null,
    val photo_path: String? = null,
    val pin_status: Any? = null,
    val pincode: String? = null,
    val place_uuid: Int? = null,
    val quarantine_status_date: Any? = null,
    val quarentine_status: Any? = null,
    val quarentine_status_type_detail: Any? = null,
    val quarentine_status_type_uuid: Int? = null,
    val referred_doctor: Any? = null,
    val relation_type_uuid: Int? = null,
    val religion_detail: Any? = null,
    val religion_uuid: Int? = null,
    val repeat_test: Boolean? = null,
    val repeat_test_date: Any? = null,
    val repeat_test_type_uuid: Int? = null,
    val revision: Boolean? = null,
    val sample_type_uuid: Int? = null,
    val sari: Boolean? = null,
    val smart_ration_number: String? = null,
    val state_uuid: Int? = null,
    val symptom_duration: Any? = null,
    val symptoms: Any? = null,
    val taluk_uuid: Int? = null,
    val test_location: Any? = null,
    val test_result: Boolean? = null,
    val travel_history: Any? = null,
    val travel_history_date: Any? = null,
    val travel_history_to_date: Any? = null,
    val treatment_category_uuid: Int? = null,
    val treatment_plan_detail: Any? = null,
    val treatment_plan_uuid: Int? = null,
    val underline_medicine_condition_uuid: Int? = null,
    val underline_medicine_details: Any? = null,
    val uuid: Int? = null,
    val village_uuid: Int? = null,
    val weight: String? = null
)

data class PatientTypeDetail(
    val code: String? = null,
    val is_active: Boolean? = null,
    val name: String? = null,
    val uuid: Int? = null
)

data class PatientVisit(
    val created_by: Int? = null,
    val created_date: String? = null,
    val department_details: DepartmentDetails? = null,
    val department_uuid: Int? = null,
    val facility_uuid: Int? = null,
    val is_active: Boolean? = null,
    val is_last_visit: Boolean? = null,
    val is_mlc: Boolean? = null,
    val modified_by: Int? = null,
    val modified_date: String? = null,
    val patient_type_uuid: Int? = null,
    val patient_uuid: Int? = null,
    val registered_date: String? = null,
    val revision: Boolean? = null,
    val session_uuid: Int? = null,
    val speciality_department_uuid: Any? = null,
    val unit_uuid: Any? = null,
    val uuid: Int? = null,
    val visit_number: String? = null,
    val visit_type_uuid: Int? = null
)             data class DepartmentDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)


data class PeriodDetail(
    val code: String? = null,
    val is_active: Boolean? = null,
    val name: String? = null,
    val uuid: Int? = null
)

data class RelationDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)

data class SalutationDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)

data class StateDetails(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)

class SuffixDetails(
)

class TalukDetails(
)

class VillageDetails(
)