package com.oss.digihealth.nur.ui.emr_workflow.prescription.model

data class ItemMaster(
    var code: String? = "",
    var name: String? = "",
    var strength: String? = "",
    var uuid: Int? = 0,
    var stock_item: StockDetail? =null

)
data class StockDetail(
    var uuid: Int?,
    var quantity: String?,
    var stock_serial_items: List<StockSerialItemPrescription>?
)

data class StockSerialItemPrescription(
    var quantity: String?,
    var expiry_date: String?,
    var batch_id: String?
)