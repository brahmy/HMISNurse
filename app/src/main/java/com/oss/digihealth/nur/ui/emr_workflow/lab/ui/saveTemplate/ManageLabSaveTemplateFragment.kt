package com.oss.digihealth.nur.ui.emr_workflow.lab.ui.saveTemplate

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.databinding.DataBindingUtil
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.config.AppConstants
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.*

import androidx.cardview.widget.CardView

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogManageLabSaveTemplateBinding
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.*
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.gettemplate.ResponseContentLabGetDetails
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request.Detail
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request.Headers
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.updaterequest.NewDetail
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.updaterequest.RemovedDetail
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.oss.digihealth.nur.ui.emr_workflow.lab.view_model.ManageLabTemplateViewModel
import com.oss.digihealth.nur.ui.emr_workflow.lab.view_model.ManageLabTemplateViewModelFactory
import com.oss.digihealth.nur.ui.emr_workflow.model.favorite.FavouritesModel
import com.oss.digihealth.nur.ui.emr_workflow.model.templete.LabDetail
import com.oss.digihealth.nur.ui.emr_workflow.model.templete.TempleResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.radiology.model.RecyclerDto

import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.doc.utils.Utils
import retrofit2.Response
import java.io.IOException
import java.util.HashMap


class ManageLabSaveTemplateFragment : DialogFragment() {

    private var favouriteData: ArrayList<FavouritesModel>? = ArrayList()
    private var Itemname: String? = ""
    private var Itemdescription : String?=""
    private var arrayItemData: ArrayList<ResponseContentsfav?>? =null
    private var Str_auto_id: Int? = 0
    private var Str_auto_name: String? = ""
    private var Str_auto_code: String? = ""
    private var deparment_UUID: Int? = 0
    private var autocompleteTestResponse: List<FavAddTestNameResponseContent>? = null
    private var content: String? = null
    private var viewModel: ManageLabTemplateViewModel? = null
    var binding: DialogManageLabSaveTemplateBinding? = null
    private var facility_UUID: Int? = 0
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var dropdownReferenceView: AutoCompleteTextView? = null
    var mCallback: LabChiefComplaintListener? =null
    val detailsList: ArrayList<Detail> = ArrayList()
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var mAdapter: LabManageSaveTemplateAdapter? = null
    private val favList: MutableList<RecyclerDto> = java.util.ArrayList()
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    val header: Headers? = Headers()
    var RequestTemplateAddDetails : RequestTemplateAddDetails= RequestTemplateAddDetails()
    var callbacktemplate: OnSaveTemplateRefreshListener? = null
    var status : Boolean ?=false
    var rasponsecontentLabGetTemplateDetails : ResponseContentLabGetDetails = ResponseContentLabGetDetails()
    var arraylistresponse : ArrayList<ResponseContentsfav?> = ArrayList()
    var UpdateRequestModule : UpdateRequestModule = UpdateRequestModule()

    val removeList:  ArrayList<RemovedDetail> = ArrayList()
    private var selectedDepartmentId: Int? =0
    private val departmentPositionId: HashMap<Int, Int> = HashMap()
    var ispublic="false"




    private var customdialog: Dialog?=null

    var newDetail : NewDetail = NewDetail()
    var newDetailList : ArrayList<NewDetail> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (dialog.window != null)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
            isCancelable = false
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_manage_lab_save_template, container, false)
        viewModel = ManageLabTemplateViewModelFactory(
            requireActivity().application
        )
            .create(ManageLabTemplateViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils= Utils(this.requireContext())


//        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(context)
//        binding?.labManageTemplateRecyclerView!!.layoutManager = layoutmanager
        mAdapter = LabManageSaveTemplateAdapter(requireContext(), ArrayList())
        binding?.labManageTemplateRecyclerView!!.adapter = mAdapter


        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_UUID = appPreferences?.getInt(AppConstants?.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants?.DEPARTMENT_UUID)

        binding?.UserName?.setText(userDataStoreBean?.user_name)

//        departmentDropDownAdapter = FavouriteDropDownAdapter(context!!,ArrayList())
        binding?.viewModel?.getDepartmentList(facility_UUID, FavdepartmentRetrofitCallBack)


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.spinnerdepartment?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            binding?.viewModel?.getAllDepartment(facility_UUID, favAddAllDepartmentCallBack)
            false
        })

        binding?.spinnerdepartment?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedDepartmentId =
                        favAddResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                }

            }
        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            binding?.UserName?.setText(userDataStoreBean?.user_name)
            arraylistresponse.clear()
            favouriteData = args.getParcelableArrayList<FavouritesModel>(AppConstants.RESPONSECONTENT)
            for(i in favouriteData!!.indices){
                val ResponseContantSave: ResponseContentsfav? = ResponseContentsfav()
                ResponseContantSave?.test_master_name = favouriteData!![i].test_master_name
                ResponseContantSave?.test_master_id = favouriteData!![i].test_master_id
                ResponseContantSave?.test_master_code = favouriteData!![i]?.test_master_code.toString()
                if (i != favouriteData?.size!!) {
                    arraylistresponse.add(ResponseContantSave)
                }
                else
                {

                }

            }
            mAdapter?.setFavAddItem(arraylistresponse)
        }
        mAdapter?.setOnDeleteClickListener(object : LabManageSaveTemplateAdapter.OnDeleteClickListener {
            override fun onDeleteClick(responseData : ResponseContentsfav?,position: Int) {

                customdialog = Dialog(requireContext())
                customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!! .setCancelable(false)
                customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                val drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                drugNmae.text ="${drugNmae.text.toString()} '"+responseData?.test_master_name+"' Record ?"
                val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {

                    mAdapter?.delete(position)

                    customdialog!!.dismiss()


                }
                noBtn.setOnClickListener {
                    customdialog!! .dismiss()
                }
                customdialog!! .show()


            }

        })


        binding?.clear?.setOnClickListener {

            binding?.editName?.setText("")

            binding?.editDescription?.setText("")

            binding?.displayorder?.setText("")

            binding?.autoCompleteTextViewTestName?.setText("")

        }

        binding?.cancelCardView?.setOnClickListener {

            dialog?.dismiss()
        }

        binding?.save?.setOnClickListener {
            val displayorder= binding?.displayorder?.text?.toString()
            val templeteName= binding?.editName?.text?.toString()
            if(templeteName?.isEmpty()!!){
                Toast.makeText(requireContext(),"Please Enter Templete name",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(displayorder?.isEmpty()!!)
            {
                Toast.makeText(requireContext(),"Please Enter Display Order",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val displayordervalue = binding?.displayorder?.text.toString()
            arrayItemData = mAdapter?.getItems()
            detailsList.clear()
            if (arrayItemData?.size!! > 0) {
                for (i in arrayItemData?.indices!!) {
                    val details: Detail = Detail()
                    details.chief_complaint_uuid = 0
                    details.vital_master_uuid = 0
                    details.test_master_uuid = arrayItemData?.get(i)?.test_master_id
                    details.item_master_uuid = 0
                    details.drug_route_uuid = 0
                    details.drug_frequency_uuid = 0
                    details.duration = 0
                    details.duration_period_uuid = 0
                    details.drug_instruction_uuid = 0
                    details.revision = true
                    details.is_active = true
                    detailsList.add(details)
                }
                if(binding!!.myDepartment!!.isChecked){

                    ispublic="true"
                }else{
                    ispublic = "false"
                }

                header?.name = binding?.editName?.text.toString()
                header?.description =  binding?.editDescription?.text.toString()
                header?.template_type_uuid = AppConstants.FAV_TYPE_ID_LAB
                header?.diagnosis_uuid = 0
                header?.is_public = ispublic
                header?.facility_uuid = facility_UUID?.toString()
                header?.department_uuid = selectedDepartmentId.toString()
                header?.display_order = binding?.displayorder?.text?.toString()
                header?.revision = true
                header?.is_active = binding?.switchId?.isChecked

                RequestTemplateAddDetails.headers = header!!
                RequestTemplateAddDetails.details = this.detailsList

                val request = Gson().toJson(RequestTemplateAddDetails)

                viewModel?.labTemplateDetails(
                    facility_UUID,
                    RequestTemplateAddDetails!!,
                    emrlabtemplatepostRetrofitCallback
                )
            }

        }

        binding?.addDetails?.setOnClickListener {
            val displayorder= binding?.displayorder?.text?.toString()
            val templeteName= binding?.editName?.text?.toString()
            if(templeteName?.isEmpty()!!){
                Toast.makeText(requireContext(),"Please Enter Templete name",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(displayorder?.isEmpty()!!)
            {
                Toast.makeText(requireContext(),"Please Enter Display Order",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Itemname = binding?.editName?.text.toString()
            Itemdescription = binding?.editDescription?.text.toString()


            if(!binding?.editName?.text.toString().isNullOrEmpty()) {


                if(!binding?.displayorder?.text.toString().isNullOrEmpty()) {


                    if(Str_auto_id!=0) {

                        val testmasterId = Str_auto_id

                        val responseContentsfav = ResponseContentsfav()

                        responseContentsfav.test_master_name = Str_auto_name
                        responseContentsfav.test_master_id = testmasterId
                        responseContentsfav.test_master_code = Str_auto_code

                        binding?.autoCompleteTextViewTestName?.setText("")

                        mAdapter?.setAddItem(responseContentsfav)

                        Str_auto_id=0
                    }

                    else{
                        binding?.autoCompleteTextViewTestName?.error="Test Name must be valid"

                    }
                }

                else{
                    binding?.displayorder?.error="Display order can't be empty"

                }
            }
            else{

                binding?.editName?.error="Template name must be valid"


            }


        }


        binding?.autoCompleteTextViewTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }


            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {

                    viewModel?.getTestName(s.toString(), favAddTestNameCallBack)

                }
            }
        })

        binding?.autoCompleteTextViewTestName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)


            Str_auto_code = autocompleteTestResponse?.get(position)?.code
            Str_auto_name = autocompleteTestResponse?.get(position)?.name

            Str_auto_id = autocompleteTestResponse?.get(position)?.uuid

        }


        return binding?.root
    }

    val emrlabtemplatepostRetrofitCallback = object : RetrofitCallback<ReponseTemplateadd> {
        override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd>?) {

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )

            Toast.makeText(context,responseBody?.body()?.message,Toast.LENGTH_SHORT).show()

            mAdapter?.cleardata()

            dialog!!.dismiss()

            mCallback?.sendDataChiefComplaint()


            //    viewModel!!.getTemplete(getTempleteRetrofitCallBack)
        }


        override fun onBadRequest(response: Response<ReponseTemplateadd>) {
            val gson = GsonBuilder().create()
            val responseModel: PrescriptionDurationResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrescriptionDurationResponseModel::class.java
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

/*
Update Response
 */


    /* val UpdateemrlabtemplatepostRetrofitCallback = object : RetrofitCallback<UpdateResponse> {
         override fun onSuccessfulResponse(responseBody: Response<UpdateResponse>?) {
             utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 responseBody?.body()?.message!!
             )


             mAdapter?.cleardata()



         }
         override fun onBadRequest(response: Response<UpdateResponse>) {
             val gson = GsonBuilder().create()
             val responseModel: PrescriptionDurationResponseModel
             try {
                 responseModel = gson.fromJson(
                     response.errorBody()!!.string(),
                     PrescriptionDurationResponseModel::class.java
                 )
                 utils?.showToast(
                     R.color.negativeToast,
                     binding?.mainLayout!!,
                     getString(R.string.something_went_wrong)
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
    val FavdepartmentRetrofitCallBack =
        object : RetrofitCallback<FavAddResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {

                viewModel?.getTestName(facility_UUID.toString(), favAddTestNameCallBack)
                listDepartmentItems.add(response.body()?.responseContent)
                favAddResponseMap =
                    listDepartmentItems.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.spinnerdepartment!!.adapter = adapter

            }

            override fun onBadRequest(response: Response<FavAddResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavAddResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavAddResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        ""
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

    val favAddAllDepartmentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            listAllAddDepartmentItems = responseBody?.body()?.responseContents!!
            favAddResponseMap =
                listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
            for (i in listAllAddDepartmentItems?.indices!!) {
                departmentPositionId[listAllAddDepartmentItems?.get(i)!!.uuid!!] = i
            }

            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.spinner_item,
                    favAddResponseMap.values.toMutableList()
                )

            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding?.spinnerdepartment!!.adapter = adapter
            binding?.spinnerdepartment?.setSelection(departmentPositionId.get(selectedDepartmentId)!!)

        }

        override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddAllDepatResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddAllDepatResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    val favAddTestNameCallBack = object : RetrofitCallback<FavAddTestNameResponse> {
        override fun onSuccessfulResponse(responseBody: Response<FavAddTestNameResponse>?) {

            autocompleteTestResponse = responseBody?.body()?.responseContents
            val responseContentAdapter = FavTestNameSearchResultAdapter(
                context!!,
                R.layout.row_chief_complaint_search_result,
                responseBody?.body()?.responseContents!!
            )
            binding?.autoCompleteTextViewTestName?.threshold = 1
            binding?.autoCompleteTextViewTestName?.setAdapter(responseContentAdapter)

        }

        override fun onBadRequest(errorBody: Response<FavAddTestNameResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddTestNameResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddTestNameResponse::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
/*    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)




            }

            override fun onBadRequest(response: Response<TempleResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TempleResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TempleResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.bad)
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
        }*/

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext()!!, theme) {
            override fun onBackPressed() {
            }
        }
    }

    fun setOnTemplateRefreshListener(callback: OnSaveTemplateRefreshListener) {
        this.callbacktemplate = callback
    }
    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnSaveTemplateRefreshListener {
        fun onTemplateID(position: Int)
        fun onTemplateRefreshList()
    }
    //defining Interface
    fun setOnClickedListener(callback: LabChiefComplaintListener) {
        this.mCallback = callback
    }
    interface LabChiefComplaintListener {
        fun sendDataChiefComplaint()
    }

}


