package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.viewmodel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NextVitalsViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NextVitalsViewModel(
            application
        ) as T
    }
}
