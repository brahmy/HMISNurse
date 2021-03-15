package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.viewmodel

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
import com.oss.digihealth.nur.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.templete.TempleResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.VitalSaveRequestModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.VitalsTemplateResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSaveResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSearchListResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual.ResponseEditTemplate
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.MainVItalsListResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.uom.newuom.UOMNewReferernceResponseModel
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class NextVitalsViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    private var department_UUID: Int? = 0
    private var facility_UUID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)

    }

    fun getvitalsTemplate(
        faciltyID: Int?,
        departmentId: Int?,
        vitalsTemplateRetrofitCallBack: RetrofitCallback<VitalsTemplateResponseModel?>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getVitalsTemplatet(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            faciltyID!!, departmentId!!, AppConstants.TEM_TYPE_ID_VITALS, userDataStoreBean?.uuid!!
        )?.enqueue(RetrofitMainCallback(vitalsTemplateRetrofitCallBack))
        return
    }

    fun getUmoList(faciltyID: Int?, umoListRetrofitCallback: RetrofitCallback<UOMNewReferernceResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        // api call for send otp

        val jsonBody = JSONObject()
        try {
            jsonBody.put("sortField", "modified_date")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("status", 1)
            jsonBody.put("table_name", "emr_uom")

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
        apiService?.gatUomVitalList(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            faciltyID!!, body
        )?.enqueue(RetrofitMainCallback(umoListRetrofitCallback))
        return

    }

    fun getVitalsName(faciltyID: Int?, vitalsNametRetrofitCallback: RetrofitCallback<MainVItalsListResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        // api call for send otp

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getVitalsList(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            faciltyID!!
        )?.enqueue(RetrofitMainCallback(vitalsNametRetrofitCallback))
        return

    }

    fun deleteFavourite(facility_id: Int?, favouriteId: Int?, deleteRetrofitCallback: RetrofitCallback<DeleteResponseModel?>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("favouriteId", favouriteId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteRows(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body)?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun vitalSave(faciltyID: Int?, vitalsSaveRetrofitCallback: RetrofitCallback<VitalSaveResponseModel?>, saveData: ArrayList<VitalSaveRequestModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.saveVitals(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, faciltyID!!, saveData)?.enqueue(RetrofitMainCallback(vitalsSaveRetrofitCallback))
        return

    }

    fun searchList(vitalSearchRetrofitCallback: RetrofitCallback<VitalSearchListResponseModel?>, faciltyID: Int?) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getVitals(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, faciltyID!!)?.enqueue(RetrofitMainCallback(vitalSearchRetrofitCallback))
        return

    }

    fun getTemplateDetails(templateId: Int?, facilityUuid: Int?, departmentUuid: Int?, getTemplateRetrofitCallback: RetrofitCallback<ResponseEditTemplate?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getLastVitualTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!, templateId!!, AppConstants.FAV_TYPE_ID_Vitual, departmentUuid!!)?.enqueue(RetrofitMainCallback(getTemplateRetrofitCallback))
        return
    }



    fun deleteTemplate(facility_id : Int?,template_uuid : Int?, deleteRetrofitCallback: RetrofitCallback<DeleteResponseModel?>){
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("template_uuid", template_uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,body)?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun getTemplete(templeteRetrofitCallBack: RetrofitCallback<TempleResponseModel?>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        apiService?.getTemplete(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!, department_UUID!!,facility_UUID!!,
            AppConstants.FAV_TYPE_ID_Vitual
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        return
    }
}



