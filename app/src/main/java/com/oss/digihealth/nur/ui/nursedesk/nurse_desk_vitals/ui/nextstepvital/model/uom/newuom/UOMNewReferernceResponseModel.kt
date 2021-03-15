package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.uom.newuom

data class UOMNewReferernceResponseModel(
    val responseContents: List<UOMNewReferernceResponseContent?>? = listOf(),
    val code: Int? = 0,
    val message: String? = "",
    val totalRecords: Int? = 0
)