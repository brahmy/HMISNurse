package com.oss.digihealth.nur.ui.nursedesk.viewmodel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.application.HmisApplication
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.callbacks.RetrofitMainCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.ui.configuration.model.ConfigResponseModel
import com.oss.digihealth.nur.ui.configuration.model.ConfigUpdateResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.model.NurseUpdateRequestModule
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class NurseConfigViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }
    fun getConfigList(
        contextId: Int?,
        configRetrofitCallBack: RetrofitCallback<ConfigResponseModel?>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        val jsonBody = JSONObject()
        try {
            jsonBody.put("context_uuid", contextId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getConfigList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(configRetrofitCallBack))
        return
    }



    fun postRequestParameter(
        facility_id: Int?,
        configRequestData: ArrayList<NurseUpdateRequestModule?>,
        configFinalRetrofitCallBack: RetrofitCallback<ConfigUpdateResponseModel?>,
        configStatus: Boolean
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if(configStatus){


            apiService?.getNurseConfigCreate("en",
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!,
                configRequestData
            )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        }
        else {


            apiService?.getNurseConfigUpdate("en",
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!,
                configRequestData
            )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        }
        return

    }

    fun getEmrWorkFlowFav(emrWorkFlowRetrofitCallBack: RetrofitCallback<EmrWorkFlowResponseModel?>, contextId: Int?) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getWorkFlowNurseGetList("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,contextId!!
        )!!.enqueue(
            RetrofitMainCallback(emrWorkFlowRetrofitCallBack)
        )
    }



}