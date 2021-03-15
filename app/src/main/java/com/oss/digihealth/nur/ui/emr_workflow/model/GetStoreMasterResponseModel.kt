package com.oss.digihealth.nur.ui.emr_workflow.model

data class GetStoreMasterResponseModel(
    val responseContents: List<StoreMasterresponseContent?>? = listOf(),
    val message: String? = "",
    val req: Req? = Req(),
    val status: Int? = 0,
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)