package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.DialogInvestigationNurseDeskBinding
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.view_model.NurseDeskInvestigationViewModelFactory
import com.oss.digihealth.nur.ui.emr_workflow.documents.model.AddDocumentDetailsResponseModel
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.model.NurseDeskInvestigationResultResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_investigation.view_model.NurseDeskInvestigationViewModel
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File


class NurseDeskInvestigationResultDialogFragment : DialogFragment() {
    private var fileformat: String?=""
    private var content: String? = null
    private var viewModel: NurseDeskInvestigationViewModel? = null
    private var managePrevINvestigationAdapter: NurseDeskInvestigationDialogAdapter? = null
    var binding: DialogInvestigationNurseDeskBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var facility_id: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var destinationFile: File? = null
    var imgposition : Int?=null
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
                R.layout.dialog_investigation_nurse_desk,
                container,
                false
            )
        viewModel = NurseDeskInvestigationViewModelFactory(
            requireActivity().application
        )
            .create(NurseDeskInvestigationViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)


        val args = arguments
        if (args != null) {
            val Patient_UUID = args.getInt("UUID")
            viewModel?.getNurseDeskREsultInvestigationDetails(
                Patient_UUID,
                getresultInvestigationRetrofitCallBack
            )

        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding?.investigationDialogRecyclerView!!.layoutManager = layoutManager
        managePrevINvestigationAdapter = NurseDeskInvestigationDialogAdapter(requireContext())
        binding?.investigationDialogRecyclerView!!.adapter = managePrevINvestigationAdapter
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()

        }
        managePrevINvestigationAdapter?.setOnListItemClickListener(object :
            NurseDeskInvestigationDialogAdapter.OnListItemClickListener {
            override fun onListItemClick(filepath: String?, position: Int) {
                dowloadimage(filepath,position)

            }

        })

        return binding!!.root
    }

    val getresultInvestigationRetrofitCallBack =
        object : RetrofitCallback<NurseDeskInvestigationResultResponseModel?> {
            override fun onSuccessfulResponse(response: Response<NurseDeskInvestigationResultResponseModel?>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {


                    managePrevINvestigationAdapter?.setData(response.body()?.responseContents!!)

                } else {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        response?.body()!!.message!!
                    )

                }

            }

            override fun onBadRequest(response: Response<NurseDeskInvestigationResultResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: NurseDeskInvestigationResultResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        NurseDeskInvestigationResultResponseModel::class.java
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
                managePrevINvestigationAdapter?.setImage(bmp,imgposition!!)

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

}