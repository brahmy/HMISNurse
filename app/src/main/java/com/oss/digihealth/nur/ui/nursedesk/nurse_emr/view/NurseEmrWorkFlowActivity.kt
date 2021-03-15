package com.oss.digihealth.nur.ui.nursedesk.nurse_emr.view

import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.FragmentBackClick
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.FragmentNurseWorkflowBinding
import com.oss.digihealth.nur.ui.emr_workflow.model.ResponseContent
import com.oss.digihealth.nur.ui.emr_workflow.model.StoreMasterresponseContent
import com.oss.digihealth.nur.ui.emr_workflow.model.fetch_encounters_response.FetchEncounterResponseContent
import com.oss.digihealth.nur.ui.emr_workflow.view_model.EmrViewModel
import com.oss.digihealth.nur.ui.emr_workflow.view_model.EmrViewModelFactory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_CONFIG
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_BED_MANAGEMENT
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_CRITICAL_CARE_CHART
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_DIET
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_DISCHARGE_SUMMARY
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_INVESTIGATION
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_LAB
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_NOTES
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_PRESCRIPTION
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_RADIOLOGY
import com.oss.digihealth.nur.config.AppConstants.ACTIVITY_CODE_NURSE_VITAL
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.ui.NurseDeskInvestigationFragment
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.ui.NurseDeskLabFragment
import com.oss.digihealth.nur.ui.emr_workflow.chief_complaint.ui.NurseDeskVitalsFragment
import com.oss.digihealth.nur.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.view.lab.ui.NurdeDeskRadiologyChildFragment
import com.oss.digihealth.nur.ui.landingscreen.MainLandScreenActivity
import com.oss.digihealth.nur.ui.nursedesk.nursedeskconfiguration.view.NurseConfigFragment
import retrofit2.Response


class NurseEmrWorkFlowActivity : Fragment() {

    private var encounter_uuid: Int? = 0
    private var encounter_doctor_uuid: Int? = 0
    private var viewpageradapter: ViewPagerAdapter? = null
    private var tabsArrayList: ArrayList<ResponseContent?>? = null
    private var binding: FragmentNurseWorkflowBinding? = null
    private var viewModel: EmrViewModel? = null
    private var utils: Utils? = null
    private var selectedFragment: Fragment? = null
    lateinit var encounterResponseContent: List<FetchEncounterResponseContent?>
    private lateinit var getStoreMasterId: List<StoreMasterresponseContent?>
    private var patientUuid: Int = 0
    private var encounterType: Int = 0
    private var facility_id: Int = 0
    private var department_uuid: Int = 0
    private var store_master_uuid: Int? = null
    private var fragmentBackClick: FragmentBackClick? = null
    var appPreferences: AppPreferences? = null
    private var institutionName: String = ""


    val flow: String?
        get() = if (arguments == null) null else requireArguments().getSerializable(
            NurseEmrWorkFlowActivity.ARG_NAME
        ) as String


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_nurse_workflow,
                container,
                false
            )

        viewModel = EmrViewModelFactory(
            requireActivity().application
        )
            .create(EmrViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }

        /*     binding?.tvTitle?.setText("Nurse Desk")

             binding?.patientImage?.visibility=View.INVISIBLE

             binding?.tvPatientName?.visibility=View.INVISIBLE

             binding?.tvAgeGender?.visibility=View.INVISIBLE
     */

        binding?.ivConsultingView?.setImageResource(R.drawable.ic_baseline_settings_24)

        binding?.ivConsultingView?.setOnClickListener {

            val emr = NurseConfigFragment.newInstance(AppConstants.OUT_PATIENT, true)
            (activity as MainLandScreenActivity).replaceFragment(emr)


        }


        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)

        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!

        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!

        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!

        institutionName = appPreferences?.getString(AppConstants.WARDNAME)!!

        binding?.tvWard?.text = institutionName

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        binding?.contentLinearLayout?.visibility = View.INVISIBLE
        binding?.noDataFoundTextView?.visibility = View.INVISIBLE

        viewModel?.getEmrWorkFlowFav(emrWorkFlowRetrofitCallBack, facility_id)


        return binding!!.root
    }


    private fun setupViewPager(tabsArrayList: ArrayList<ResponseContent?>) {
        viewpageradapter = ViewPagerAdapter(activity?.supportFragmentManager!!)
        for (i in tabsArrayList.indices) {
            if (tabsArrayList[i]?.activity_code == ACTIVITY_CODE_NURSE_LAB || tabsArrayList[i]!!.activity_code == "Lab02") {
                viewpageradapter!!.addFragment(NurseDeskLabFragment(), "Lab")
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_INVESTIGATION) {
                viewpageradapter!!.addFragment(
                    NurseDeskInvestigationFragment(),
                    "Investigation"
                )
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_RADIOLOGY) {
                viewpageradapter!!.addFragment(NurdeDeskRadiologyChildFragment(), "Radiology")
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_VITAL || tabsArrayList[i]!!.activity_code == "0177") {

                viewpageradapter!!.addFragment(NurseDeskVitalsFragment(), "Vital")
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_PRESCRIPTION) {
                viewpageradapter!!.addFragment(NurseDeskPrescriptionFragment(), "Prescription")
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_BED_MANAGEMENT) {
                viewpageradapter!!.addFragment(
                    BedMangementParentFragment(),
                    "Bed Management"
                )
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_DISCHARGE_SUMMARY ||
                tabsArrayList[i]!!.activity_code == "Dis05"
            ) {
                viewpageradapter!!.addFragment(
                    ParentNurseDichargeSummaryFragment(),
                    "Discharge Summary"
                )
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_INVESTIGATION) {
                viewpageradapter!!.addFragment(
                    NurseDeskInvestigationFragment(),
                    "Nurse Investigation"
                )
                /*     } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_LAB) {
                         viewpageradapter!!.addFragment(NurseDeskLabFragment(), "Lab")
                     } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_RADIOLOGY) {
                         viewpageradapter!!.addFragment(
                             NurdeDeskRadiologyChildFragment(),
                             "Radiology"
                         )*/
            } else if (tabsArrayList[i]!!.activity_code?.trim() == ACTIVITY_CODE_NURSE_CRITICAL_CARE_CHART) {
                viewpageradapter!!.addFragment(
                    NurseDeskCriticalCareChartListFragment(),
                    "Critical Care Chart"
                )
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_DIET) {
                viewpageradapter!!.addFragment(NurseDeskDietFragment(), "Diet")
            } else if (tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_NURSE_NOTES) {
                viewpageradapter!!.addFragment(NurseDeskNotesListFragment(), "Notes")
            } else {
                //   viewpageradapter!!.addFragment(SampleFragment(), "Certificate")

                viewpageradapter!!.addFragment(SampleFragment(), "Bed Management")
            }
        }
        binding?.viewPager?.adapter = viewpageradapter
        viewpageradapter?.notifyDataSetChanged()
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

        fun getCurrentFragment(position: Int): Fragment? {
            return mFragmentList[position]
        }

    }

    private val emrWorkFlowRetrofitCallBack = object : RetrofitCallback<EmrWorkFlowResponseModel?> {
        override fun onSuccessfulResponse(response: Response<EmrWorkFlowResponseModel?>) {
            if (response.body()?.responseContents?.isNotEmpty()!!) {
                binding?.contentLinearLayout?.visibility = View.VISIBLE
                binding?.noDataFoundTextView?.visibility = View.INVISIBLE

                tabsArrayList = response.body()?.responseContents!!

                for (i in tabsArrayList!!.indices) {

                    if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_CONFIG) {

                        tabsArrayList!!.removeAt(i)

                        break

                    }

                }

                setupViewPager(tabsArrayList!!)
//                binding?.viewPager!!.setOffscreenPageLimit(4)
                binding?.tabLayout!!.setupWithViewPager(binding?.viewPager!!)
                for (i in tabsArrayList!!.indices) {
                    val layoutInflater: View? =
                        LayoutInflater.from(requireContext())
                            .inflate(R.layout.treatment_custom_tab_row, null, false)
                    val tabImageView = layoutInflater?.findViewById(R.id.tabImageView) as ImageView
                    val tabTextView = layoutInflater.findViewById(R.id.tabTextView) as TextView


                    if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_VITAL || tabsArrayList!![i]!!.activity_code == "0177") {

                        tabImageView.setImageResource(R.drawable.ic_vitals_icon)

                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_BED_MANAGEMENT) {

                        tabImageView.setImageResource(R.drawable.ic_bed_management)

                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_DISCHARGE_SUMMARY ||
                        tabsArrayList!![i]!!.activity_code == "Dis05"
                    ) {

                        tabImageView.setImageResource(R.drawable.ic_discharge_summary)
                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_LAB) {
                        tabImageView.setImageResource(R.drawable.ic_widget_lab)
                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_RADIOLOGY) {
                        tabImageView.setImageResource(R.drawable.ic_widget_radiology)
                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_PRESCRIPTION) {
                        tabImageView.setImageResource(R.drawable.ic_widget_prescription)
                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_INVESTIGATION) {
                        tabImageView.setImageResource(R.drawable.ic_widget_investigation)
                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_NOTES) {
                        tabImageView.setImageResource(R.drawable.ic_widget_notes)
                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_DIET) {
                        tabImageView.setImageResource(R.drawable.ic_widget_diet_order)
                    } else if (tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_NURSE_CRITICAL_CARE_CHART) {
                        tabImageView.setImageResource(R.drawable.ic_widget_critical_case_sheet)
                    }
                    tabTextView.text = tabsArrayList!![i]?.activity_name
                    binding?.tabLayout?.getTabAt(i)!!.customView = layoutInflater


                }
                binding?.viewPager?.addOnPageChangeListener(object :
                    ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        when (tabsArrayList!![position]!!.activity_code) {
                            ACTIVITY_CODE_NURSE_BED_MANAGEMENT ->
                                (viewpageradapter?.getCurrentFragment(position) as BedMangementParentFragment).refreshPage()
                            ACTIVITY_CODE_NURSE_DISCHARGE_SUMMARY ->
                                (viewpageradapter?.getCurrentFragment(position) as ParentNurseDichargeSummaryFragment).refreshPage()

                            ACTIVITY_CODE_NURSE_LAB ->
                                (viewpageradapter?.getCurrentFragment(position) as NurseDeskLabFragment).refreshPage()
                            ACTIVITY_CODE_NURSE_INVESTIGATION ->
                                (viewpageradapter?.getCurrentFragment(position) as NurseDeskInvestigationFragment).refreshPage()
                            "Dis05" ->
                                (viewpageradapter?.getCurrentFragment(position) as ParentNurseDichargeSummaryFragment).refreshPage()
                        }
                    }
                })
            } else {
                binding?.contentLinearLayout?.visibility = View.INVISIBLE
                binding?.noDataFoundTextView?.visibility = View.VISIBLE

                val emr = NurseConfigFragment.newInstance(AppConstants.OUT_PATIENT, true)
                (activity as MainLandScreenActivity).replaceFragment(emr)


            }
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
            viewModel!!.progressBar.value = 8
        }
    }


    companion object {
        const val ARG_NAME = "flow"


        fun newInstance(from: String): NurseEmrWorkFlowActivity {
            val fragment = NurseEmrWorkFlowActivity()

            val bundle = Bundle().apply {
                putSerializable(ARG_NAME, from)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        fragmentBackClick?.setSelectedFragment(this)
    }


}