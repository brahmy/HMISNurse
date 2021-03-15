package com.oss.digihealth.nur.ui.configuration.model

data class ConfigResponseModel(
    var responseContents: ArrayList<ConfigResponseContent?> = ArrayList(),
    var statusCode: Int? = 0
)