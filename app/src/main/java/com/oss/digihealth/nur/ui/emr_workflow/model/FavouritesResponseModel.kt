package com.oss.digihealth.nur.ui.emr_workflow.model

import com.oss.digihealth.nur.ui.emr_workflow.model.favorite.FavouritesModel

data class FavouritesResponseModel(
    var code: Int? = null,
    var message: String? = null,
    var responseContentLength: Int? = null,
    var responseContents: ArrayList<FavouritesModel?>? = ArrayList()
)