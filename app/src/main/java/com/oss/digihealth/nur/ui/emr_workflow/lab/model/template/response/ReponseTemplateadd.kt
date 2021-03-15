package com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.response

data class ReponseTemplateadd(
    var code: Int? = 0,
    var message: String? = "",
    var responseContent: ResponseContentTemplateResponse? = ResponseContentTemplateResponse()
)