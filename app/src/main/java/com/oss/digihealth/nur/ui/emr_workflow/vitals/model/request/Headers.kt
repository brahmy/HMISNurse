package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request

data class Headers(
    var display_order: Int? = 0,
    var is_public: String? = "",
    var name: String? = "",
    var template_id: Int? = 0
)