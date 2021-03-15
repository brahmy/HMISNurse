package com.oss.digihealth.nur.ui.login.model

data class LoginSessionRequest(
    var ApplicationId: Int? = null,
    var DeviceId: Any? = null,
    var DeviceName: Any? = null,
    var DeviceOS: Any? = null,
    var DeviceVersion: Any? = null,
    var ExpiresIn: Int? = null,
    var IPAddress: String? = null,
    var LoginId: String? = null,
    var Logininfo: String? = null,
    var Password: String? = null,
    var RoleInfo: String? = null,
    var UserName: String? = null,
    var UserTypeId: String? = null
)