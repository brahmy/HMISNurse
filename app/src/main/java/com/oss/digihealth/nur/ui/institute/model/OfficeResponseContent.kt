package com.oss.digihealth.nur.ui.institute.model

data class OfficeResponseContent(
    var approval_user_uuid: Any? = Any(),
    var code: String? = "",
    var created_by: Int? = 0,
    var created_date: String? = "",
    var description: Any? = Any(),
    var email: String? = "",
    var facility_level_uuid: Int? = 0,
    var facility_type_uuid: Int? = 0,
    var fax: String? = "",
    var health_office: HealthOffice? = HealthOffice(),
    var health_office_uuid: Int? = 0,
    var hud_uuid: Int? = 0,
    var image_url: Any? = Any(),
    var is_active: Boolean? = false,
    var is_facility_model: Boolean? = false,
    var is_lab_center: Boolean? = false,
    var json_array: Any? = Any(),
    var language_uuid: Any? = Any(),
    var mobile: String? = "",
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var nabh_logo: Any? = Any(),
    var name: String? = "",
    var parent_facility_uuid: Int? = 0,
    var phone: String? = "",

    var speciality_uuid: Any? = Any(),
    var status: Boolean? = false,
    var user_facilities: List<UserFacility?>? = listOf(),
    var uuid: Int? = 0
)