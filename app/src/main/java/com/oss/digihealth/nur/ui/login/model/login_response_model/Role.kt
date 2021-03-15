package com.oss.digihealth.doc.ui.login.model.login_response_model

data class Role(
    val active_from: String? = null,
    val active_to: String? = null,
    val activity_uuid: Int? = null,
    val created_by: Int? = null,
    val is_active: Boolean? = null,
    val modified_by: Int? = null,
    val revision: Int? = 0,
    val role_code: String? = null,
    val role_description: String? = null,
    val role_name: String? = null,
    val status: Boolean? = null,
    val uuid: Int? = null
)