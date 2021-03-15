package com.oss.digihealth.nur.ui.emr_workflow.model.templete

import com.oss.digihealth.nur.ui.emr_workflow.diet.model.DietTemplateDeatils

data class TemplatesLab(
    var lab_details: List<LabDetail?>? = listOf(),
    var radiology_details: List<LabDetail?>? = listOf(),
    var diet_details: List<DietTemplateDeatils?>? = listOf(),
    var temp_details: TempDetails? = TempDetails(),
    var collapse:Boolean?=true
)