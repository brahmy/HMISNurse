package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request

data class RequestTemplateAddDetails(
    var details: List<Detail?>? = listOf(),
    var headers: Headers? = Headers()
)