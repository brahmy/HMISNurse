package com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.view_model.NurseDeskRadiologyViewModel

class NurseDeskRadiologyViewModelFactory(private var application: Application) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NurseDeskRadiologyViewModel(
            application
        ) as T
    }
}
