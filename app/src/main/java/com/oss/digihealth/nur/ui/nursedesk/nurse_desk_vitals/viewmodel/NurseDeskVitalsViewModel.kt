package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.viewmodel

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
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.model.response.NurseDeskVitalsResponseModel
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class NurseDeskVitalsViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    private var department_UUID: Int? = 0
    private var facilityID: Int? = 0
    private var wardID: Int? = 0
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences : AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        wardID = appPreferences?.getInt(AppConstants.WARDUUID)
    }


    fun getNurseVitals(pageno: Int, pageSize: Int, search: String, searchUhid: String, patientName : String, IPNumber : String, nurseVitalsListRetrofitCallBack: RetrofitCallback<NurseDeskVitalsResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("ward_uuid",wardID.toString())
            jsonBody.put("search_uhid", searchUhid)
            jsonBody.put("search_patient_name", patientName)
            jsonBody.put("search_ip_no", IPNumber)
            jsonBody.put("pageNo", pageno)
            jsonBody.put("pageSize", pageSize)
            jsonBody.put("sortField", "modified_date")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("search", search)

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
        apiService?.getNurseDeskVitalsList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,body)!!.enqueue(
            RetrofitMainCallback(nurseVitalsListRetrofitCallBack)
        )
    }

    fun getNurseVitalsNextPage(pageno: Int, pageSize: Int, search: String, searchUhid: String, patientName : String, nurseVitalsListRetrofitCallBack: RetrofitCallback<NurseDeskVitalsResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("ward_uuid", wardID.toString())
            jsonBody.put("search_uhid", searchUhid)
            jsonBody.put("search_patient_name", patientName)
            jsonBody.put("pageNo", pageno)
            jsonBody.put("pageSize", pageSize)
            jsonBody.put("sortField", "modified_date")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("search", search)

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
        apiService?.getNurseDeskVitalsList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!, body
        )!!.enqueue(
            RetrofitMainCallback(nurseVitalsListRetrofitCallBack)
        )
    }
}