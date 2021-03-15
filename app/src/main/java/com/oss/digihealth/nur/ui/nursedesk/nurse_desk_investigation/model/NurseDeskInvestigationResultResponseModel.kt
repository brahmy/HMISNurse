package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model

data class NurseDeskInvestigationResultResponseModel(
    var responseContents: List<NurseDeskInvestigationResultResponseContent?>? = listOf(),
    var message: String? = "",
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)