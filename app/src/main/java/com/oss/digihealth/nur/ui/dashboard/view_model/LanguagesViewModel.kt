package com.oss.digihealth.nur.ui.dashboard.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository

class LanguagesViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var enterOTPEditText = MutableLiveData<String>()
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()

    var errorText = MutableLiveData<String>()

    var progressBar = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null


    init {
        enterOTPEditText.value=""
        enterNewPasswordEditText.value=""
        enterConfirmPasswordEditText.value=""
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
    }



}