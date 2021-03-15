package com.oss.digihealth.nur.ui.emr_workflow.lab.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LabToLocationResponse(
    val message: String? = "",
    val responseContents: List<LabToLocationContent?>? = listOf(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
) : Parcelable {

    @Parcelize
    data class LabToLocationContent(
        val created_by: Int? = 0,
        val created_by_name: String? = "",
        val created_date: String? = "",
        val department_name: String? = "",
        val department_uuid: Int? = 0,
        val description: String? = "",
        val facility_name: String? = "",
        val facility_uuid: Int? = 0,
        val is_active: Boolean? = false,
        val lab_master_type_uuid: Int? = 0,
        val location_code: String? = "",
        var location_name: String? = "",
        val modified_by: Int? = 0,
        val modified_by_name: String? = "",
        val modified_date: String? = "",
        val revision: Int? = 0,
        val status: Boolean? = false,
        val sub_department_name: String? = "",
        val sub_department_uuid: Int? = 0,
        var uuid: Int? = 0
    ) : Parcelable
}