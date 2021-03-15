package com.oss.digihealth.nur.ui.quick_reg.model

data class LocationMasterResponseModel(
    var responseContents: List<LocationMaster> = listOf(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)