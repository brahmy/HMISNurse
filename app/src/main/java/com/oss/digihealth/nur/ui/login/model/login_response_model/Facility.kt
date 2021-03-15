package com.oss.digihealth.doc.ui.login.model.login_response_model

data class Facility(
    val code: String? = null,
    val created_by: Int? = null,
    val created_date: String? = null,
    val description: String? = null,
    val email: String? = null,
    val facility_level_uuid: Int? = null,
    val facility_type_uuid: Int? = null,
    val fax: String? = null,
    val health_office_uuid: Int? = null,
    val hud_uuid: String? = null,
    val is_active: Boolean? = null,
    val json_array: String? = null,
    val language_uuid: String? = null,
    val mobile: String? = null,
    val modified_by: Int? = null,
    val modified_date: String? = null,
    val nabh_logo: String? = null,
    val name: String? = null,
    val parent_facility_uuid: Int? = null,
    val phone: String? = null,
    val revision: String? = "",
    val speciality_uuid: Int? = null,
    val status: Boolean? = null,
    val uuid: Int? = null
)