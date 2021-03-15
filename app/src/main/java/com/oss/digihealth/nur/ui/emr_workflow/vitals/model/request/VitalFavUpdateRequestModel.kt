package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request

data class VitalFavUpdateRequestModel(
    var existing_details: List<Any?>? = listOf(),
    var headers: Headers? = Headers(),
    var new_details: List<NewDetail?>? = listOf(),
    var removed_details: List<RemovedDetail?>? = listOf()
)