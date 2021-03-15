package com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.ui

import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.FragmentNextvitalsChildBinding
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.ui.NurseDeskVitalsFragment
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.TemplateDetail
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.VitalSaveRequestModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.VitalsTemplateResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSaveResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.vitals.model.response.VitalSearchListResponseModel
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.MainVItalsListResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.uom.newuom.UOMNewReferernceResponseModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.viewmodel.NextVitalsViewModel
import com.oss.digihealth.nur.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.viewmodel.NextVitalsViewModelFactory
import com.oss.digihealth.nur.utils.custom_views.CustomProgressDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NextVitalChildFragment : Fragment(), NurseVitalsTempleteFragment.TempleteClickedListener,
    PrevNurseVitalsFragment.NurseVitalPrevClickedListener {
    private var customdialog: Dialog? = null
    private var facility_id: Int = 0
    private var deparment_UUID: Int? = 0
    private var doctor_UUID: Int? = 0
    private var encounter_id: Int? = 0
    private var encounter_type: Int? = 0
    private var storemaster_Id: Int? = 0
    private var Str_auto_id: Int? = 0

    private var customProgressDialog: CustomProgressDialog? = null
    var vitalsAdapter: NextVitalsAdapter? = null
    var binding: FragmentNextvitalsChildBinding? = null
    var searchposition: Int = 0
    private var viewModel: NextVitalsViewModel? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    private var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    var gson: Gson? = Gson()
    private var searchPosition: Int? = 0
    private var responseContents: String? = ""
    private var patientId: Int? = 0
    private var patientInfo: String? = ""

    var getallVitals: List<TemplateDetail?>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_nextvitals_child,
            container,
            false
        )

        viewModel = NextVitalsViewModelFactory(
            requireActivity().application
        ).create(NextVitalsViewModel::class.java)


        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        var userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        doctor_UUID = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)!!
        //encounter_id = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)!!
        //encounter_type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        storemaster_Id = appPreferences?.getInt(AppConstants.STOREMASTER_UUID)!!
        utils = Utils(requireContext())
        setADapter()
        binding?.viewpager!!.setOffscreenPageLimit(2)
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)



        customProgressDialog = CustomProgressDialog(requireContext())
        binding?.favouriteDrawerCardView!!.setOnClickListener {

            binding?.drawerLayout!!.openDrawer(GravityCompat.END)

        }
        binding?.drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //binding?.drawerLayout?.drawerElevation = 0f

        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )


        val bundle = arguments
        val titleName = bundle?.getString("tName")
        val name = bundle?.getString("fName")
        val age = bundle?.getString("age")
        val gender = bundle?.getString("gender")
        val pin = bundle?.getString("pin")
        val mobile = bundle?.getString("mobile")
        patientInfo = bundle?.getString("patientInfo")
        patientId = bundle?.getString("patientId")?.toInt()
        encounter_id = bundle?.getString("admission_encounter_uuid")?.toInt()
        encounter_type = bundle?.getString("admission_encounter_type_uuid")?.toInt()


        /*if(titleName!=null && titleName!="null") {

            binding?.detailTextView?.setText(titleName + name + " / " + age + "Y / " + gender + " / " + pin + " / " + mobile)
        }
        else {
            binding?.detailTextView?.setText(name + " / " + age + "Y / " + gender + " / " + pin + " / " + mobile)

        }*/

        if (!patientInfo.isNullOrEmpty()) {
            binding?.detailTextView?.text = "$patientInfo $pin / $mobile"
        } else {
            if (titleName.isNullOrEmpty()) {
                if (age == "1") {
                    val patientInfo = "$name / $age Year / $gender / $pin / $mobile"
                    binding?.detailTextView?.text = patientInfo
                } else {
                    val patientInfo = "$name / $age Year(s) / $gender / $pin / $mobile"
                    binding?.detailTextView?.text = patientInfo

                }
            } else {
                if (age == "1") {
                    val patientInfo = "$titleName  $name / $age Year / $gender / $pin / $mobile"
                    binding?.detailTextView?.text = patientInfo
                } else {
                    val patientInfo = "$titleName  $name / $age Year(s) / $gender / $pin / $mobile"
                    binding?.detailTextView?.text = patientInfo
                }
            }
        }

        if (gender == "Male") {

            binding?.genderIcon?.setImageResource(R.drawable.ic_man_placeholder)
        } else {

            binding?.genderIcon?.setImageResource(R.drawable.ic_female_palceholder)

        }
        setupViewPager(binding?.viewpager!!)

        setData()


        binding?.saveCardView?.setOnClickListener {
            saveVitals()
        }

        binding?.backCardView?.setOnClickListener {
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.drawerLayout, NurseDeskVitalsFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding?.clearCardView?.setOnClickListener {

            binding?.mainLayout?.visibility = View.GONE

            //   vitalsAdapter?.clearAllData(getallVitals)

            vitalsAdapter!!.clearAll()

            setData()

        }


/*        viewModel!!.progress.observe(requireActivity(), Observer { progress ->

            if (progress == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (progress == View.GONE) {
                customProgressDialog!!.dismiss()

            }

        })*/
        return binding!!.root
    }

    private fun setData() {
        customProgressDialogShow(true)
        viewModel!!.getVitalsName(facility_id, getVitalsListRetrofitCallback)

    }

    private fun customProgressDialogShow(b: Boolean) {

        if (b) {
            customProgressDialog?.show()
        } else {
            customProgressDialog?.dismiss()
        }

    }

    private fun setADapter() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.savevitalsRecyclerView!!.layoutManager = linearLayoutManager
        vitalsAdapter = NextVitalsAdapter(requireActivity(), ArrayList()) {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
        binding?.savevitalsRecyclerView?.adapter = vitalsAdapter

        //Delete
        vitalsAdapter!!.setOnDeleteClickListener(object :
            NextVitalsAdapter.OnDeleteClickListener {
            override fun onDeleteClick(templateDetail: TemplateDetail, position: Int) {

                customdialog = Dialog(requireContext())
                customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!!.setCancelable(false)
                customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
                closeImageView.setOnClickListener {
                    customdialog!!.dismiss()
                }
                val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                drugNmae.text =
                    "${drugNmae.text.toString()} '" + templateDetail?.name + "' Record ?"

                val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    //vitalsAdapter?.deleteItem(position)


                    customdialog!!.dismiss()
                }
                noBtn.setOnClickListener {
                    customdialog!!.dismiss()
                }
                customdialog!!.show()
            }
        })

        vitalsAdapter!!.setOnSearchClickListener(object : NextVitalsAdapter.OnSearchClickListener {
            override fun onSearchClick(
                autoCompleteTextView1: AppCompatAutoCompleteTextView,
                position: Int
            ) {

                dropdownReferenceView = autoCompleteTextView1

                searchPosition = position

                viewModel!!.searchList(vitalSearchRetrofitCallback, facility_id)

            }
        })
    }

    private fun saveVitals() {

        val saveData: ArrayList<VitalSaveRequestModel> = ArrayList()
        val allDAta = vitalsAdapter!!.getall()
        val datasize: Int = allDAta.size
        var saveOk: Boolean? = false
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val dateInString = sdf.format(Date())
        if (datasize > 1) {
            for (i in 0..(datasize - 2)) {
                if (allDAta[i].vital_value.trim().length <= 0) {
                    saveOk = false
                    break

                } else {
                    saveOk = true
                    val vitalSaveRequestModel = VitalSaveRequestModel()
                    vitalSaveRequestModel.facility_uuid = facility_id.toString()
                    vitalSaveRequestModel.department_uuid = deparment_UUID.toString()
                    vitalSaveRequestModel.patient_uuid = patientId.toString()
                    vitalSaveRequestModel.encounter_uuid = encounter_id!!
                    vitalSaveRequestModel.encounter_type_uuid = encounter_type!!

                    vitalSaveRequestModel.performed_date = dateInString + "Z"
                    vitalSaveRequestModel.tat_start_time = dateInString + "Z"
                    vitalSaveRequestModel.tat_end_time = dateInString + "Z"

                    vitalSaveRequestModel.vital_group_uuid = 1
                    vitalSaveRequestModel.vital_type_uuid = 1
                    vitalSaveRequestModel.vital_qualifier_uuid = 1
                    vitalSaveRequestModel.patient_vital_status_uuid = 1
                    vitalSaveRequestModel.vital_value_type_uuid = 1
                    vitalSaveRequestModel.vital_master_uuid = allDAta[i].uuid
                    vitalSaveRequestModel.vital_value = allDAta[i].vital_value
                    vitalSaveRequestModel.vital_uom_uuid = allDAta[i].uom_master_uuid
                    saveData.add(vitalSaveRequestModel)

                }
            }
            if (saveOk!!) {
                val gson = Gson()
                val json = gson.toJson(saveData)
                Log.e("SaveRequest", json)
                Log.e("Save", "Saveee")


                customProgressDialogShow(true)
                viewModel!!.vitalSave(facility_id, vitalsSaveRetrofitCallback, saveData)
            } else {
                Toast.makeText(context, "please enter required fields!!", Toast.LENGTH_LONG).show()
            }

        } else {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Please select any one item"
            )
        }

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NurseVitalsTempleteFragment(), "Templates")
        adapter.addFragment(PrevNurseVitalsFragment.newInstance(patientId!!), "Previous.Vital(s)")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = java.util.ArrayList<Fragment>()
        private val mFragmentTitleList = java.util.ArrayList<String>()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    val getUmoListRetrofitCallback = object : RetrofitCallback<UOMNewReferernceResponseModel?> {
        override fun onSuccessfulResponse(responseBody: Response<UOMNewReferernceResponseModel?>) {

            if (responseBody?.body()?.responseContents?.isNotEmpty()!!) {

                if (responseBody!!.body()!!.responseContents!!.size != 0) {

                    vitalsAdapter!!.setTypeValue(responseBody!!.body()!!.responseContents)

                    Log.e("Uom", responseBody.body().toString())
                }
            }

        }

        override fun onBadRequest(response: Response<UOMNewReferernceResponseModel?>) {
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


    val getVitalsListRetrofitCallback = object : RetrofitCallback<MainVItalsListResponseModel?> {
        override fun onSuccessfulResponse(responseBody: Response<MainVItalsListResponseModel?>) {

            customProgressDialogShow(false)
            if (responseBody?.body()?.responseContents?.getVitals?.isNotEmpty()!!) {

                if (responseBody!!.body()!!.responseContents?.getVitals?.size != 0) {

                    getallVitals = responseBody!!.body()!!.responseContents?.getVitals

                    binding?.mainLayout?.visibility = View.VISIBLE
                    binding?.tvVitalEmpty?.visibility = View.GONE

                    //Log.e("VitalList",responseBody!!.body()?.responseContents.toString())

                    vitalsAdapter!!.addFavouritesInRow(responseBody.body()?.responseContents?.getVitals as ArrayList<TemplateDetail>)

                    viewModel!!.getUmoList(facility_id, getUmoListRetrofitCallback)

                }
            } else {
                binding?.mainLayout?.visibility = View.GONE
                binding?.tvVitalEmpty?.visibility = View.VISIBLE
            }

        }

        override fun onBadRequest(response: Response<MainVItalsListResponseModel?>) {
            customProgressDialogShow(false)
            val gson = GsonBuilder().create()
            val responseModel: VitalsListResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    VitalsListResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            customProgressDialogShow(false)
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            customProgressDialogShow(false)
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            customProgressDialogShow(false)
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            customProgressDialogShow(false)
            if(failure!=null)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            //       customProgressDialogShow(false)
            viewModel!!.progress.value = 8
        }

    }

    val vitalSearchRetrofitCallback = object : RetrofitCallback<VitalSearchListResponseModel?> {

        override fun onSuccessfulResponse(response: Response<VitalSearchListResponseModel?>) {
            responseContents = Gson().toJson(response.body()?.responseContents)

            vitalsAdapter!!.setAdapter(
                dropdownReferenceView,
                response.body()?.responseContents!!.getVitals,
                searchPosition
            )


        }

        override fun onBadRequest(response: Response<VitalSearchListResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: VitalSearchListResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    VitalSearchListResponseModel::class.java
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


    val vitalsSaveRetrofitCallback = object : RetrofitCallback<VitalSaveResponseModel?> {
        override fun onSuccessfulResponse(response: Response<VitalSaveResponseModel?>) {
            customProgressDialogShow(false)
            responseContents = Gson().toJson(response.body()?.responseContents)
            Log.e("VitalCreated", response.body()?.responseContents.toString())

            Toast.makeText(activity, response.body()?.message!!, Toast.LENGTH_LONG).show()
            vitalsAdapter!!.clearAll()
            viewModel!!.getVitalsName(facility_id, getVitalsListRetrofitCallback)

        }

        override fun onBadRequest(response: Response<VitalSaveResponseModel?>) {
            customProgressDialogShow(false)
            val gson = GsonBuilder().create()
            val responseModel: VitalSaveResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    VitalSaveResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            customProgressDialogShow(false)
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            customProgressDialogShow(false)
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            customProgressDialogShow(false)
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            customProgressDialogShow(false)
            if(failure!=null)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            customProgressDialogShow(false)
            viewModel!!.progress.value = 8
        }


    }

    override fun sendTemplete(
        templeteDetails: List<TemplateDetail?>?,
        position: Int,
        selected: Boolean,
        id: Int
    ) {

        if (!selected) {
            for (i in templeteDetails!![position]?.vw_template_master_details!!.indices) {

                val templateDetail: TemplateDetail = TemplateDetail()
                templateDetail.name =
                    templeteDetails[position]?.vw_template_master_details!![i].vm_name!!
                templateDetail.uuid =
                    templeteDetails[position]?.vw_template_master_details!![i].vm_uuid!!
                /*  templateDetail.uom_master_uuid=
                      templeteDetails[position]?.vw_template_master_details!![i].vital_master.uom_master_uuid*/
                vitalsAdapter!!.addRow(templateDetail)
            }
        } else {

            for (i in templeteDetails!![position]?.template_master_details!!.indices) {

                vitalsAdapter!!.deleteRowItem(templeteDetails[position]?.template_master_details!![i].vital_master.uuid)

            }

        }

    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is NurseVitalsTempleteFragment) {
            childFragment.setOnTextClickedListener(this)
        }

        if (childFragment is PrevNurseVitalsFragment) {
            childFragment.setOnTextClickedListener(this)
        }
    }

    override fun sendPrevtoChild(responseContent: List<PV>?) {

        vitalsAdapter!!.clearAll()
        for (i in responseContent!!.indices) {
            val templateDetail: TemplateDetail = TemplateDetail()
            templateDetail.name = responseContent[i].vital_name.toString()
            templateDetail.uuid = responseContent[i].vital_master_uuid!!
            templateDetail.uom_master_uuid = responseContent[i].uom_uuid!!
            templateDetail.vital_value = responseContent[i].vital_value!!
            vitalsAdapter!!.addPrevRow(templateDetail)

        }


        binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
    }


}