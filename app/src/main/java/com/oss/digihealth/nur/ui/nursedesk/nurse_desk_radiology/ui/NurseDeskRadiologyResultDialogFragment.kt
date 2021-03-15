package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.ui
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.oss.digihealth.nur.databinding.DialogNurseDeskRadiologyPrevlabBinding
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model.NurseDeskRadiologyViewModelFactory
import com.oss.digihealth.nur.ui.emr_workflow.documents.model.AddDocumentDetailsResponseModel
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.model.NurseDeskRadiologyResulyResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_radiology.view_model.NurseDeskRadiologyViewModel
import okhttp3.ResponseBody
import retrofit2.Response


class NurseDeskRadiologyResultDialogFragment : DialogFragment() {
    private var content: String? = null
    var imgposition : Int?=null
    private var fileformat: String?=""
    private var viewModel: NurseDeskRadiologyViewModel? = null
    private var managePrevLabAdapter: NurseDeskRadiologyResultAdapter?=null
    var binding: DialogNurseDeskRadiologyPrevlabBinding? = null
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
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_nurse_desk_radiology_prevlab, container, false)
        viewModel = NurseDeskRadiologyViewModelFactory(
            requireActivity().application
        )
            .create(NurseDeskRadiologyViewModel::class.java)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        binding!!.closeImageView.setOnClickListener {
            dialog?.dismiss()
        }
        val args = arguments
        if (args != null) {
            val Patient_UUID = args.getInt("UUID")
            viewModel?.getNurseDeskResultRadiologyDetails(Patient_UUID,getresultInvestigationRetrofitCallBack)

        }
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding!!.labManagePrevLabRecyclerView.layoutManager = linearLayoutManager
        managePrevLabAdapter = NurseDeskRadiologyResultAdapter(
            requireActivity()
        )
        binding!!.labManagePrevLabRecyclerView.adapter = managePrevLabAdapter
        managePrevLabAdapter?.setOnListItemClickListener(object :
            NurseDeskRadiologyResultAdapter.OnListItemClickListener {
            override fun onListItemClick(filepath: String?, position: Int) {
                dowloadimage(filepath,position)

            }

        })
        return binding!!.root
    }

    private fun dowloadimage(filepath: String?, position: Int) {
        imgposition = position
        fileformat =
            filepath!!.substring(filepath.lastIndexOf(".") + 1) // Without dot jpg, png
        viewModel?.getImage(
            filepath,
            downloadimagecallback
        )
    }

    val downloadimagecallback =
        object : RetrofitCallback<ResponseBody?> {
            override fun onSuccessfulResponse(response: Response<ResponseBody?>) {
                val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())
                managePrevLabAdapter?.setImage(bmp,imgposition!!)

            }
            override fun onBadRequest(response: Response<ResponseBody?>) {
                val responseModel: AddDocumentDetailsResponseModel
                try {
                    responseModel = GsonBuilder().create().fromJson(
                        response.errorBody()!!.string(),
                        AddDocumentDetailsResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        response.message()
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
                )}
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

    val getresultInvestigationRetrofitCallBack =
        object : RetrofitCallback<NurseDeskRadiologyResulyResponseModel?> {
            override fun onSuccessfulResponse(response: Response<NurseDeskRadiologyResulyResponseModel?>) {
                if(response.body()?.responseContents?.isNotEmpty()!!)
                {
                    managePrevLabAdapter?.setData(response.body()?.responseContents!!)
                }
                else
                {
                    Toast.makeText(requireContext(),"No Record Found",Toast.LENGTH_LONG).show()
                }
            }
            override fun onBadRequest(response: Response<NurseDeskRadiologyResulyResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: NurseDeskRadiologyResulyResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        NurseDeskRadiologyResulyResponseModel::class.java
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding!!.mainLayout!!,
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
                    binding!!.mainLayout,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String?) {
                if(failure!==null)
                utils?.showToast(R.color.negativeToast, binding!!.mainLayout, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }



}