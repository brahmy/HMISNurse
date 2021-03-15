package com.oss.digihealth.nur.ui.institute.model.Phermisiun

data class Store(
    var created_by: Int = 0,
    var created_date: String = "",
    var department_name: String = "",
    var department_uuid: Int = 0,
    var facility_uuid: Int = 0,
    var is_active: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var revision: Int = 0,
    var status: Boolean = false,
    var store_code: String = "",
    var store_master: StoreMaster = StoreMaster(),
    var store_master_uuid: Int = 0,
    var store_name: String = "",
    var store_user_uuid: Int = 0,
    var user_type_uuid: Int = 0,
    var user_uuid: Int = 0,
    var uuid: Int = 0
)