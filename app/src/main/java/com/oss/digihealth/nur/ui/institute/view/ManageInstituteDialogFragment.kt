package com.oss.digihealth.nur.ui.institute.view


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogManageinstituteListBinding
import com.oss.digihealth.nur.ui.emr_workflow.model.FavouritesResponseModel
import com.oss.digihealth.nur.ui.institute.model.DepartmentResponseContent
import com.oss.digihealth.nur.ui.institute.model.DepartmentResponseModel
import com.oss.digihealth.nur.ui.institute.model.InstitutionResponseContent
import com.oss.digihealth.nur.ui.institute.model.InstitutionResponseModel
import com.oss.digihealth.nur.ui.institute.view_model.ManageInstituteViewModel
import com.oss.digihealth.nur.ui.institute.view_model.ManageInstituteViewModelFactory
import kotlinx.android.synthetic.main.land_layout.*
import retrofit2.Response


class ManageInstituteDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var office_UUID: Int? = null
    private var institution_NAME: String? = null
    private var content: String? = null
    private var viewModel: ManageInstituteViewModel? = null
    var binding: DialogManageinstituteListBinding? = null
    private var utils: Utils? = null
    private var institutionDropDownAdapter: InstitutionDropDownAdapter? = null
    private var departmentDropDownAdapter: DepartmentDropDownAdapter? = null
    private var arraylist_institution: ArrayList<InstitutionResponseContent?> = ArrayList()
    private var arraylist_department: ArrayList<DepartmentResponseContent?> = ArrayList()
    var appPreferences: AppPreferences? = null

    private val hashInstitutionSpinnerList: HashMap<Int,Int> = HashMap()
    private val hashDepartmentSpinnerList: HashMap<Int,Int> = HashMap()

    var dialogListener: DialogListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_manageinstitute_list, container, false)
        viewModel = ManageInstituteViewModelFactory(
            requireActivity().application
        )
            .create(ManageInstituteViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)

        office_UUID = appPreferences?.getInt(AppConstants.OFFICE_UUID)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        viewModel?.getInstitutionList(office_UUID,instituteRetrofitCallBack)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.clear?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.save?.setOnClickListener {
            if (office_UUID != 0 && department_uuid != 0 && facilitylevelID != 0) {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    getString(R.string.data_save)
                )

                appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)

                appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, department_uuid!!)

                appPreferences?.saveString(AppConstants.INSTITUTION_NAME, institution_NAME!!)

                //  startActivity(Intent(context, DashBoardActivity::class.java))


                dialogListener = activity as DialogListener?

                dialogListener!!.onInstitutionCallback()

                dialog!!.dismiss()

            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.empty_item)
                )
            }
        }

        institutionDropDownAdapter = InstitutionDropDownAdapter(context!!, ArrayList())

        departmentDropDownAdapter = DepartmentDropDownAdapter(context!!, ArrayList())

        binding?.spinnerInstitution?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != institutionDropDownAdapter?.count!!) {
                    val institutionListGetDetails = institutionDropDownAdapter?.getlistDetails()

                    facilitylevelID = institutionListGetDetails?.get(position)?.uuid
                    institution_NAME = institutionListGetDetails?.get(position)?.name

                    if (facilitylevelID != 0) {
                        viewModel?.getDepartmentList(facilitylevelID,departmentRetrofitCallBack)
                    }
                    return
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                if(institutionDropDownAdapter?.count!! !=0) {
                    val institutionListGetDetails = institutionDropDownAdapter?.getlistDetails()

                    facilitylevelID = institutionListGetDetails?.get(0)?.uuid
                    institution_NAME = institutionListGetDetails?.get(0)?.name

                    //appPreferences?.saveString(AppConstants.INSTITUTION_NAME, institution_NAME!!)

                    if (facilitylevelID != 0) {

                        viewModel?.getDepartmentList(facilitylevelID,departmentRetrofitCallBack)

                    }

                    return
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
                department_uuid = departmentListGetDetails?.get(position)?.department_uuid

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                val departmentListGetDetails = departmentDropDownAdapter?.getlistDetails()
                department_uuid = departmentListGetDetails?.get(0)?.department_uuid

            }

        }

        return binding?.root
    }


    val instituteRetrofitCallBack =
        object : RetrofitCallback<InstitutionResponseModel?> {
            override fun onSuccessfulResponse(response: Response<InstitutionResponseModel?>) {
                //Log.i("Instection",""+response.body()?.responseContents)
                hashInstitutionSpinnerList.clear()

                var isprocess:Boolean=false

                for(i in response.body()?.responseContents!!.indices){

                    hashInstitutionSpinnerList[response.body()?.responseContents!![i]!!.uuid!!]=i

                    if((response.body()?.responseContents!![i]!!.uuid!!)==appPreferences!!.getInt(AppConstants.FACILITY_UUID)){

                        isprocess=true

                    }

                }
                institutionDropDownAdapter?.setInstitutionListDetails(response.body()?.responseContents as ArrayList<InstitutionResponseContent?>?)
                binding?.spinnerInstitution?.adapter = institutionDropDownAdapter

                Log.i("responseData",""+response.body()?.responseContents)
                Log.i("hashmap",""+hashInstitutionSpinnerList)
                Log.i("FactryID",""+facilitylevelID +""+hashInstitutionSpinnerList[appPreferences?.getInt(AppConstants.FACILITY_UUID)])

                if (isprocess) {

                    binding?.spinnerInstitution!!.setSelection(hashInstitutionSpinnerList[appPreferences?.getInt(AppConstants.FACILITY_UUID)]!!)

                }
                else{
                    binding?.spinnerInstitution!!.setSelection(0)

                }

            }

            override fun onBadRequest(response: Response<InstitutionResponseModel?>) {
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

    val departmentRetrofitCallBack =
        object : RetrofitCallback<DepartmentResponseModel?> {
            override fun onSuccessfulResponse(response: Response<DepartmentResponseModel?>) {
                //Log.i("", "" + response.body());
                hashDepartmentSpinnerList.clear()

                var isProcess:Boolean=false

                for(i in response.body()?.responseContents!!.indices){

                    hashDepartmentSpinnerList[response.body()?.responseContents!![i]!!.department_uuid!!]=i

                    if((response.body()?.responseContents!![i]!!.department_uuid!!)==appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)){

                        isProcess=true

                    }

                }

                Log.e("DepData",hashDepartmentSpinnerList.toString())
                departmentDropDownAdapter?.setDepatmentListDetails(response.body()?.responseContents as ArrayList<DepartmentResponseContent?>?)
                binding?.spinnerDeparment?.adapter = departmentDropDownAdapter

                if(isProcess) {

                    binding?.spinnerDeparment!!.setSelection(hashDepartmentSpinnerList.get(appPreferences?.getInt(AppConstants.DEPARTMENT_UUID))!!)

                }
                else{

                    binding?.spinnerDeparment!!.setSelection(0)

                }
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
            }
        }
    }


    interface DialogListener {
        fun onInstitutionCallback()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { /* this line is main difference for fragment to fragment communication & fragment to activity communication
            fragment to fragment: onInputListener = (OnInputListener) getTargetFragment();
            fragment to activity: onInputListener = (OnInputListener) getActivity();
             */
            dialogListener = targetFragment as DialogListener?
        } catch (e: ClassCastException) {
        }
    }

}


