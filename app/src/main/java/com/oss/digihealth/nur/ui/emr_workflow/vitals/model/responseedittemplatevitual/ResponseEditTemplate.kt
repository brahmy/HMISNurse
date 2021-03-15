package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual


data class ResponseEditTemplate(
    var req: String? = "",
    var responseContent:List<ResponseContentedittemplatevitual?>? = listOf(),
    var statusCode: Int? = 0
)