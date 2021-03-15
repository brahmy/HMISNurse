package com.oss.digihealth.nur.ui.institute.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseModel

class InstituteViewModelFactory(
    private var application: Application?,
    private var officeRetrofitCallBack: RetrofitCallback<OfficeResponseModel?>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InstituteViewModel(
            application,
            officeRetrofitCallBack
        ) as T
    }
}
