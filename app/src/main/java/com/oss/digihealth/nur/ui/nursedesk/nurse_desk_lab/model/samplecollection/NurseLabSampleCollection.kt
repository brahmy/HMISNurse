package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.samplecollection

data class NurseLabSampleCollection(
    var msg: String? = "",
    var req: Req? = Req(),
    var responseContents: ResponseContentsnurselabsamplecollection? = ResponseContentsnurselabsamplecollection(),
    var statusCode: Int? = 0
)