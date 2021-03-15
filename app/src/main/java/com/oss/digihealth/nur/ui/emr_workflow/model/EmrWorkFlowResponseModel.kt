package com.oss.digihealth.nur.ui.emr_workflow.model

data class EmrWorkFlowResponseModel(
    var code: Int? = null,
    var message: String? = null,
    var responseContents: ArrayList<ResponseContent?>? = null
)