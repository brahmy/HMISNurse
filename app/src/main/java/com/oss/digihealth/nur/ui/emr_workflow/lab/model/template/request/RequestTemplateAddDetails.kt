package com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request

data class RequestTemplateAddDetails(
    var details: List<Detail?>? = listOf(),
    var headers: Headers? = Headers()
)