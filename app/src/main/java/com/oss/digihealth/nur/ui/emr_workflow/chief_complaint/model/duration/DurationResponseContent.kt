package com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.model.duration

data class DurationResponseContent(
    val duration_period_code: String? = null,
    val duration_period_id: Int? = null,
    val duration_period_name: String? = null,
    var isSelected: Boolean? = false
)