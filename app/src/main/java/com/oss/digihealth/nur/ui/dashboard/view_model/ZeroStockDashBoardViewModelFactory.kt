package com.oss.digihealth.nur.ui.dashboard.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ZeroStockDashBoardViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ZeroStockDashBoardViewModel(
            application
        ) as T
    }
}