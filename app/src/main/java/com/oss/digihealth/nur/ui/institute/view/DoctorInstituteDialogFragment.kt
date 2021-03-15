package com.oss.digihealth.nur.ui.institute.view


import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogDoctorInstituteListBinding
import com.oss.digihealth.nur.ui.emr_workflow.history.surgery.model.response.InstitutionresponseContent
import com.oss.digihealth.nur.ui.emr_workflow.history.surgery.model.response.SurgeryInstitutionResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.FavouritesResponseModel
import com.oss.digihealth.nur.ui.institute.model.DepartmentResponseContent
import com.oss.digihealth.nur.ui.institute.model.DepartmentResponseModel
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseContent
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseModel
import com.oss.digihealth.nur.ui.institute.view_model.InstituteViewModel
import retrofit2.Response


class DoctorInstituteDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var office_UUID: Int? = null
    private var institution_NAME: String? = null
    private var content: String? = null
    private var departmentname: String? = ""
    private var viewModel: InstituteViewModel? = null
    var binding: DialogDoctorInstituteListBinding? = null
    private var utils: Utils? = null
    private var officeDropDownAdapter: OfficeDropDownAdapter? = null
    private var institutionDropDownAdapter: SelectInstituteDropDownAdapter? = null
    private var departmentDropDownAdapter: DepartmentDropDownAdapter? = null
    private var arraylist_institution: ArrayList<InstitutionresponseContent?> = ArrayList()
    private var arraylist_department: ArrayList<DepartmentResponseContent?> = ArrayList()
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
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_doctor_institute_list,
                container,
                false
            )
/*
        viewModel = InstituteViewModelFactory(
            requireActivity().application,
            officeRetrofitCallBack
        )
            .create(InstituteViewModel::class.java)
*/

        viewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(Application()).create(InstituteViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        ClearData()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        office_UUID = appPreferences?.getInt(AppConstants.OFFICE_UUID)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        viewModel!!.getfacilityCallback(facilitycallbackRetrofitCallback)

        binding?.clear?.setOnClickListener {
            ClearData()
        }
        binding?.save?.setOnClickListener {
            if (department_uuid != 0 && facilitylevelID != 0) {
                /*  utils?.showToast(
                      R.color.positiveToast,
                      binding?.mainLayout!!,
                      getString(R.string.data_save)
                  )*/


                appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                appPreferences?.saveString(AppConstants.INSTITUTION_NAME, institution_NAME!!)

                appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, department_uuid!!)

                appPreferences?.saveString(AppConstants.DEPARTMENT_NAME, departmentname!!)

//                startActivity(Intent(context, MainLandScreenActivity::class.java))

                requireActivity().finish()
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.empty_item)
                )
            }
        }

        binding?.spinnerInstitution!!.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN ->

                    viewModel!!.getfacilityCallback(facilitycallbackRetrofitCallback)

            }

            v?.onTouchEvent(event) ?: true
        }




        return binding?.root
    }

    private fun ClearData() {
        arraylist_office.clear()
        officeDropDownAdapter = OfficeDropDownAdapter(requireContext(), ArrayList())
        arraylist_office.add(OfficeResponseContent())
        officeDropDownAdapter?.setOfficeListDetails(arraylist_office)


        arraylist_institution.clear()
        institutionDropDownAdapter = SelectInstituteDropDownAdapter(requireContext(), ArrayList())
        arraylist_institution.add(InstitutionresponseContent())
        institutionDropDownAdapter?.setInstitutionListDetails(arraylist_institution)
        binding?.spinnerInstitution?.adapter = institutionDropDownAdapter

        arraylist_department.clear()
        departmentDropDownAdapter = DepartmentDropDownAdapter(requireContext(), ArrayList())
        arraylist_department.add(DepartmentResponseContent())
        departmentDropDownAdapter?.setDepatmentListDetails(arraylist_department)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter


        facilitylevelID = 0
        institution_NAME = ""

        department_uuid = 0

        departmentname = ""

        /*appPreferences?.saveInt(AppConstants.OFFICE_UUID, 0)
        appPreferences?.saveString(AppConstants.OFFICE_NAME, "")
        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
        appPreferences?.saveInt(AppConstants.FACILITY_UUID, 0)
*/

    }

    val officeRetrofitCallBack =
        object : RetrofitCallback<OfficeResponseModel?> {
            override fun onSuccessfulResponse(response: Response<OfficeResponseModel?>) {
                Log.i("", "" + response.body())
                if (response.body()?.responseContents!!.isNotEmpty()) {
                    /*   officeDropDownAdapter?.setOfficeListDetails(response.body()?.responseContents as ArrayList<OfficeResponseContent?>?)
                       binding?.spinnerOfficelist?.adapter = officeDropDownAdapter
                       binding?.spinnerOfficelist?.setSelection(officeDropDownAdapter?.count!!);*/
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
                if(failure!=null)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }
/*

    val instituteRetrofitCallBack =
        object : RetrofitCallback<InstitutionResponseModel> {
            override fun onSuccessfulResponse(response: Response<InstitutionResponseModel>) {

                Log.i("Instection",""+response.body()?.responseContents)
                Log.i("Instection",""+response.body()?.responseContents)
                Log.i("Instection",""+response.body()?.responseContents)


                institutionDropDownAdapter?.setInstitutionListDetails(response.body()?.responseContents as ArrayList<InstitutionResponseContent?>?)
                binding?.spinnerInstitution?.adapter = institutionDropDownAdapter
            }

            override fun onBadRequest(response: Response<InstitutionResponseModel>) {
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

            override fun onServerError(response: Response<*>) {
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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    */


    val facilitycallbackRetrofitCallback =
        object : RetrofitCallback<SurgeryInstitutionResponseModel?> {

            override fun onSuccessfulResponse(responseBody: Response<SurgeryInstitutionResponseModel?>) {

                Log.i("", "" + responseBody?.body()?.responseContents)
                Log.i("", "" + responseBody?.body()?.responseContents)
                Log.i("", "" + responseBody?.body()?.responseContents)

                setInstution(responseBody!!.body()?.responseContents as ArrayList<InstitutionresponseContent?>?)


            }

            override fun onBadRequest(response: Response<SurgeryInstitutionResponseModel?>) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!, "Bad Request")
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(context!!, getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(context!!, getString(R.string.unauthorized))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(context!!, getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                if (s != null) {
                    try {
//                        AnalyticsManager.getAnalyticsManager().trackLoginFailed(context!!, s)
                        utils?.showToast(
                            R.color.negativeToast,
                            binding?.mainLayout!!,
                            s
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun setInstution(arrayList: ArrayList<InstitutionresponseContent?>?) {

        institutionDropDownAdapter?.setInstitutionListDetails(arrayList)
        binding?.spinnerInstitution?.adapter = institutionDropDownAdapter

        if (facilitylevelID != 0) {

            for (i in arrayList!!.indices) {

                if (arrayList[i]?.facility_uuid == facilitylevelID) {

                    binding?.spinnerInstitution?.setSelection(i)

                    break

                }

            }
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
                    institution_NAME = institutionListGetDetails?.get(position)?.facility?.name


                    if (facilitylevelID != 0) {
                        viewModel?.getDepartmentList(facilitylevelID, departmentRetrofitCallBack)
                    }

                    return
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }

    val departmentRetrofitCallBack =
        object : RetrofitCallback<DepartmentResponseModel?> {
            override fun onSuccessfulResponse(response: Response<DepartmentResponseModel?>) {
                Log.i("", "" + response.body())

                setDepartment(response.body()?.responseContents as ArrayList<DepartmentResponseContent?>?)

            }

            override fun onBadRequest(response: Response<DepartmentResponseModel?>) {
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
                if(failure!=null)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun setDepartment(arrayList: ArrayList<DepartmentResponseContent?>?) {

        departmentDropDownAdapter?.setDepatmentListDetails(arrayList)
        binding?.spinnerDeparment?.adapter = departmentDropDownAdapter


        if (department_uuid != 0) {

            for (i in arrayList!!.indices) {

                if (arrayList[i]?.department_uuid == department_uuid) {

                    binding?.spinnerDeparment?.setSelection(i)

                    break

                }

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
                department_uuid =
                    departmentListGetDetails?.get(position)?.department_uuid


                departmentname = departmentListGetDetails?.get(position)?.department?.name


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
            }
        }
    }


}


