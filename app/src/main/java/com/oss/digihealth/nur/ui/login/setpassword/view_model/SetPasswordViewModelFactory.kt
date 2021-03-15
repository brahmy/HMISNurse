package com.oss.digihealth.nur.ui.login.setpassword.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SetPasswordViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SetPasswordViewModel(
            application
        ) as T
    }
}
