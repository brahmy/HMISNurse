package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model

data class MainVItalsListResponseModel(
    val responseContents: MainVitalsListresponseContents? = MainVitalsListresponseContents(),
    val message: String? = "",
    val statusCode: Int? = 0
)