package com.oss.digihealth.nur.ui.emr_workflow.radiology.model

class RecyclerDto {
    var title: String? = null
    var genre: String? = null
    var year: String? = null
    var image: Int? = null


    constructor() {}
    constructor(title: String?, genre: String?, year: String?, image: Int) {
        this.title = title
        this.genre = genre
        this.year = year
        this.image = image


    }
}