package com.oss.digihealth.nur.ui.emr_workflow.lab.view_model



import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



class ManageLabTemplateViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageLabTemplateViewModel(
            application
        ) as T
    }
}
