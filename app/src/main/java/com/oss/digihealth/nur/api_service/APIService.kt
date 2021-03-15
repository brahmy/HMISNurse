package com.oss.digihealth.nur.api_service

import android.content.Context
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.ui.login.model.login_response_model.LoginResponseModel
import com.oss.digihealth.nur.BuildConfig.BASE_DOMAIN
import com.oss.digihealth.nur.BuildConfig.BASE_URL
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.ui.configuration.model.ConfigResponseModel
import com.oss.digihealth.nur.ui.configuration.model.ConfigUpdateResponseModel
import com.oss.digihealth.nur.ui.dashboard.model.DashBoardResponse
import com.oss.digihealth.nur.ui.dashboard.model.GetGenderReq
import com.oss.digihealth.nur.ui.dashboard.model.GetGenderResp
import com.oss.digihealth.nur.ui.dashboard.model.GetSessionResp
import com.oss.digihealth.nur.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.history.surgery.model.response.SurgeryInstitutionResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.FavAddResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.FavAddTestNameResponse
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.oss.digihealth.nur.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.GetStoreMasterResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.PatientDetailResponse
import com.oss.digihealth.nur.ui.emr_workflow.model.PatientLatestRecordResponse
import com.oss.digihealth.nur.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oss.digihealth.nur.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.templete.TempleResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.prescription.model.ZeroStockResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.VitalSaveRequestModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.VitalsTemplateResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request.VitalFavUpdateRequestModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSaveResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSearchListResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSearchNameResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual.ResponseEditTemplate
import com.oss.digihealth.nur.ui.institute.model.DepartmentResponseModel
import com.oss.digihealth.nur.ui.institute.model.InstitutionResponseModel
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseModel
import com.oss.digihealth.nur.ui.institute.model.Phermisiun.StoreListResponseModel
import com.oss.digihealth.nur.ui.institute.model.wardlist.WardListResponseModel
import com.oss.digihealth.nur.ui.login.model.ChangePasswordOTPResponseModel
import com.oss.digihealth.nur.ui.login.model.LoginSessionRequest
import com.oss.digihealth.nur.ui.login.model.PasswordChangeResponseModel
import com.oss.digihealth.nur.ui.login.model.SimpleResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResultResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseInvestigationPostResponse
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.NurseDeskLabResultResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.NurseLabResponseModule
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.request.requestlabresult
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.samplecollection.NurseLabSampleCollection
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.NurseDeskRadiologyResulyResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.listviewresponse.NurseRadiologyResponse
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.postsample.NursedeskPostsampleResponse
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.model.response.NurseDeskVitalsResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.MainVItalsListResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.uom.newuom.UOMNewReferernceResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.model.NurseUpdateRequestModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface APIService {
    object Factory {
        fun create(context: Context?): APIService {
            val b = OkHttpClient.Builder()
            b.readTimeout(
                AppConstants.TIMEOUT_VALUE.toLong(),
                TimeUnit.MILLISECONDS
            )
            b.writeTimeout(
                AppConstants.TIMEOUT_VALUE.toLong(),
                TimeUnit.MILLISECONDS
            )
            val gson = GsonBuilder()
                .serializeNulls()
                .setLenient()
                .create()
//            if



//            {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
//                val client = b.addNetworkInterceptor(StethoInterceptor()).build()
            val client = b.addInterceptor(ChuckInterceptor(context))
                .addInterceptor(logging)
                .build()
//            }
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return retrofit.create(APIService::class.java)
        }
    }

    //Login
    @FormUrlEncoded
    @POST(Login)
    fun getLoginDetails(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<LoginResponseModel?>?

    @POST(setPassword)
    fun setPassword(
        @Body requestbody: RequestBody?
    ): Call<SimpleResponseModel?>?

    @POST(GetOtpForPasswordChange)
    fun getOtpForPasswordChange(@Body body: RequestBody?): Call<ChangePasswordOTPResponseModel?>?


    @POST(GetPasswordChanged)
    fun getPasswordChanged(@Body body: RequestBody?): Call<PasswordChangeResponseModel?>?


    @POST(Login_session)
    fun LoginSession(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: String,
        @Header("session_id") session_id: String?,
        @Body req: LoginSessionRequest
    ): Call<SimpleResponseModel>

    // ward details
    @POST(GetWardList)
    fun getWardList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<WardListResponseModel?>?

    @POST(GetOfficeList)
    fun getOfficeList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<OfficeResponseModel?>?

    //getInstitutionlistrrrww
    @POST(GetInstitutionList)
    fun getInstitutionList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<InstitutionResponseModel?>?

    /**
     * Get Department List
     *
     * @param authorization
     * @param user_uuid
     * @param body
     * @return
     */
    @POST(GetDepartmentList)
    fun getDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<DepartmentResponseModel?>?


    @POST(GetSurgeryInstitutions)
    fun getFaciltyCheck(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body body: RequestBody?
    ): Call<SurgeryInstitutionResponseModel?>?

    @POST(GetStoreList)
    fun getStoreList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<StoreListResponseModel?>?

    @POST(Logout_Session)
    fun LogoutSeasion(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("session_id") session_id: String?,
        @Body req: RequestBody
    ): Call<SimpleResponseModel>

    //GetEMRWorkFlowForOpAndIp
    @GET(EMRWORKFLOW)
    fun getEmrWorkflowForIpAndOp(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Query("context_uuid") contextId: Int
    ): Call<EmrWorkFlowResponseModel?>?

    //DashBoard
    @GET(DashBoardDetail)
    fun getDashBoardResponse(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Query("depertment_Id") depertment_id: Int,
        @Query("from_date") fromData: String?,
        @Query("to_date") toDate: String?,
        @Query("gender") gender: String?,
        @Query("session") session: String?,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<DashBoardResponse?>?

    @GET(getSession)
    fun getSession(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<GetSessionResp?>?

    @POST(getGender)
    fun getGender(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getGenderReq: GetGenderReq?
    ): Call<GetGenderResp?>?

    @POST(getZeroStock)
    fun getZeroStock(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ZeroStockResponseModel?>?

    @GET(GetEncounters)
    fun getEncounters(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<FectchEncounterResponseModel?>?


    @POST(CreateEncounter)
    fun createEncounter(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body createEncounterRequestModel: CreateEncounterRequestModel?
    ): Call<CreateEncounterResponseModel?>?

    @POST(GetStoreMaster)
    fun getStoreMaster(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetStoreMasterResponseModel?>?

    @GET(getLatestRecord)
    fun getLatestRecordById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Query("patientId") patientId: Int,
        @Query("encounterTypeId") encounterTypeId: Int
    ): Call<PatientLatestRecordResponse?>?

    @POST(getPatientByIdUrl)
    fun getPatientById(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("user_name") user_name: String?,
        @Body body: RequestBody
    ): Call<PatientDetailResponse?>?

    // Nurse Workflow details
    @GET(GetNurseworkflow)
    fun getWorkFlowNurseGetList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<EmrWorkFlowResponseModel?>?


    //Configuation
    @POST(GetConfigList)
    fun getConfigList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body body: RequestBody?
    ): Call<ConfigResponseModel?>?

    @POST(GetNurseconfigCreate)
    fun getNurseConfigCreate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<NurseUpdateRequestModule?>?
    ): Call<ConfigUpdateResponseModel?>?

    //Config Update
    @PUT(GetNurseconfigUpdate)
    fun getNurseConfigUpdate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<NurseUpdateRequestModule?>?
    ): Call<ConfigUpdateResponseModel?>?

    //Labview
    @GET(nursePatientLabDetails)
    fun getnurseLabViewDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int
    ): Call<NurseLabResponseModule?>?

    @POST(getNurseDeskResultLab)
    fun getNurseDeskLabDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: requestlabresult?

    ): Call<NurseDeskLabResultResponseModel?>?


    @POST(postsamplecollectionlabnurse)
    fun getlabpostipsamplecollection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseLabSampleCollection?>?

    //nurseDeSkInvestigationview
    @GET(getNurseDeskInvestigation)
    fun getNurseDeskInvestigationDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int

    ): Call<NurseDeskInvestigationResponseModel?>?

    @POST(getNurseDeskResultInvestigation)
    fun getNurseDeskResultInvestigationDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseDeskInvestigationResultResponseModel?>?

    @POST(postInvestigationnurse)
    fun getInvestigationpostipsamplecollection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseInvestigationPostResponse?>?

    @GET(getNurseDeskInvestigation)
    fun getNurseInvestigationData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int

    ): Call<NurseDeskInvestigationResponseModel?>?

    @Streaming
    @POST(GetDownload)
    fun getResultDownload(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @POST(getNurseDeskResultRadiology)
    fun getNurseDeskRadiologyResultDetails(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseDeskRadiologyResulyResponseModel?>?


    //Radiology
    @GET(getNurseRadilogyData)
    fun getNurseRadilogyData(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("ward_uuid") ward_uuid: Int

    ): Call<NurseRadiologyResponse?>?

    //getBedmanagemt
    @POST(postipsamplecollection)
    fun getpostipsamplecollection(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NursedeskPostsampleResponse?>?

    //NurseVitals
    @POST(getBedmanagemt)
    fun getNurseDeskVitalsList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?

    ): Call<NurseDeskVitalsResponseModel?>?

    @GET(GetVitalsTemplatet)
    fun getVitalsTemplatet(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("dept_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("user_uuid") user_uid: Int
    ): Call<VitalsTemplateResponseModel?>?

    @POST(GetAllergySource)
    fun gatUomVitalList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<UOMNewReferernceResponseModel?>?

    @GET(getVitalsList)
    fun getVitalsList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<MainVItalsListResponseModel?>?

    @PUT(DeleteRows)
    fun deleteRows(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DeleteResponseModel?>?

    @POST(VitalSave)
    fun saveVitals(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body configRequestData: ArrayList<VitalSaveRequestModel>
    ): Call<VitalSaveResponseModel?>?

    @GET(VitalSearch)
    fun getVitals(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): Call<VitalSearchListResponseModel?>?

    @GET(VitualGetTemplate)
    fun getLastVitualTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("dept_id") dept_id: Int
    ): Call<ResponseEditTemplate?>?

    @PUT(DeleteTemplate)
    fun deleteTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DeleteResponseModel?>?

    /*
        Templete
       */
    @GET(GetTemplete)
    fun getTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("dept_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int
    ): Call<TempleResponseModel?>?

    //VitalNameSearch
    @POST(getVitalSearchName)
    fun getVitalSearchNameNew(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<VitalSearchNameResponseModel?>?

    @POST(LabTemplateCreate)
    fun createTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestTemplateAddDetails?
    ): Call<ReponseTemplateadd?>?

    @PUT(LabUpdateTemplate)
    fun getVitalTemplateUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: VitalFavUpdateRequestModel?
    ): Call<UpdateResponse?>?

    @POST(GetFavDepartmentList)
    fun getFavDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddResponseModel?>?

    @POST(GetFavaddDepartmentList)
    fun getFavddAllADepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddAllDepatResponseModel?>?

    //DEVHMIS-LIS/v1/api/testmaster/gettestandprofileinfo
    @POST(GetLabSearchResult)
    fun getAutocommitText(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddTestNameResponse?>?

    @PUT(LabUpdateTemplate)
    fun getTemplateUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: UpdateRequestModule?
    ): Call<UpdateResponse?>?


    companion object {

        const val Login =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/loginNew"
        const val setPassword =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/newUser_changePassword"
        const val GetOtpForPasswordChange =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/sendOtp"
        const val GetPasswordChanged =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/changePassword"
        const val Login_session =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/login_session"

        /*Nurse Desk*/
        const val GetWardList =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/ward/getwardbyloggedyinfacility"
        const val GetOfficeList =
            BASE_DOMAIN + "Appmaster/v1/api/facility/getUserOfficeByUserId"
        const val GetInstitutionList =
            BASE_DOMAIN + "Appmaster/v1/api/facility/getFacilityByHealthOfficeId"
        const val GetDepartmentList =
            BASE_DOMAIN + "Appmaster/v1/api/manageInstitution/getManageInstitutionByUFId"

        const val GetSurgeryInstitutions =
            BASE_DOMAIN + "Appmaster/v1/api/userFacility/getUserFacilityByUId"

        const val GetStoreList =
            BASE_DOMAIN + "HMIS-INVENTORY/v1/api/storeMaster/getStoreMasterByFacilityId"

        const val Logout_Session =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/is_current_loginuser"

        const val EMRWORKFLOW =
            BASE_DOMAIN + "HMIS-EMR/v1/api/emr-workflow-settings/getEMRWorkflowByUserId"

        //Dashboard
        const val getSession =
            BASE_DOMAIN + "Appmaster/v1/api/session/getSession"
        const val DashBoardDetail =
            BASE_DOMAIN + "HMIS-EMR/v1/api/dashboard/getDashBoarddata"
        const val getGender =
            BASE_DOMAIN + "Appmaster/v1/api/gender/getGender"
        const val getZeroStock =
            BASE_DOMAIN + "HMIS-INVENTORY/v1/api/stockItems/getZeroStockItems"

        const val GetEncounters =
            BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"

        const val CreateEncounter =
            BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/create"
        const val GetStoreMaster =
            BASE_DOMAIN + "HMIS-INVENTORY/v1/api/storeMaster/getStoreDepartmentById"

        //get latest encounter
        const val getLatestRecord =
            BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/get-latest-enc-by-patient"

        const val getPatientByIdUrl =
            BASE_DOMAIN + "registration/v1/api/patient/getById"

        const val GetNurseworkflow =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/work-flow/getworkflowsettingsByUserID"

        const val GetConfigList =
            BASE_DOMAIN + "Appmaster/v1/api/contextActivityMap/getMappingByContextId"

        const val GetNurseconfigCreate =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/work-flow/create"

        const val GetNurseconfigUpdate =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/work-flow/update"

        const val nursePatientLabDetails =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/lab/getPatientLabDetails"

        const val getNurseDeskResultLab =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorderdetails/getauthorizedlabresults"

        //Labsamplecollection
        const val postsamplecollectionlabnurse =
            BASE_DOMAIN + "HMIS-LIS/v1/api/ipsamplecollection/postipsamplecollection"

        //nurseDeskInvestigation
        const val getNurseDeskInvestigation =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/investigation/getPatientInvestigationDetails?"
        const val getNurseDeskResultInvestigation =
            BASE_DOMAIN + "HMIS-INV/v1/api/patientworkorderdetails/getAuthorizedRadiologyResults"

        //PostInves
        const val postInvestigationnurse =
            BASE_DOMAIN + "HMIS-INV/v1/api/ipsamplecollection/postipsamplecollection"
        const val GetDownload =
            BASE_DOMAIN + "Fileserver/v1/api/file/read"

        //nurseDEskRadiologyREsult
        const val getNurseDeskResultRadiology =
            BASE_DOMAIN + "HMIS-RMIS/v1/api/patientworkorderdetails/getAuthorizedRadiologyResults"
        //Radiology nurse
        const val getNurseRadilogyData =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/radiology/getPatientRadiologyDetails"

        ///postipsamplecollection
        const val postipsamplecollection =
            BASE_DOMAIN + "HMIS-RMIS/v1/api/ipsamplecollection/postipsamplecollection"

        const val getVitalsList =
            BASE_DOMAIN + "HMIS-EMR/v1/api/vitalMaster/getVitals"

        const val getBedmanagemt =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/nurseDesk/bed-management/getPatientList"

        const val GetVitalsTemplatet =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettemplateByID"

        const val GetAllergySource =
            BASE_DOMAIN + "HMIS-EMR/v1/api/commonReference/getReference"

        const val DeleteRows =
            BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/delete"

        const val VitalSave =
            BASE_DOMAIN + "HMIS-EMR/v1/api/emr-patient-vitals/create"

        const val VitalSearch =
            BASE_DOMAIN + "HMIS-EMR/v1/api/vitalMaster/getAllVitals"

        const val VitualGetTemplate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettempdetails"

        const val DeleteTemplate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/deleteTemplateDetails"

        const val GetTemplete =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettemplateByID"

        //Vitals
        const val getVitalSearchName =
            BASE_DOMAIN + "HMIS-EMR/v1/api/vitalMaster/getALLVitalsFilter"

        const val LabTemplateCreate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/create"

        const val LabUpdateTemplate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/updatetemplateById"

        const val GetFavDepartmentList =
            BASE_DOMAIN + "Appmaster/v1/api/department/getDepartmentOnlyById"

        const val GetFavaddDepartmentList =
            BASE_DOMAIN + "Appmaster/v1/api/department/getAllDepartment"

        const val GetLabSearchResult =
            BASE_DOMAIN + "HMIS-LIS/v1/api/testmaster/gettestandprofileinfo"

    }
}