package com.oss.digihealth.nur.ui.emr_workflow.vitals.model

data class TemplateDetail(
    val active_from: String = "",
    val active_to: String = "",
    val code: Any = Any(),
    val comments: Any = Any(),
    val created_by: Int = 0,
    val created_date: String = "",
    val department_uuid: Int = 0,
    val description: String = "",
    val diagnosis_uuid: Int = 0,
    val display_order: Int = 0,
    val facility_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_public: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    var name: String = "",
    val revision: Int = 0,
    val status: Boolean = false,
    val template_master_details: List<TemplateMasterDetail> = listOf(),
    val template_type_uuid: Int = 0,
    val user_uuid: Int = 0,
    var uuid: Int = 0,
    var vw_template_master_details:List<vwtemplatemasterdetails> = listOf(),
    var isSelected: Boolean? = false,
    var position: Int? = 0,
    var itemAppendString: String? = "",
    var itemId: Int? = 0,

    //getAllVitals
    var is_default: Boolean = false,
    var loinc_code_master_uuid: Int = 0,
    var mnemonic: Boolean = false,
    var reference_range_from: Long = 0,
    var reference_range_to: Long = 0,
    var uom_master_uuid: Int = 0,
    var vital_type_uuid: Int = 0,
    var vital_value: String = "",
    var vital_name: String = "",
    var vital_value_type_uuid: Int = 0,
    var vital_value_type:VitalValueType=VitalValueType(),
    var vitals_value:String="",
    var collapse : Boolean = true


)