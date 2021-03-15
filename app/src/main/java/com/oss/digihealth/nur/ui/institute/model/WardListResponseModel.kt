package com.oss.digihealth.nur.ui.institute.model

import android.os.Parcelable
import com.oss.digihealth.nur.ui.institute.model.wardlist.Ward
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WardListResponseModel(
    val responseContents: List<Ward>? = listOf(),
    val msg: String? = null,
    val status: String? = null,
    val statusCode: Int? = null
) : Parcelable