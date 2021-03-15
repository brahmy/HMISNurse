package com.oss.digihealth.nur.ui.configuration.model

data class ConfigResponseContent(
    var activity: ConfigActivityModel? = ConfigActivityModel(),
    var activity_uuid: Int? = 0,
    var context_uuid: Int? = 0,
    var code: Int? = 0,
    var created_by: String? = "",
    var created_date: String? = "",
    var display_order: Int? = 0,
    var is_active: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var revision: Int? = 0,
    var status: Boolean? = false,
    var order:Int?=0,
    var uuid: Int? = 0

)