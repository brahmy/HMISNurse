package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response

data class VitalSaveResponseModel(
    var code: Int = 0,
    var message: String = "",
    var responseContents: List<VitalResponse> = listOf()
)