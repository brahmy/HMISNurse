package com.oss.digihealth.nur.ui.emr_workflow.lab.model

data class FavAddAllDepatResponseModel(
    val req: String = "",
    val responseContents: List<FavAddAllDepatResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)