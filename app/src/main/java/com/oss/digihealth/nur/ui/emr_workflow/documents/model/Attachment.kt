package com.oss.digihealth.nur.ui.emr_workflow.documents.model

data class Attachment(
    val attached_date: String = "",
    val attachment_name: String = "",
    val attachment_type: AttachmentType = AttachmentType(),
    val attachment_type_uuid: Int = 0,
    val comments: String = "",
    val consultation_uuid: Int = 0,
    val created_by: Int = 0,
    val created_date: String = "",
    val encounter: Encounter = Encounter(),
    val encounter_uuid: Int = 0,
    val file_path: String = "",
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val patient_uuid: Int = 0,
    val revision: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)