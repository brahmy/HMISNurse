package com.oss.digihealth.nur.ui.emr_workflow.model.favorite

import android.os.Parcel
import android.os.Parcelable


data class FavouritesModel(
    var chief_complaint_code: String? = "",
    var chief_complaint_id: Int? = 0,
    var chief_complaint_name: String? = "",
    var diagnosis_code: String? = "",
    var diagnosis_description: String? = "",
    var diagnosis_id: String? = "",
    var diagnosis_name: String? = "",
    var drug_active: Boolean? = false,
    var drug_duration: Int? = 0,
    var drug_code: String? = "",
    var drug_quantity: String? = "0",
    var Presstrength: String? = "",
    var drug_frequency_id: String? = "",
    var drug_frequency_name: String? = "",

    var drug_instruction_code: String? = null,
    var drug_instruction_id: Int? = 0,
    var drug_instruction_name: String? = "",

    var drug_id: Int? = 0,
    var store_master_uuid: Int? = 0,
    var drug_name: String? = null,
    var drug_period_code: String? = "",
    var PrescriptiondurationPeriodId: Int = 1,
    var drug_period_id: Int? = null,
    var drug_period_name: String? = "",
    var drug_route_id: Int? = 0,
    var drug_route_name: String? = "",
    var favourite_details_id: Int? = null,
    var favourite_display_order: Int? = null,
    var favourite_id: Int? = null,
    var speciality_sketch_id: Int? = null,
    var favourite_name: String? = null,
    var test_master_code: String? = null,
    var test_master_description: String? = null,
    var test_master_id: Int? = 0,
    var test_master_name: String? = "",
    var TestMethodId: Int = 0,
    var vital_master_name: Any? = null,
    var vital_master_uom: Any? = null,
    var isSelected: Boolean? = false,
    var itemAppendString: String? = "",
    var position: Int? = 0,
    var duration: String? = "1",
    var durationName: String? = "Select duration",
    var durationPeriodId: Int? = 0,
    var selectTypeUUID: Int = 0,
    var selectTypeName: String? = null,
    var selectRouteID: Int = 0,
    var selecteFrequencyID: Int = 0,
    var selectInvestID: Int = 0,
    var selectToLocationUUID: Int = 0,
    var selectedLocationName: String = "",
    var selectToTestMethodUUID: Int = 0,
    var selectToTestMethodName: String? = null,
    var profile_master_uuid: Int = 0,

    var perdayquantityPrescription: String = "",
    var viewRadiologystatus: Int = 0,
    var viewChiefcomplaintstatus: Int = 0,
    var viewPrescriptionstatus: Int = 0,
    var viewDiagnosisstatus: Int = 0,
    var isFavposition: Int = 0,
    var viewLabstatus: Int = 0,
    var isTemposition: Int = 0,
    var code: String = "",
    var template_id: Int = 1,
    var is_snomed: Int = 0,
    var commands: String = "",
    var diagnosisUUiD: Int = 0,
    var favourite_active: Boolean? = false,
    val favourite_code: String? = "",
    val favourite_type_id: Int? = 0,
    val treatment_kit_id: Int? = 0,
    var treatment_kit_type_id: Int? = 0,

    var lab_id: Int? = 0,
    var lab_name: String? = "",
    var lab_code: String? = "",
    var lab_description: String? = "",

    var radiology_id: Int? = 0,
    var radiology_name: String? = "",
    var radiology_code: String? = "",
    var radiology_description: String? = "",

    var investigation_id: Int? = 0,
    var investigation_name: String? = "",
    var investigation_code: String? = "",
    var investigation_description: String? = "",

    var diet_master_id: Int = 0,
    var diet_master_name: String = "",
    var diet_master_code: String = "",
    var diet_quantity: Int = 0,
    var diet_frequency_id: Int = 0,
    var diet_frequency_name: String = "",
    var diet_frequency_code: Int = 0,
    var diet_category_id: Int = 0,
    var diet_category_name: String = "",
    var diet_category_code: String = "",
    var department_id: Int = 0,
    var template_details_uuid: Int = 0,
    var template_details_displayorder: Int = 0,
    var patient_order_uuid: Int = 0,
    var patient_order_details_uuid: Int = 0,
    var collapse: Boolean = true,
    var Update: Boolean = false,
    var drug_is_emar: Boolean? = false,
    var labDataSelected: Boolean? = false,
    var radiologyDataSelected: Boolean? = false,
    var investigationDataSelected: Boolean? = false,
    var isEditableMode: Boolean? = false,
    var isModifiy: Boolean? = false,
    var isReadyForSave: Boolean? = true

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("drug_instruction_code")

    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(chief_complaint_code)
        parcel.writeValue(chief_complaint_id)
        parcel.writeString(chief_complaint_name)
        parcel.writeString(diagnosis_code)
        parcel.writeString(diagnosis_description)
        parcel.writeString(diagnosis_id)
        parcel.writeString(diagnosis_name)
        parcel.writeValue(drug_active)
        parcel.writeValue(drug_duration)
        parcel.writeString(Presstrength)
        parcel.writeString(drug_frequency_id)
        parcel.writeString(drug_frequency_name)
        parcel.writeValue(drug_instruction_id)
        parcel.writeString(drug_instruction_name)
        parcel.writeValue(drug_id)
        parcel.writeString(drug_name)
        parcel.writeString(drug_period_code)
        parcel.writeInt(PrescriptiondurationPeriodId)
        parcel.writeString(drug_period_name)
        parcel.writeValue(drug_route_id)
        parcel.writeString(drug_route_name)
        parcel.writeValue(favourite_details_id)
        parcel.writeValue(favourite_display_order)
        parcel.writeValue(favourite_id)
        parcel.writeValue(test_master_id)
        parcel.writeString(test_master_name)
        parcel.writeValue(isSelected)
        parcel.writeString(itemAppendString)
        parcel.writeValue(position)
        parcel.writeString(duration)
        parcel.writeValue(durationPeriodId)
        parcel.writeInt(selectTypeUUID)
        parcel.writeInt(selectRouteID)
        parcel.writeInt(selecteFrequencyID)
        parcel.writeInt(selectInvestID)
        parcel.writeInt(selectToLocationUUID)
        parcel.writeInt(viewLabstatus)
        parcel.writeString(perdayquantityPrescription)
        parcel.writeInt(viewRadiologystatus)
        parcel.writeInt(viewChiefcomplaintstatus)
        parcel.writeInt(viewPrescriptionstatus)
        parcel.writeInt(viewDiagnosisstatus)
        parcel.writeInt(isFavposition)
        parcel.writeInt(isTemposition)
        parcel.writeString(code)
        parcel.writeInt(template_id)
        parcel.writeInt(is_snomed)
        parcel.writeString(commands)
        parcel.writeInt(diagnosisUUiD)
        parcel.writeValue(favourite_active)
        parcel.writeString(favourite_code)
        parcel.writeValue(favourite_type_id)
        parcel.writeValue(treatment_kit_id)
        parcel.writeValue(treatment_kit_type_id)
        parcel.writeInt(diet_master_id)
        parcel.writeString(diet_master_name)
        parcel.writeString(diet_master_code)
        parcel.writeInt(diet_quantity)
        parcel.writeInt(diet_frequency_id)
        parcel.writeString(diet_frequency_name)
        parcel.writeInt(diet_frequency_code)
        parcel.writeInt(diet_category_id)
        parcel.writeString(diet_category_name)
        parcel.writeString(diet_category_code)
        parcel.writeInt(department_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavouritesModel> {
        override fun createFromParcel(parcel: Parcel): FavouritesModel {
            return FavouritesModel(parcel)
        }

        override fun newArray(size: Int): Array<FavouritesModel?> {
            return arrayOfNulls(size)
        }
    }
}