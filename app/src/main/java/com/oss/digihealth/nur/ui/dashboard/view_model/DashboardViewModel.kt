package com.oss.digihealth.nur.ui.dashboard.view_model
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.application.HmisApplication
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.callbacks.RetrofitMainCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppConstants.BEARER_AUTH
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.ui.dashboard.model.DashBoardResponse
import com.oss.digihealth.nur.ui.dashboard.model.GetGenderReq
import com.oss.digihealth.nur.ui.dashboard.model.GetGenderResp
import com.oss.digihealth.nur.ui.dashboard.model.GetSessionResp
import com.oss.digihealth.nur.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.prescription.model.ZeroStockResponseModel
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class DashboardViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var errorText = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    init {
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }
    fun getEmrWorkFlow(emrWorkFlowRetrofitCallBack: RetrofitCallback<EmrWorkFlowResponseModel?>, contextId: Int) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getEmrWorkflowForIpAndOp(
            BEARER_AUTH+userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,contextId)!!.enqueue(
            RetrofitMainCallback(emrWorkFlowRetrofitCallBack)
        )
    }

    fun getPatientsDetails(
        departmentId: Int,
        fromDate: String,
        toDate: String,
        gender: String,
        session: String,
        facility_uuid: Int,
        dashBoardDetailRetrofitCallBack: RetrofitCallback<DashBoardResponse?>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getDashBoardResponse(
            BEARER_AUTH+userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            departmentId,
            fromDate,
            toDate,
            gender,
            session,facility_uuid
        )!!.enqueue(RetrofitMainCallback(dashBoardDetailRetrofitCallBack))
    }

    fun getSession(
        facility_uuid: Int,
        getSessionRespCallback: RetrofitCallback<GetSessionResp?>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getSession(
            "accept",
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid
        )?.enqueue(RetrofitMainCallback(getSessionRespCallback))
    }

    fun getGender(
        facility_uuid: Int,
        getGenderReq: GetGenderReq,
        getGenderRespCallback: RetrofitCallback<GetGenderResp?>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getGender(
            "accept",
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getGenderReq
        )?.enqueue(RetrofitMainCallback(getGenderRespCallback))
    }


    fun getZeroStock(facility_uuid: Int,favAddTestNameCallBack: RetrofitCallback<ZeroStockResponseModel?>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("sortField", "item_master.name")
            jsonBody.put("sortOrder","desc" )
            jsonBody.put("pageSize", "10")
            jsonBody.put("pageNo", "0")
            jsonBody.put("store_master_uuid",31)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getZeroStock(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facility_uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddTestNameCallBack))
        return
    }



}

