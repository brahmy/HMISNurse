package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.postsample

data class NursedeskPostsampleResponse(
    var msg: String? = "",
    var req: Req? = Req(),
    var responseContents: ResponseContentsnursepostsample? = ResponseContentsnursepostsample(),
    var statusCode: Int? = 0
)