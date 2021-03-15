package com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.viewmodel.NurseDeskVitalsViewModel

class NurseDeskVitalsViewModelFactory(private var application: Application) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NurseDeskVitalsViewModel(
            application
        ) as T
    }
}