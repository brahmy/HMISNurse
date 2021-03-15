package com.oss.digihealth.nur.ui.emr_workflow.lab.model

data class FavAddResponseModel(
    val responseContent: FavAddResponseContent = FavAddResponseContent(),
    val statusCode: Int = 0
)