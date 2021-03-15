package com.oss.digihealth.nur.ui.institute.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.DialogFragment


import androidx.databinding.DataBindingUtil

import com.google.gson.GsonBuilder

import com.oss.digihealth.doc.utils.Utils
import retrofit2.Response

import android.app.Dialog
import android.content.Intent
import android.view.MotionEvent
import com.google.gson.Gson
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogSelectInstituteBinding
import com.oss.digihealth.nur.ui.emr_workflow.history.surgery.model.response.InstitutionresponseContent
import com.oss.digihealth.nur.ui.emr_workflow.history.surgery.model.response.SurgeryInstitutionResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.FavouritesResponseModel
import com.oss.digihealth.nur.ui.institute.model.DepartmentResponseModel
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseContent
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseModel
import com.oss.digihealth.nur.ui.institute.view_model.InstituteViewModel
import com.oss.digihealth.nur.ui.quick_reg.model.LocationMaster
import com.oss.digihealth.nur.ui.quick_reg.model.LocationMasterResponseModel


class SelectInstituteDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var labuuid: Int? = null
    private var office_UUID: Int? = null
    private var institution_NAME: String? = null
    private var content: String? = null
    private var viewModel: InstituteViewModel? = null
    var binding: DialogSelectInstituteBinding? = null
    private var utils: Utils? = null
    private var officeDropDownAdapter: OfficeDropDownAdapter? = null
    private var institutionDropDownAdapter: SelectInstituteDropDownAdapter? = null
    private var departmentDropDownAdapter: LabListAdapter? = null
    private var arraylist_institution: ArrayList<InstitutionresponseContent?> = ArrayList()
    private var arraylist_department: ArrayList<LocationMaster?> = ArrayList()
    private var arraylist_office: ArrayList<OfficeResponseContent?> = ArrayList()
    var appPreferences: AppPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_select_institute, container, false)
/*
        viewModel = InstituteViewModelFactory(
            requireActivity().application,
            officeRetrofitCallBack
        )
            .create(InstituteViewModel::class.java)
*/
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        office_UUID = appPreferences?.getInt(AppConstants.OFFICE_UUID)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        ClearData()
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.clear?.setOnClickListener {
            ClearData()
        }
        binding?.save?.setOnClickListener {
            if (labuuid != 0 && facilitylevelID != 0) {

                //department_uuid != 0 &&
                /*  utils?.showToast(
                      R.color.positiveToast,
                      binding?.mainLayout!!,
                      getString(R.string.data_save)
                  )*/

//                AnalyticsManager.getAnalyticsManager().trackLoginSuccess(requireContext())

//                startActivity(Intent(context, MainLandScreenActivity::class.java))

//                requireActivity().finish()
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.empty_item)
                )
            }
        }


//        viewModel!!.getfacilityCallback(facilitycallbackRetrofitCallback)




        binding?.spinnerInstitution!!.setOnTouchListener { v, event ->
            when (event?.action) {
/*
                MotionEvent.ACTION_DOWN ->

                    viewModel!!.getfacilityCallback(facilitycallbackRetrofitCallback)
*/

            }

            v?.onTouchEvent(event) ?: true
        }

        binding?.spinnerInstitution?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != institutionDropDownAdapter?.count!!) {
                    val institutionListGetDetails = institutionDropDownAdapter?.getlistDetails()

                    facilitylevelID = institutionListGetDetails?.get(position)?.facility_uuid
                    institution_NAME = institutionListGetDetails?.get(position)?.facility!!.name
                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                    appPreferences?.saveString(AppConstants.INSTITUTION_NAME, institution_NAME!!)
                    if (facilitylevelID != 0) {
//                        viewModel?.getDepartmentList(facilitylevelID,departmentRetrofitCallBack)
                    }
                    return
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding?.spinnerDeparment?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val departmentListGetDetails = departmentDropDownAdapter?.getlistDetails()

                department_uuid = departmentListGetDetails?.get(position)?.department_uuid

                appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, department_uuid!!)

                labuuid= departmentListGetDetails?.get(position)!!.uuid

                appPreferences?.saveInt(AppConstants.LAB_UUID,
                    departmentListGetDetails.get(position)!!.uuid
                )

                var tolocationMap= departmentListGetDetails.get(position)!!.to_location_department_maps

                if(tolocationMap.isNotEmpty()) {

                    var otherdepaertment: ArrayList<Int> = ArrayList()

                    for (i in tolocationMap.indices) {

                        otherdepaertment.add(tolocationMap[i].department_uuid)

                    }

                    val res = otherdepaertment.toString()

                    Log.i("department", "" + res)

                    appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID,res)
                }

                //   appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID, departmentListGetDetails?.get(position)!!.uuid!!)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return binding?.root
    }

    private fun ClearData() {

        arraylist_institution.clear()
        institutionDropDownAdapter = SelectInstituteDropDownAdapter(requireContext(), ArrayList())
        arraylist_institution.add(InstitutionresponseContent())
        institutionDropDownAdapter?.setInstitutionListDetails(arraylist_institution)
        binding?.spinnerInstitution?.adapter = institutionDropDownAdapter

        arraylist_department.clear()
        departmentDropDownAdapter = LabListAdapter(requireContext(), ArrayList())
        arraylist_department.add(LocationMaster())
        departmentDropDownAdapter?.setDepatmentListDetails(arraylist_department)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter

        appPreferences?.saveInt(AppConstants.OFFICE_UUID, 0)
        appPreferences?.saveString(AppConstants.OFFICE_NAME, "")
        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
        appPreferences?.saveInt(AppConstants.FACILITY_UUID, 0)

        viewModel?.getOfficeList()
    }

    val officeRetrofitCallBack =
        object : RetrofitCallback<OfficeResponseModel?> {
            override fun onSuccessfulResponse(response: Response<OfficeResponseModel?>) {
                Log.i("", "" + response.body())
                if(response.body()?.responseContents!!.isNotEmpty())
                {
                    officeDropDownAdapter?.setOfficeListDetails(response.body()?.responseContents as ArrayList<OfficeResponseContent?>?)

                }

            }

            override fun onBadRequest(response: Response<OfficeResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message!!
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String?) {
                if(failure!=null) {
                    utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val facilitycallbackRetrofitCallback = object : RetrofitCallback<SurgeryInstitutionResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<SurgeryInstitutionResponseModel?>) {

            Log.i("",""+responseBody?.body()?.responseContents)
            Log.i("",""+responseBody?.body()?.responseContents)
            Log.i("",""+responseBody?.body()?.responseContents)

            institutionDropDownAdapter?.setInstitutionListDetails(responseBody!!.body()?.responseContents as ArrayList<InstitutionresponseContent?>?)

            facilitylevelID = responseBody?.body()?.responseContents?.get(0)?.facility_uuid

            appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)

            binding?.spinnerInstitution?.adapter = institutionDropDownAdapter


        }
        override fun onBadRequest(response: Response<SurgeryInstitutionResponseModel?>) {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,"Bad Request")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.unauthorized))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,s)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val departmentRetrofitCallBack =
        object : RetrofitCallback<DepartmentResponseModel?> {
            override fun onSuccessfulResponse(response: Response<DepartmentResponseModel?>) {
                Log.i("", "" + response.body())

                val datas=response.body()!!.responseContents

                var departmentList:ArrayList<Int> = ArrayList()

                for(i in datas!!.indices){

                    departmentList.add(datas[i]!!.department_uuid!!)
                }



/*
                viewModel!!.getLocationMaster(departmentList,
                    facilitylevelID!!,LocationMasterResponseCallback)
*/



            }

            override fun onBadRequest(response: Response<DepartmentResponseModel?>) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,"Bad Request")
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.message!!
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>?) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String?) {

                if(failure!=null) {

//                    AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,failure)

                    utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)

                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val LocationMasterResponseCallback = object : RetrofitCallback<LocationMasterResponseModel?> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModel?>) {
            Log.i("","locationdata"+responseBody!!.body()!!.responseContents)
            val data= responseBody.body()!!.responseContents
            if(data.isNotEmpty()) {

                departmentDropDownAdapter?.setDepatmentListDetails(responseBody.body()?.responseContents as ArrayList<LocationMaster?>?)
                binding?.spinnerDeparment?.adapter = departmentDropDownAdapter

            }

        }
        override fun onBadRequest(errorBody: Response<LocationMasterResponseModel?>) {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,"Bad Request")
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LocationMasterResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }

        }

        override fun onServerError(response: Response<*>?) {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.unauthorized))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
//            AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!,s)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8

        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }


}


