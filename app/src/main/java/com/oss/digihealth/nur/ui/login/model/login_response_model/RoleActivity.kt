package com.oss.digihealth.doc.ui.login.model.login_response_model

data class RoleActivity(
    var activity: Activity = Activity(),
    var activity_uuid: Int? = 0,
    var module_uuid: Int? = 0,
    var role_uuid: Int? = 0,
    var uuid: Int? = 0
)