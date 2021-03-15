package com.oss.digihealth.nur.ui.login.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TermsAndConditionFactory(
    private var application: Application?

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TermsAndConditionViewModel(
            application!!

        ) as T
    }
}
