package com.oss.digihealth.nur.ui.emr_workflow.model.create_encounter_response

import com.oss.digihealth.nur.ui.emr_workflow.model.fetch_encounters_response.EncounterDoctor

data class CreateEncounterResponseContents(
    val encounter: Encounter? = null,
    val encounterDoctor: EncounterDoctor? = null
)