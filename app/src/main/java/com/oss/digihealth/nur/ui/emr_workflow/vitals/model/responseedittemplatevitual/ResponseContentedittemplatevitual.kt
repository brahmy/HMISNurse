package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual

import android.os.Parcel
import android.os.Parcelable


data class ResponseContentedittemplatevitual(
    val active_from: String? = "",
    val active_to: String? = "",
    val code: Any? = Any(),
    val comments: Any? = Any(),
    val created_by: Int? = 0,
    val created_date: String? = "",
    val department_name: String? = "",
    val department_uuid: Int? = 0,
    val description: String? = "",
    val diagnosis_uuid: Int? = 0,
    val display_order: Int? = 0,
    val facility_name: String? = "",
    val facility_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_public: Boolean? = false,
    val lab_uuid: Int? = 0,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val template_master_details: List<TemplateMasterDetail?>? = listOf(),
    val template_type_uuid: Int? = 0,
    val user_uuid: Int? = 0,
    val uuid: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        TODO("code"),
        TODO("comments"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        TODO("template_master_details"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseContentedittemplatevitual> {
        override fun createFromParcel(parcel: Parcel): ResponseContentedittemplatevitual {
            return ResponseContentedittemplatevitual(parcel)
        }

        override fun newArray(size: Int): Array<ResponseContentedittemplatevitual?> {
            return arrayOfNulls(size)
        }
    }
}