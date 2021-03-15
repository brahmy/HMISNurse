package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model

import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.TemplateDetail

data class MainVitalsListresponseContents(
    val getVitals: List<TemplateDetail?>? = listOf()
)