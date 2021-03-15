package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.view_model

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
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.NurseDeskLabResultResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.NurseLabResponseModule
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.request.requestlabresult
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.samplecollection.NurseLabSampleCollection
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class NurseDeskLabViewModel(
    application: Application
) : AndroidViewModel(
    application
) {

    private var department_UUID: Int? = 0
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences : AppPreferences? = null
    var facilityid:Int?=0
    var wardid:Int?=0
    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facilityid = appPreferences?.getInt(AppConstants?.FACILITY_UUID)
        wardid = appPreferences?.getInt(AppConstants?.WARDUUID)
    }


    fun getLabResultData(labResultNurseRetrofitCallBack: RetrofitCallback<NurseLabResponseModule?>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getnurseLabViewDetails("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facilityid!!,wardid!!
        )!!.enqueue(
            RetrofitMainCallback(labResultNurseRetrofitCallBack)
        )
    }

    fun getNurseDeskREsultInvestigationDetails(
        PatientUUID: requestlabresult,
        labViewListRetrofitCallBack: RetrofitCallback<NurseDeskLabResultResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getNurseDeskLabDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityid!!,PatientUUID

        )!!.enqueue(
            RetrofitMainCallback(labViewListRetrofitCallBack)
        )


    }

    fun getLabSampleCollection(uuid: Int?, labSampleCollectionNurseRetrofitCallBack: RetrofitCallback<NurseLabSampleCollection?>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_order_test_details_uuid", uuid)

            jsonBody.put("ward_uuid", wardid)
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
        apiService?.getlabpostipsamplecollection("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facilityid!!,body
        )!!.enqueue(
            RetrofitMainCallback(labSampleCollectionNurseRetrofitCallBack)
        )
    }



}