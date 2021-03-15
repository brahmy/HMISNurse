package com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.gettemplate

import android.os.Parcel
import android.os.Parcelable
import com.oss.digihealth.nur.ui.emr_workflow.diet.model.TemplateDietDetail

data class ResponseContentLabGetDetails(
    var lab_details: List<LabDetail?>? = listOf(),
    var radiology_details: List<LabDetail?>? = listOf(),
    var diet_details: List<TemplateDietDetail> = listOf(),
    var temp_details: TempDetails? = TempDetails()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("lab_details"),
        TODO("diet_details"),
        TODO("temp_details")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseContentLabGetDetails> {
        override fun createFromParcel(parcel: Parcel): ResponseContentLabGetDetails {
            return ResponseContentLabGetDetails(parcel)
        }

        override fun newArray(size: Int): Array<ResponseContentLabGetDetails?> {
            return arrayOfNulls(size)
        }
    }
}
