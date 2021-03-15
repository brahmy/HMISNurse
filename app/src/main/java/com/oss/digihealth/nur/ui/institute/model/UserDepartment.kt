package com.oss.digihealth.nur.ui.institute.model

data class UserDepartment(
    var department: Department? = Department(),
    var department_uuid: Int? = 0,
    var user_uuid: Int? = 0,
    var uuid: Int? = 0
)