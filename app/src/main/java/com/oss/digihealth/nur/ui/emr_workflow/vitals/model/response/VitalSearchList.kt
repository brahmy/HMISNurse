package com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response

data class VitalSearchList(
    var getVitals: ArrayList<GetVital> = ArrayList()
)