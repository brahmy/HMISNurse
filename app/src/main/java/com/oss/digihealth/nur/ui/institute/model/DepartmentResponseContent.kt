package com.oss.digihealth.nur.ui.institute.model

data class DepartmentResponseContent(
    var created_by: Int? = 0,
    var created_date: String? = "",
    var department: Department? = Department(),
    var department_uuid: Int? = 0,
    var facility_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",

    var status: Boolean? = false,
    var user_department: UserDepartment? = UserDepartment(),
    var uuid: Int? = 0
)