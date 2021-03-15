package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.ui.template
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.GsonBuilder
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.FragmentNurseVitalTempleteBinding
import com.oss.digihealth.nur.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.lab.ui.LabAdapter
import com.oss.digihealth.nur.ui.emr_workflow.lab.ui.saveTemplate.ManageLabSaveTemplateFragment
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.TemplateDetail
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.VitalsTemplateResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.responseedittemplatevitual.ResponseEditTemplate
import com.oss.digihealth.nur.ui.emr_workflow.vitals.ui.ManageVitalTemplateFragment
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.viewmodel.NextVitalsViewModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.viewmodel.NextVitalsViewModelFactory

import com.oss.digihealth.doc.utils.Utils
import retrofit2.Response

class NurseVitalsTempleteFragment : Fragment() , NurseManageVitalTemplateFragment.OnTemplateRefreshListener,ManageLabSaveTemplateFragment.OnSaveTemplateRefreshListener{
    private var customdialog: Dialog?=null
    @SuppressLint("ClickableViewAccessibility")
    var binding : FragmentNurseVitalTempleteBinding ?=null
    private var viewModel: NextVitalsViewModel? = null
    lateinit var vitalsTemplatesAdapter: NurseVitalsTemplateAdapter
    var mCallback: TempleteClickedListener? =null
    private var utils: Utils? = null
    var labadapter : LabAdapter?=null
    var appPreferences: AppPreferences? = null
    private var department_uuid: Int? = null
    private var facility_UUID: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_nurse_vital_templete,
                container,
                false
            )
        viewModel = NextVitalsViewModelFactory(
            requireActivity().application
        ).create(NextVitalsViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this

        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        utils = Utils(requireContext())
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
                vitalsTemplatesAdapter.getFilter().filter(query)
            }
        })
        binding?.manageTemplatesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val manageVitalTempFragment = NurseManageVitalTemplateFragment()
            manageVitalTempFragment.show(ft, "Tag")
        }
        initTempleteAdapter()
        //viewModel!!.getTemplete(getTempleteRetrofitCallBack)
        viewModel!!.getvitalsTemplate(
            facility_UUID,
            department_uuid,
            vitalsTemplateRetrofitCallBack
        )


        return binding!!.root
    }

    private fun initTempleteAdapter() {
        vitalsTemplatesAdapter =
            NurseVitalsTemplateAdapter(requireContext())

        val tabletSize = getResources().getBoolean(R.bool.isTablet)
        var gridLayoutManager:GridLayoutManager? = null
        if(tabletSize) {
            gridLayoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }else{
            gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }

        binding?.vitalsTemplaterecyclerView?.layoutManager = gridLayoutManager
        binding?.vitalsTemplaterecyclerView?.adapter = vitalsTemplatesAdapter

        vitalsTemplatesAdapter.setOnItemViewClickListener(object :
            NurseVitalsTemplateAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: TemplateDetail?) {
                Log.i("",""+responseContent)
                viewModel?.getTemplateDetails(responseContent?.uuid,facility_UUID,department_uuid,getTemplateRetrofitResponseCallback)
            }
        })
        vitalsTemplatesAdapter.setOnItemDeleteClickListener(object :
            NurseVitalsTemplateAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: TemplateDetail?
            ) {
                Log.i("",""+responseContent)
                customdialog = Dialog(requireContext())
                customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!! .setCancelable(false)
                customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView
                closeImageView.setOnClickListener {
                    customdialog!!.dismiss()
                }
                val drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView
                drugNmae.text ="${drugNmae.text.toString()} '"+responseContent?.name+"' Record ?"

                val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {

                    viewModel!!.deleteTemplate(
                        facility_UUID,
                        responseContent?.uuid!!,
                        deleteRetrofitResponseCallback
                    )
                }
                noBtn.setOnClickListener {
                    customdialog!! .dismiss()
                }
                customdialog!! .show()
            }
        })
        vitalsTemplatesAdapter.setOnItemClickListener(object :
            NurseVitalsTemplateAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: ArrayList<TemplateDetail>,
                position: Int,
                selected: Boolean,
                templatesId : Int
            ) {
                vitalsTemplatesAdapter.updateSelectStatus(position, selected)
                mCallback?.sendTemplete(responseContent,position,selected,templatesId)
            }
        })


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
                vitalsTemplatesAdapter.getFilter().filter(query)
            }

        })

    }
    val vitalsTemplateRetrofitCallBack =
        object : RetrofitCallback<VitalsTemplateResponseModel> {

            override fun onSuccessfulResponse(response: Response<VitalsTemplateResponseModel>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                vitalsTemplatesAdapter.refreshList(response.body()?.responseContents?.templateDetails)

                Log.e("TempList",response.body()?.responseContents.toString())

            }
            override fun onBadRequest(response: Response<VitalsTemplateResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: VitalsTemplateResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        VitalsTemplateResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!, ""
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
    //defining Interface
    interface TempleteClickedListener {
        fun sendTemplete(
            templeteDetails: List<TemplateDetail?>?,
            position: Int,
            selected: Boolean,
            id:Int
        )
    }

    fun setOnTextClickedListener(callback: TempleteClickedListener) {
        this.mCallback = callback
    }


    var deleteRetrofitResponseCallback = object : RetrofitCallback<DeleteResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteResponseModel>?) {
            viewModel!!.getvitalsTemplate(
                facility_UUID,
                department_uuid,
                vitalsTemplateRetrofitCallBack
            )
            Toast.makeText(requireContext(),responseBody?.body()?.message.toString(),Toast.LENGTH_LONG).show()
            customdialog!! .dismiss()

            Log.e("DeleteResponse", responseBody?.body().toString())
        }

        override fun onBadRequest(errorBody: Response<DeleteResponseModel>?) {

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

        }

    }

    override fun onTemplateID(position: Int) {

    }

    override fun onTemplateRefreshList() {

        viewModel!!.getvitalsTemplate(
            facility_UUID,
            department_uuid,
            vitalsTemplateRetrofitCallBack
        )
    }


    override fun onRefreshList() {
        viewModel!!.getvitalsTemplate(
            facility_UUID,
            department_uuid,
            vitalsTemplateRetrofitCallBack
        )
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is NurseManageVitalTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
        if (childFragment is ManageLabSaveTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }
    }

    /*
      Get Template
     */
    var getTemplateRetrofitResponseCallback = object : RetrofitCallback<ResponseEditTemplate> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseEditTemplate>?) {

            if(!responseBody?.body()?.responseContent.toString().isNullOrEmpty()) {
                Log.e("RespData", responseBody?.body()?.responseContent.toString())

                val ft = childFragmentManager.beginTransaction()
                val labtemplatedialog = NurseManageVitalTemplateFragment()
                val bundle = Bundle()
                bundle.putParcelable(
                    AppConstants.RESPONSECONTENT,
                    responseBody?.body()?.responseContent?.get(0)
                )
                labtemplatedialog.setArguments(bundle)
                labtemplatedialog.show(ft, "Tag")
            }

        }
        override fun onBadRequest(errorBody: Response<ResponseEditTemplate>?) {

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

        }

    }



}


