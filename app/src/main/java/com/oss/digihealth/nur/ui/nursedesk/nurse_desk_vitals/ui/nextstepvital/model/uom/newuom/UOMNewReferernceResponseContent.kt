package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.uom.newuom

data class UOMNewReferernceResponseContent(
    val Is_default: Int? = 0,
    val code: String? = "",
    val color: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val display_order: Int? = 0,
    val is_active: Boolean? = false,
    val language: Any? = Any(),
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val revision: Revision? = Revision(),
    val status: Boolean? = false,
    val uuid: Int? = 0
)