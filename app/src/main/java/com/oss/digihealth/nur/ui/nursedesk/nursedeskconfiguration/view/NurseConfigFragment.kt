package com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.view

import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.NurseActivityConfigBinding
import com.oss.digihealth.nur.ui.configuration.model.ConfigResponseContent
import com.oss.digihealth.nur.ui.configuration.model.ConfigResponseModel
import com.oss.digihealth.nur.ui.configuration.model.ConfigUpdateRequestModel
import com.oss.digihealth.nur.ui.configuration.model.ConfigUpdateResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.ResponseContent
import com.oss.digihealth.nur.ui.landingscreen.MainLandScreenActivity
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nurse_emr.view.NurseEmrWorkFlowActivity
import com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.model.NurseUpdateRequestModule
import com.oss.digihealth.nur.ui.nursedesk.viewmodel.NurseConfigViewModel
import com.oss.digihealth.nur.ui.nursedesk.viewmodel.NurseConfigViewModelFactory
import com.oss.digihealth.nur.utils.NurseItemMoveCallback
import com.oss.digihealth.nur.utils.StartDragListener
import com.oss.digihealth.nur.utils.custom_views.CustomProgressDialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import retrofit2.Response


class NurseConfigFragment : Fragment(), StartDragListener {
    private var utils: Utils? = null
    private var conficFinalData: ArrayList<ConfigResponseContent?>? = ArrayList()
    private var binding: NurseActivityConfigBinding?=null
    private var viewModel: NurseConfigViewModel? = null
    private var configadapter: NurseConfigRecyclerAdapter? = null
    private var configfavadapter: NurseConfigFavRecyclerAdapter? = null
    internal var touchHelper: ItemTouchHelper? = null
    private var customProgressDialog: CustomProgressDialog? = null

    var requestparameter: ConfigUpdateRequestModel? = ConfigUpdateRequestModel()
    private var configRequestData: ArrayList<NurseUpdateRequestModule?>? = ArrayList()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null
    var selecteConfriguration: Int? = null
    var configStatus:Boolean=false
    var createList:ArrayList<ResponseContent?> = ArrayList()

    val flow:String?
        get() = if (arguments == null) null else requireArguments().getSerializable(NurseConfigFragment.ARG_NAME) as String
    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.nurse_activity_config,
                container,
                false)
        viewModel = NurseConfigViewModelFactory(
            requireActivity().application
        ).create(NurseConfigViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val roleid = userDataStoreBean?.role_uuid
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val DepartmentID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        val facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        val warduuid = appPreferences?.getInt(AppConstants.WARDUUID)
        customProgressDialog = CustomProgressDialog(context)
        configadapter = NurseConfigRecyclerAdapter(context, ArrayList())
        binding!!.ConfigRecyclerView.adapter = configadapter
        val searchText =
            binding?.searchView?.findViewById(R.id.search_src_text) as TextView
        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        searchText.typeface = tf
        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }
            fun callSearch(query: String) {
                configadapter?.getFilter()?.filter(query)
            }
        })
        trackConfigurationAnalyticsVisit()
        configfavadapter = NurseConfigFavRecyclerAdapter(this, ArrayList())
        val callback = NurseItemMoveCallback(configfavadapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper!!.attachToRecyclerView(binding?.ConfigfavRecyclerView)
        binding!!.ConfigfavRecyclerView.adapter = configfavadapter
        binding!!.clearCardView.setOnClickListener {
            conficFinalData = configfavadapter?.getFinalData()
            if(conficFinalData?.size==0)
            {
                Toast.makeText(requireContext(),"All item moved already",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            conficFinalData = configfavadapter?.getFinalData()
            for (i in conficFinalData!!.indices) {
                configadapter?.setConfigfavList(conficFinalData!!.get(i))
            }
            configfavadapter?.clearALL()
            binding?.conficount?.setText(configadapter?.getItemSize().toString())
            binding?.confifavcount?.setText(configfavadapter?.getItemSize().toString())
        }
        binding!!.click.setOnClickListener {
            conficFinalData = configfavadapter?.getFinalData()
            if (conficFinalData?.size!! > 0) {
                for (i in conficFinalData?.indices!!) {
                    val configData: NurseUpdateRequestModule = NurseUpdateRequestModule()
                    configData.facility_uuid = facility_id
                    configData.department_uuid = DepartmentID?.toString()
                    configData.ward_master_uuid=warduuid
                    configData.role_uuid = roleid.toString()
                    configData.context_uuid = selecteConfriguration
                    configData.context_activity_map_uuid = conficFinalData?.get(i)!!.context_uuid
                    configData.activity_uuid = conficFinalData?.get(i)?.activity_uuid
                    configData.nurse_desk_order = conficFinalData?.get(i)?.order
                    configRequestData!!.add(configData)
                }
                viewModel?.postRequestParameter(
                    facility_id,
                    configRequestData!!,
                    configFinalRetrofitCallBack,
                    configStatus
                )

            } else {
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, "Please add any one item")
            }
        }
        binding?.moveall!!.setOnClickListener {
            conficFinalData = configadapter?.getConfigData()
            if(conficFinalData?.size==0)
            {
                Toast.makeText(requireContext(),"All item moved already",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val  conficfev= configfavadapter?.getFinalData()
            val confignormal =configadapter?.getConfigData()
            for(i in confignormal!!.indices)
            {
                val check= conficfev!!.any{ it!!.activity_uuid == confignormal.get(i)!!.activity_uuid}
                if (!check) {
                    configfavadapter?.setConfigfavList(confignormal.get(i)!!)

                }
            }
            configadapter?.clearALL()
            binding?.conficount?.setText(configadapter?.getItemSize().toString())
            binding?.confifavcount?.setText(configfavadapter?.getItemSize().toString())

        }
        configadapter!!.setOnItemClickListener(object :
            NurseConfigRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(
                configResponseContent: ConfigResponseContent,
                position: Int
            ) {

                conficFinalData = configfavadapter?.getFinalData()
                val check= conficFinalData!!.any{ it!!.activity_uuid == configResponseContent.activity_uuid}
                if (!check) {
                    configfavadapter?.setConfigfavList(configResponseContent)
                    binding?.confifavcount?.setText(configfavadapter?.getItemSize().toString())
                    configadapter?.removeitem(position)
                    binding?.conficount?.setText(configadapter?.getItemSize().toString())
                }
                else{
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Already Item is available"
                    )
                }

            }
        })
        configfavadapter!!.setOnItemClickListener(object :
            NurseConfigFavRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(
                configfavResponseContent: ConfigResponseContent?,
                position: Int
            ) {
                configadapter?.setConfigList(configfavResponseContent)
                configfavadapter?.removeitem(position)
                binding?.conficount?.setText(configadapter?.getItemSize().toString())
                binding?.confifavcount?.setText(configfavadapter?.getItemSize().toString())
            }})
        selecteConfriguration = 4
        viewModel?.getEmrWorkFlowFav(emrWorkFlowRetrofitCallBack,selecteConfriguration)

        return binding!!.root
    }
    val configRetrofitCallBack = object : RetrofitCallback<ConfigResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<ConfigResponseModel?>) {

            configadapter?.clearALL()

            createList

            var tabsArrayList = responseBody?.body()?.responseContents!!

            for(i in tabsArrayList!!.indices){

                if(tabsArrayList!![i]!!.activity!!.code== AppConstants?.ACTIVITY_CODE_CONFIG){

                    tabsArrayList!!.removeAt(i)

                    break

                }

            }

            for (j in createList!!.indices) {

                for (i in tabsArrayList!!.indices) {

                    if (tabsArrayList!![i]!!.activity!!.code == createList!![j]!!.activity_code) {

                        tabsArrayList!!.removeAt(i)

                        break

                    }

                }

            }


            configadapter?.setConfigItem(tabsArrayList!!)
            binding?.conficount?.setText(tabsArrayList!!.size.toString())


        }
        override fun onBadRequest(errorBody: Response<ConfigResponseModel?>) {
        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }
        override fun onForbidden() {

        }
        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val configFinalRetrofitCallBack = object : RetrofitCallback<ConfigUpdateResponseModel?> {
        override fun onSuccessfulResponse(responseBody: Response<ConfigUpdateResponseModel?>) {
            utils!!.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )

            val emr= NurseEmrWorkFlowActivity.newInstance(AppConstants.OUT_PATIENT)

            (activity as MainLandScreenActivity).replaceFragment(emr)
//            startActivity(Intent(context, DashBoardActivity::class.java))

        }

        override fun onBadRequest(errorBody: Response<ConfigUpdateResponseModel?>) {
        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
/*

    private val configCreateRetrofitCallBack = object : RetrofitCallback<ConfigUpdateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<ConfigUpdateResponseModel>?) {
            utils!!.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )


            val emr= DashBoardActivity()

            (activity as MainLandScreenActivity).replaceFragment(emr)
//            startActivity(Intent(context, DashBoardActivity::class.java))

        }

        override fun onBadRequest(errorBody: Response<ConfigUpdateResponseModel>?) {
        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
*/

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper?.startDrag(viewHolder)
    }

    private val emrWorkFlowRetrofitCallBack = object : RetrofitCallback<EmrWorkFlowResponseModel?> {
        override fun onSuccessfulResponse(response: Response<EmrWorkFlowResponseModel?>) {
            if (response.body()?.responseContents?.isNotEmpty()!!) {

                configfavadapter?.clearALL()

                createList = response.body()?.responseContents!!

                for(i in createList!!.indices){

                    if(createList!![i]!!.activity_code== AppConstants.ACTIVITY_CODE_CONFIG){

                        createList!!.removeAt(i)

                        break

                    }

                }


                configfavadapter?.setConfigfavarrayList(createList!!)
                binding?.confifavcount?.setText(configfavadapter?.getItemSize().toString())

                configStatus=false



            }
            else{
                configStatus=true

            }

            viewModel?.getConfigList(selecteConfriguration,configRetrofitCallBack)
        }

        override fun onBadRequest(response: Response<EmrWorkFlowResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
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

    fun setupViemodelProgress(){
        viewModel!!.progress.observe(viewLifecycleOwner,
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
    }


    companion object {
        const val ARG_NAME = "from"
        fun newInstance(from:String,Status:Boolean): NurseConfigFragment {
            val fragment = NurseConfigFragment()
            val bundle = Bundle().apply {
                putSerializable(ARG_NAME, from)
                putSerializable("Status",Status)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    fun trackConfigurationAnalyticsVisit(){
//        AnalyticsManager.getAnalyticsManager().trackConfigurationVisit()
    }
    fun trackConfigSaveAnalyticsVisit(type : String){
//        AnalyticsManager.getAnalyticsManager().trackOPConfigSave(type)
    }
}