package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.view_model

import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository


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
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResultResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseInvestigationPostResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject


class NurseDeskInvestigationViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    private var department_UUID: Int? = 0
    private var facilityID: Int? = 0
    private var ward_UUid: Int? = 0

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences : AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        ward_UUid = appPreferences?.getInt(AppConstants?.WARDUUID)

    }



    fun getNurseDeskInvestigationDetails(labViewListRetrofitCallBack: RetrofitCallback<NurseDeskInvestigationResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getNurseDeskInvestigationDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!, ward_UUid!!

        )!!.enqueue(
            RetrofitMainCallback(labViewListRetrofitCallBack)
        )


    }
    fun getNurseDeskREsultInvestigationDetails(PatientUUID:Int?,labViewListRetrofitCallBack: RetrofitCallback<NurseDeskInvestigationResultResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", PatientUUID)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getNurseDeskResultInvestigationDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,body

        )!!.enqueue(
            RetrofitMainCallback(labViewListRetrofitCallBack)
        )


    }
    fun getInvestigationSampleCollection(uuid: Int?, radiologySampleCollectionNurseRetrofitCallBack: RetrofitCallback<NurseInvestigationPostResponse?>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_order_test_details_uuid", uuid)

            jsonBody.put("ward_uuid", ward_UUid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getInvestigationpostipsamplecollection("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facilityID!!,body
        )!!.enqueue(
            RetrofitMainCallback(radiologySampleCollectionNurseRetrofitCallBack)
        )
    }

    fun getInvestigationResultData(investigationResultNurseRetrofitCallBack: RetrofitCallback<NurseDeskInvestigationResponseModel?>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getNurseInvestigationData("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facilityID!!,ward_UUid!!
        )!!.enqueue(
            RetrofitMainCallback(investigationResultNurseRetrofitCallBack)
        )
    }

    fun getImage(filePath: String?,  downloadfile: RetrofitCallback<ResponseBody?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("filePath", filePath)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progress.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getResultDownload("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facilityID!!, body)?.enqueue(RetrofitMainCallback(downloadfile))

    }


}