package com.oss.digihealth.nur.ui.configuration.model

data class ConfigActivityModel(
    var code: String? = "",
    var created_by: Int? = 0,
    var created_date: String? = "",
    var display_order: Int? = 0,
    var icon: String? = "",
    var is_active: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var module_uuid: Int? = 0,
    var name: String? = "",
    var revision: Int? = 0,
    var route_url: String? = "",
    var status: Boolean? = false,
    var uuid: Int? = 0
)