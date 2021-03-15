package com.oss.digihealth.nur.ui.emr_workflow.model.fetch_encounters_response

data class FectchEncounterResponseModel(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: List<FetchEncounterResponseContent?>? = null
)