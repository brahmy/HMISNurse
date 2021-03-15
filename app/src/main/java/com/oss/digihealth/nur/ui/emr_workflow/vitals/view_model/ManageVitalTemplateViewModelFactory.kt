package com.oss.digihealth.nur.ui.emr_workflow.vitals.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ManageVitalTemplateViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageVitalTemplateViewModel(
            application
        ) as T
    }
}