package com.oss.digihealth.nur.ui.emr_workflow.prescription.model

import com.oss.digihealth.nur.ui.institute.model.Phermisiun.StoreMaster

data class ZeroStockResponseContent(
    val base_uom_uuid: Int = 0,
    val buffer_percentage: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val facility_uuid: Int = 0,
    val is_active: Boolean = false,
    val item_master: ItemMaster = ItemMaster(),
    val item_master_uuid: Int = 0,
    val lead_time: Int = 0,
    val max_quantity: Int = 0,
    val min_quantity: Int = 0,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val purchase_uom_uuid: Int = 0,
    val quantity: Int = 0,
    val re_order_quantity: Int = 0,
    val revision: Int = 0,
    val safety_quantity: Int = 0,
    val sale_uom_uuid: Int = 0,
    val shelf: Any = Any(),
    val status: Boolean = false,
    val store_master: StoreMaster = StoreMaster(),
    val store_master_uuid: Int = 0,
    val store_rack_uuid: Int = 0,
    val tray: Any = Any(),
    val uuid: Int = 0
)