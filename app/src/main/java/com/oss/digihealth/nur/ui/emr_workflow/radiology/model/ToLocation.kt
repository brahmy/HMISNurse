package com.oss.digihealth.nur.ui.emr_workflow.radiology.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToLocation(
    var created_by: Int = 0,
    var created_date: String = "",
    var facility_uuid: Int = 0,
    var from_department_uuid: Int = 0,
    var is_active: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var profile_uuid: Int? = null,
    var revision: String? = null,
    var status: Boolean = false,
    var test_master_uuid: Int? = null,
    var to_department_uuid: Int = 0,
    var to_location_uuid: Int = 0,
    var to_subdepartment_uuid: Int = 0,
    var uuid: Int = 0,
    var ward_uuid: Int? = null
) : Parcelable