package com.oss.digihealth.nur.ui.login.view_model


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class TermsAndConditionViewModel(
    application: Application

) : AndroidViewModel(application) {

    var application = getApplication<Application>().applicationContext
    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()
    var viewClick = MutableLiveData<Int>()

    init {
        viewClick.value = 0
    }
}
