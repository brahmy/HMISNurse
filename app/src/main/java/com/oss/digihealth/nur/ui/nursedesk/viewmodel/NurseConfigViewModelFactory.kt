package com.oss.digihealth.nur.ui.nursedesk.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NurseConfigViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NurseConfigViewModel(
            application

        ) as T
    }
}
