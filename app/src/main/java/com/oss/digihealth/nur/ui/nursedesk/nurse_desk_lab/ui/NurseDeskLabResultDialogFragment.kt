package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogNurseDeskLabPrevlabBinding
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model.NurseDeskLabViewModelFactory
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.NurseDeskLabResultResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.model.request.requestlabresult
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_lab.view_model.NurseDeskLabViewModel
import retrofit2.Response


class NurseDeskLabResultDialogFragment : DialogFragment() {
    private var content: String? = null
    private var viewModel: NurseDeskLabViewModel? = null
    private var managePrevLabAdapter: NurseDeskLabResultAdapter?=null
    var binding: DialogNurseDeskLabPrevlabBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_UUID: Int? = 0
    private var deparment_UUID: Int? = 0
    private var facility_id: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_nurse_desk_lab_prevlab, container, false)
        viewModel = NurseDeskLabViewModelFactory(
            requireActivity().application
        )
            .create(NurseDeskLabViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        val args = arguments
        if (args != null) {
            val Patient_UUID = args.getInt("UUID")
            val requestlabresult : requestlabresult = requestlabresult()
            requestlabresult.Id = listOf(Patient_UUID)
            viewModel?.getNurseDeskREsultInvestigationDetails(requestlabresult,getresultInvestigationRetrofitCallBack)

        }
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.labManagePrevLabRecyclerView!!.layoutManager = linearLayoutManager
        managePrevLabAdapter = NurseDeskLabResultAdapter(
            requireActivity()
        )
        binding?.labManagePrevLabRecyclerView!!.adapter = managePrevLabAdapter
        binding?.cancelcardview?.setOnClickListener({
            dialog?.dismiss()
        })

        return binding!!.root
    }

    val getresultInvestigationRetrofitCallBack =
        object : RetrofitCallback<NurseDeskLabResultResponseModel?> {
            override fun onSuccessfulResponse(response: Response<NurseDeskLabResultResponseModel?>) {
                if(response?.body()?.responseContents?.isNotEmpty()!!)
                {
                    managePrevLabAdapter?.setData(response.body()?.responseContents!!)
                }
                else
                {
                    Toast.makeText(requireContext(),"No Record Found", Toast.LENGTH_LONG).show()
                }


            }
            override fun onBadRequest(response: Response<NurseDeskLabResultResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: NurseDeskLabResultResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        NurseDeskLabResultResponseModel::class.java
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



}