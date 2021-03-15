package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.ui.template

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ToolbarBindingAdapter
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils

import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogManageVitalTemplatesBinding
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.favresponse.ResponseContentsfav
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request.Detail
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request.Headers
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oss.digihealth.nur.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd

import com.oss.digihealth.nur.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.oss.digihealth.nur.ui.emr_workflow.model.templete.TempleResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request.NewDetail
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request.RemovedDetail
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request.VitalFavUpdateRequestModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSearchNameResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSearchNameresponseContent
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual.ResponseContentedittemplatevitual
import com.oss.digihealth.nur.ui.emr_workflow.vitals.ui.TestNameSearchResultAdapter

import com.oss.digihealth.nur.ui.emr_workflow.vitals.view_model.ManageVitalTemplateViewModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.view_model.ManageVitalTemplateViewModelFactory
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import retrofit2.Response

class NurseManageVitalTemplateFragment : DialogFragment() {
    private var selectVitualValue: String?=""
    private var selectVitualUuid: Int?=0
    private var customdialog: Dialog?=null
    //private var Str_auto_id: Int? = 0
    var arraylistresponse : ArrayList<ResponseContentsfav?> = ArrayList()
    private var content: String? = null
    private var Itemname: String? = ""
    private var viewModel: ManageVitalTemplateViewModel? = null
    var binding: DialogManageVitalTemplatesBinding? = null
    var appPreferences: AppPreferences? = null
    private val hashvitualSpinnerList: HashMap<Int,Int> = HashMap()
    private var utils: Utils? = null
    private var vitualSpinnerList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var mAdapter: NurseVitualManageTemplateAdapter? = null
    private var arrayItemData: ArrayList<ResponseContentsfav?>? =null
    val header: Headers? = Headers()
    val updateHeader: com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request.Headers = com.oss.digihealth.nur.ui.emr_workflow.vitals.model.request.Headers()
    val detailsList: ArrayList<Detail> = ArrayList()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var callbacktemplate: OnTemplateRefreshListener? = null
    var status : Boolean ?=false
    var RequestTemplateAddDetails : RequestTemplateAddDetails = RequestTemplateAddDetails()
    var newDetailList : ArrayList<NewDetail> = ArrayList()
    var UpdateRequestModule : VitalFavUpdateRequestModel = VitalFavUpdateRequestModel()
    val removeList:  ArrayList<RemovedDetail> = ArrayList()
    var rasponsecontentLabGetTemplateDetails : ResponseContentedittemplatevitual = ResponseContentedittemplatevitual()
    private var is_active: Boolean = true
    private var autocompleteTestResponse: List<VitalSearchNameresponseContent?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
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
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_manage_vital_templates,
                container,
                false
            )
        viewModel = ManageVitalTemplateViewModelFactory(
            requireActivity().application
        )
            .create(ManageVitalTemplateViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        /*int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
getDialog().getWindow().setLayout(width, height);*/

        val width = resources.getDimensionPixelSize(R.dimen._100sdp)
        val height = resources.getDimensionPixelSize(R.dimen._100sdp)
        dialog!!.window!!.setLayout(width,height)

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        facility_UUID = appPreferences?.getInt(AppConstants?.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants?.DEPARTMENT_UUID)

        mAdapter = NurseVitualManageTemplateAdapter(requireContext(), ArrayList())
        binding?.favManageRecyclerview!!.adapter = mAdapter

        binding?.radioGroup!!.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = requireView().findViewById(checkedId)
                when (checkedId) {
                    R.id.myDept -> {
                        radio.isEnabled = true
                    }
                    R.id.mySelf -> {
                        radio.isEnabled = true
                    }
                }

            })

        binding?.switchCheck!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            is_active = isChecked
        })


        val first = "Name"
        val vitals = "Vitals"
        val disOrder = "Display Order"
        val next = "<font color='#EE0000'>*</font>"

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.clear!!.setOnClickListener {
            binding?.autoCompleteTextViewTestName!!.setText("")
            selectVitualUuid = 0
        }
        /*
        Vitual Name
         */

        binding?.autoCompleteTextViewTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }


            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {

                    viewModel?.getVitalsSearch(s.toString(),facility_UUID, vitalsSearchRetrofitCallBack)

                }
            }
        })


        binding?.autoCompleteTextViewTestName!!.setOnItemClickListener { parent, _, position, id ->
            binding?.autoCompleteTextViewTestName?.setText(autocompleteTestResponse?.get(position)?.name)

            Log.i("", "" + autocompleteTestResponse!!.get(position)!!.name)

            selectVitualValue = autocompleteTestResponse?.get(position)?.name
            selectVitualUuid = autocompleteTestResponse?.get(position)?.uuid
            //Str_auto_id = autocompleteTestResponse?.get(position)?.uuid
        }

        binding?.addbutton?.setOnClickListener({
            Itemname = binding?.username?.text.toString()
            val displayordervalue = binding?.editdisplayorder?.text.toString()
            val autoCompletetext = binding?.autoCompleteTextViewTestName?.text.toString()
            val testmasterId = selectVitualUuid
            if (Itemname!!.isNotEmpty() && displayordervalue.isNotEmpty() && autoCompletetext.isNotEmpty()) {

                if(selectVitualUuid != 0) {
                    val existItemCheck = mAdapter?.getItems()
                    val check = existItemCheck!!.any { it!!.test_master_id == testmasterId }

                    if (!check) {
                        if (status as Boolean) {
                            val responseContentsfav = ResponseContentsfav()
                            responseContentsfav.vital_master_name = selectVitualValue
                            responseContentsfav.test_master_id = selectVitualUuid
                            responseContentsfav.favourite_display_order = displayordervalue.toInt()
                            arraylistresponse.add(responseContentsfav)
                            mAdapter?.setFavAddItem(arraylistresponse)
                            binding?.autoCompleteTextViewTestName?.setText("")
                        } else {

                            val newDetail: NewDetail = NewDetail()
                            newDetail.template_master_uuid =
                                mAdapter?.getItems()!![0]?.template_master_uuid
                            newDetail.test_master_uuid = 0
                            newDetail.chief_complaint_uuid = 0
                            newDetail.vital_master_uuid = testmasterId
                            newDetail.drug_id = 0
                            newDetail.drug_route_uuid = 0
                            newDetail.drug_frequency_uuid = 0
                            newDetail.drug_duration = 0
                            newDetail.drug_period_uuid = 0
                            newDetail.drug_instruction_uuid = 0
                            newDetail.display_order =
                                (binding?.editdisplayorder?.text?.toString())!!.toInt()
                            newDetail.quantity = 0
                            newDetail.revision = true
                            newDetail.is_active = true
                            newDetailList.add(newDetail)

                            val responseContentsfav = ResponseContentsfav()
                            responseContentsfav.vital_master_name = selectVitualValue
                            responseContentsfav.test_master_id = selectVitualUuid
                            responseContentsfav.template_master_uuid =
                                arrayItemData?.get(0)?.template_master_uuid
                            responseContentsfav.favourite_display_order = displayordervalue.toInt()
                            arraylistresponse.add(responseContentsfav)
                            mAdapter?.setFavAddItem(arraylistresponse)
                            binding?.autoCompleteTextViewTestName?.setText("")
                            selectVitualUuid = 0
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Already this vitual added Please select other vitual name",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    }
                }else{
                    Toast.makeText(
                        context,
                        "Please select valid vitual name",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }else{
                Toast.makeText(
                    context,
                    "Please enter all fields",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        mAdapter?.setOnDeleteClickListener(object : NurseVitualManageTemplateAdapter.OnDeleteClickListener {
            @SuppressLint("SetTextI18n")
            override fun onDeleteClick(
                responseData: ResponseContentsfav?,
                position: Int
            ) {
                Log.i("",""+responseData);
                customdialog = Dialog(requireContext())
                customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!! .setCancelable(false)
                customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView

                closeImageView.setOnClickListener {
                    customdialog?.dismiss()
                }
                val drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                drugNmae.text ="${drugNmae.text.toString()} '"+responseData?.vital_master_name+"' Record ?"
                val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {

                    Log.i("",""+responseData)
                    val removedDetail : RemovedDetail = RemovedDetail()
                    removedDetail.template_uuid = responseData?.template_master_uuid
                    removedDetail.template_details_uuid = responseData?.template_details_uuid
                    removeList.add(removedDetail)
                    mAdapter?.removeItem(position)
                    customdialog!!.dismiss()
                    selectVitualUuid = 0
                }
                noBtn.setOnClickListener {
                    customdialog!! .dismiss()
                }
                customdialog!! .show()
            }




        })

        val args = arguments
        if (args == null) {
            status = true
            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..
            rasponsecontentLabGetTemplateDetails = args.getParcelable(AppConstants.RESPONSECONTENT)!!
            Log.i("",""+rasponsecontentLabGetTemplateDetails)

            binding?.username?.setText(rasponsecontentLabGetTemplateDetails?.name)
            binding?.editdisplayorder?.setText(rasponsecontentLabGetTemplateDetails.display_order?.toString())
            arraylistresponse.clear()

            for(i in rasponsecontentLabGetTemplateDetails.template_master_details?.indices!!)
            {

                val responseContentsfav = ResponseContentsfav()
                responseContentsfav.vital_master_name = rasponsecontentLabGetTemplateDetails?.template_master_details?.get(i)?.vital_master?.name
                responseContentsfav.test_master_id =   rasponsecontentLabGetTemplateDetails?.template_master_details?.get(i)?.vital_master?.uuid
                responseContentsfav.template_master_uuid = rasponsecontentLabGetTemplateDetails?.uuid
                responseContentsfav.template_details_uuid = rasponsecontentLabGetTemplateDetails?.template_master_details?.get(i)?.uuid
                arraylistresponse.add(responseContentsfav)
            }
            mAdapter?.setFavAddItem(arraylistresponse)

            binding?.saveTxtView?.setText("Update")
        }

        binding?.save?.setOnClickListener({
            if(status as Boolean)
            {
                /*
                Add Details
                 */
                val displayordervalue = binding?.editdisplayorder?.text.toString()
                arrayItemData = mAdapter?.getItems()
                detailsList.clear()
                if (arrayItemData?.size!! > 0) {
                    for (i in arrayItemData?.indices!!) {
                        val details: Detail = Detail()
                        details.chief_complaint_uuid=0
                        details.vital_master_uuid=arrayItemData?.get(i)?.test_master_id
                        details.test_master_uuid=0
                        details.item_master_uuid = 0
                        details.drug_route_uuid=0
                        details.drug_frequency_uuid=0
                        details.duration=0
                        details.duration_period_uuid=0
                        details.drug_instruction_uuid=0
                        details.revision=true
                        details.is_active=true
                        detailsList.add(details)
                    }

                    header?.name = Itemname
                    header?.description = ""
                    header?.template_type_uuid = AppConstants.FAV_TYPE_ID_Vitual
                    header?.diagnosis_uuid =0
                    header?.is_public="false"
                    header?.facility_uuid = facility_UUID?.toString()
                    header?.department_uuid = deparment_UUID?.toString()
                    header?.display_order = binding?.editdisplayorder?.text?.toString()
                    header?.revision = true
                    header?.is_active = true

                    RequestTemplateAddDetails.headers = header!!
                    RequestTemplateAddDetails.details = this.detailsList

                    val request =  Gson().toJson(RequestTemplateAddDetails)

                    Log.i("",""+request)
                    viewModel?.labTemplateDetails(facility_UUID, RequestTemplateAddDetails!!, emrlabtemplatepostRetrofitCallback)

                }else{
                    Toast.makeText(activity,"Please add atleast one vital",Toast.LENGTH_LONG).show()
                }
            }

            else{
                val displayordervalue = binding?.editdisplayorder?.text.toString()
                arrayItemData = mAdapter?.getItems()
                if(arrayItemData?.size!! > 0)
                {

                    updateHeader.template_id = arrayItemData?.get(0)?.template_master_uuid
                    updateHeader.name = binding?.username?.text.toString()
                    updateHeader.display_order = displayordervalue.toInt()
                    updateHeader.is_public = "false"

                    UpdateRequestModule.headers = updateHeader
                    UpdateRequestModule.new_details = this.newDetailList
                    UpdateRequestModule.removed_details = this.removeList
                    val requestupdate =  Gson().toJson(UpdateRequestModule)
                    Log.e("VitalUpdateData",requestupdate.toString())
                    viewModel?.labUpdateTemplateDetails(facility_UUID, UpdateRequestModule, UpdateemrlabtemplatepostRetrofitCallback)
                }
            }
        })

        return binding!!.root
    }

    /*
    Vitual add
     */

    val vitalsSearchRetrofitCallBack =
        object : RetrofitCallback<VitalSearchNameResponseModel?> {
            override fun onSuccessfulResponse(response: Response<VitalSearchNameResponseModel?>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents!!.isNotEmpty())

                    autocompleteTestResponse = response.body()?.responseContents
                val responseContentAdapter = TestNameSearchResultAdapter(
                    context!!,
                    R.layout.row_chief_complaint_search_result,
                    response.body()?.responseContents!!
                )
                binding?.autoCompleteTextViewTestName?.threshold = 1
                binding?.autoCompleteTextViewTestName?.setAdapter(responseContentAdapter)

            }

            override fun onBadRequest(errorBody: Response<VitalSearchNameResponseModel?>) {

                val gson = GsonBuilder().create()
                val responseModel: VitalSearchNameResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody?.errorBody().toString(),
                        VitalSearchNameResponseModel::class.java
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

            override fun onFailure(s: String?) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, s.toString())
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    /*private fun setVitualSpinnerValue(responseContents: List<GetVital>) {

        vitualSpinnerList = responseContents.map { it.uuid to it.name!! }!!.toMap().toMutableMap()
        hashvitualSpinnerList.clear()

        for(i in responseContents.indices){

            hashvitualSpinnerList[responseContents[i].uuid] = i
        }
//        saveTemplateAdapter!!.setRote(vitualSpinnerList)
        val adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, vitualSpinnerList.values.toMutableList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.vitualSpinner!!.adapter = adapter

    }*/


    /*
    Add Details
     */

    val emrlabtemplatepostRetrofitCallback = object : RetrofitCallback<ReponseTemplateadd?> {
        override fun onSuccessfulResponse(responseBody: Response<ReponseTemplateadd?>) {
            Toast.makeText(activity,responseBody?.body()?.message,Toast.LENGTH_LONG).show()
            mAdapter?.cleardata()
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)

        }
        override fun onBadRequest(response: Response<ReponseTemplateadd?>) {

            Toast.makeText(activity,"Vitalname / Displayorder exists",Toast.LENGTH_LONG).show()
            val gson = GsonBuilder().create()
            val responseModel: ReponseTemplateadd
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    ReponseTemplateadd::class.java
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

    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel?> {
            override fun onSuccessfulResponse(response: Response<TempleResponseModel?>) {
                var responseContents = Gson().toJson(response.body()?.responseContents)

                callbacktemplate?.onRefreshList()

                if(dialog!!.isShowing) {
                    dialog?.dismiss()
                }

                /*     if (response.body()?.responseContents?.templates_lab_list?.isNotEmpty()!!) {
                         templeteAdapter.refreshList(response.body()?.responseContents?.templates_lab_list)
                     }*/
            }

            override fun onBadRequest(response: Response<TempleResponseModel?>) {
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


    val UpdateemrlabtemplatepostRetrofitCallback = object : RetrofitCallback<UpdateResponse?> {
        override fun onSuccessfulResponse(responseBody: Response<UpdateResponse?>) {

            Toast.makeText(activity,responseBody?.body()?.message,Toast.LENGTH_LONG).show()

            //Log.i("",""+responseBody?.body()?.responseContent)

            mAdapter?.cleardata()
            viewModel!!.getTemplete(getTempleteRetrofitCallBack)


        }
        override fun onBadRequest(response: Response<UpdateResponse?>) {
            val gson = GsonBuilder().create()
            val responseModel: UpdateResponse
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    UpdateResponse::class.java
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




    fun setOnTemplateRefreshListener(callback: OnTemplateRefreshListener) {
        this.callbacktemplate = callback
    }
    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnTemplateRefreshListener {
        fun onTemplateID(position: Int)
        fun onRefreshList()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}