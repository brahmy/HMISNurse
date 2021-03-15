package com.oss.digihealth.nur.ui.landingscreen

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.application.HmisApplication
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.callbacks.RetrofitMainCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.ui.login.model.SimpleResponseModel
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class MainLandViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    fun LogOutseassion(
        FacilityId: Int,
        loginSeasionRetrofitCallBack: RetrofitCallback<SimpleResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        val jsonBody = JSONObject()
        try {
            jsonBody.put("session_id", userDataStoreBean?.SessionId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.LogoutSeasion(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            FacilityId,
            userDataStoreBean?.SessionId,
            body
        )?.enqueue(RetrofitMainCallback(loginSeasionRetrofitCallBack))
        return


    }

    var errorText = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
    }
    /*  fun getEmrWorkFlow(emrWorkFlowRetrofitCallBack: RetrofitCallback<EmrWorkFlowResponseModel>) {
          if (!Utils.isNetworkConnected(getApplication())) {
              errorText.value = getApplication<Application>().getString(R.string.no_internet)
              return
          }
          progressBar.value = 0
          val aiiceApplication = HmisApplication.get(getApplication())
          val apiService = aiiceApplication.getRetrofitService()
          val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
          apiService?.getEmrWorkflow(
              BEARER_AUTH+userDataStoreBean?.access_token,
              userDataStoreBean?.uuid!!)!!.enqueue(
              RetrofitMainCallback(emrWorkFlowRetrofitCallBack)
          )
      }

      fun getPatientsDetails(dashBoardDetailRetrofitCallBack: RetrofitCallback<DashBoardResponse>){
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
              27343,88,"2020-04-01","2020-04-24")!!.enqueue(
              RetrofitMainCallback(dashBoardDetailRetrofitCallBack)
          )
      }*/
}