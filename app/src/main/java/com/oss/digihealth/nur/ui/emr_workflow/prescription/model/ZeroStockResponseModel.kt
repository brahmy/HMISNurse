package com.oss.digihealth.nur.ui.emr_workflow.prescription.model

data class ZeroStockResponseModel(
    val message: String = "",
    val responseContents: List<ZeroStockResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)