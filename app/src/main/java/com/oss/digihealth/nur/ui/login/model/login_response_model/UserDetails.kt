package com.oss.digihealth.doc.ui.login.model.login_response_model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class UserDetails(
    val SessionId: String? = null,
    val aadhaar_number: String? = null,
    val access_token: String? = null,
    val address_line_1: String? = null,
    val address_line_2: String? = null,
    val age: Int? = null,
    val application_login_permission: Boolean? = null,
    val created_by: Int? = null,
    val created_date: String? = null,
    val department_uuid: Int? = null,
    val designation_uuid: Int? = null,
    val district_uuid: Int? = null,
    val dob: String? = null,
    val email: String? = null,
    val employee_code: String? = null,
    val expires_in: Long? = null,
//    @Ignore val facility: Facility? = null,
    val facility_uuid: Int? = null,
    val first_name: String? = null,
    val first_time_login: Boolean? = null,
    val gender_uuid: Int? = null,
    val gpf_cps_no: String? = null,
    val gpf_suffix_uuid: Int? = null,
    val is_active: Boolean? = null,
    val is_anaesthetist: Boolean? = null,
    val last_login_datetime: String? = null,
    val last_name: String? = null,
    val login_device_count: String? = null,
    val middle_name: String? = null,
    val mobile1: String? = null,
    val mobile2: String? = null,
    val modified_by: Int? = null,
    val modified_date: String? = null,
    val nationality: String? = null,
//    @Ignore val office_level: OfficeLevel? = null,
    val office_user: Boolean? = null,
    val office_uuid: Int? = null,
    val password: String? = null,
    val preferred_language_uuid: Int? = null,
    val qualification: String? = null,
    val registration_number: String? = null,
    val reporting_to_uuid: Int? = null,
    val revision: Int? = 0,
    @Embedded(prefix = "role")
    val role: Role? = null,
    val role_uuid: Int? = null,
    val salutation_uuid: String? = null,
    val state_uuid: Int? = null,
    val taluk_uuid: Int? = null,
    val tokenurl: String? = null,
    val unit_uuid: Int? = null,
    val user_name: String? = null,
    @Embedded(prefix = "title")
    val title: Title? = null,
    @Embedded(prefix = "usertype")
    val user_type: UserType? = null,
    val user_type_uuid: Int? = null,
    @PrimaryKey
    val uuid: Int? = null
)
