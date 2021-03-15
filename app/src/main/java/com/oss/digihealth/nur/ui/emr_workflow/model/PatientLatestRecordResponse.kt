package com.oss.digihealth.nur.ui.emr_workflow.model

data class PatientLatestRecordResponse(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: ResponseContents = ResponseContents()
)




data class ResponseContents(
    val createdDate: String? = null,
    val departmentId: Int? = null,
    val departmentName: String = "",
    val doctorFirstName: String = "",
    val doctorLastName: String? = null,
    val doctorMiddleName: String? = null,
    val encounterTypeId: Int? = null,
    val patientId: Int? = null,
    val titleId: Int? = null,
    val titleName: String? = null
)
