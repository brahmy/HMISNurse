package com.oss.digihealth.nur.ui.emr_workflow.lab.model

data class FavAddTestNameResponse(
    val message: String = "",
    val statusCode: Int = 0,
    val responseContents: List<FavAddTestNameResponseContent> = listOf(),
    val totalRecords: Int = 0
)