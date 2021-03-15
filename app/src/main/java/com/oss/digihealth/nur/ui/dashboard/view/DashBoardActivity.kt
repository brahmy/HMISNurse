package com.oss.digihealth.nur.ui.dashboard.view
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.BuildConfig
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.HomeLayoutBinding
import com.oss.digihealth.nur.ui.dashboard.model.*
import com.oss.digihealth.nur.ui.dashboard.view_model.DashboardViewModel
import com.oss.digihealth.nur.ui.dashboard.view_model.DashboardViewModelFactory
import com.oss.digihealth.nur.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.prescription.model.ZeroStockResponseContent
import com.oss.digihealth.nur.ui.landingscreen.MainLandScreenActivity
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import kotlinx.android.synthetic.main.home_layout.*
import kotlinx.android.synthetic.main.navigation_layout.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


class DashBoardActivity : Fragment() {
    var utils: Utils? = null
    var binding: HomeLayoutBinding? = null
    private var viewModel: DashboardViewModel? = null
    var appPreferences: AppPreferences? = null
    var name: String? = null
    private var patientsAdapter: PatientsAdapter? = null
    private var data = ArrayList<CommonCount>()
    private var diagnosisData = ArrayList<Diagnosis>()
    private var chiefComplaintsData = ArrayList<ChiefComplients>()
    var fragmentAdapter: PatientsPagerAdapter? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var facilityId: Int? = null
    private var departmentUuid: Int? = null

    private val tabIcons = intArrayOf(
        R.drawable.ic_chief_complaints,
        R.drawable.ic_chief_complaints,
        R.drawable.ic_chief_complaints
    )
    var plus: Boolean? = true
    var emrcheck: Boolean? = null
    var registercheck: Boolean? = null
    var LmisCheck: Boolean? = null
    private var customdialog: Dialog? = null
    private var patientType: String? = null

    private var startYear = 0
    private var startMonthOfYear: Int = 0
    private var startDayOfMonth: Int = 0
    private var endYear: Int = 0
    private var endMonthOfYear: Int = 0
    private var endDayOfMonth: Int = 0

    private val sessionArrayList :ArrayList<GetSessionResp.ResponseContent?> = ArrayList()
    private var sessionList = mutableMapOf<Int, String>()
    private val sessionListUuid:HashMap<Int, Int> = HashMap()


    private val genderArrayList :ArrayList<GetGenderResp.ResponseContent?> = ArrayList()
    private var genderList = mutableMapOf<Int, String>()
    private val genderListUuid:HashMap<Int, Int> = HashMap()
    private var selectedGenderId = ""
    private var selectedSessionId = ""



    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel(inflater,container)
        initPerfernces()
        initPatientsAdapter()
        initDropDownCliCk()


        binding?.imgFilter?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        languages?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog = LanguagesDialogFragemnt()
            dialog.show(ft, "Tag")
        }

        trackDashBoardAnalyticsVisit()

        binding!!.outPatientCardView?.setOnClickListener {

            patientType = AppConstants.OUT_PATIENT

            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 2)

        }


        binding!!.inPatientCardView?.setOnClickListener {

            patientType = AppConstants.IN_PATIENT

            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 3)

        }

        binding!!.confifav?.setOnClickListener {

            val emr = ConfigActivity.newInstance(AppConstants.CONFRIGURATION, false)

            (activity as MainLandScreenActivity).replaceFragment(emr)


        }
        displayFilterDialog()


        if (BuildConfig.FLAVOR == "dev"|| BuildConfig.FLAVOR == "puneuat")
            binding?.cvTelemedicine?.visibility = View.VISIBLE
        else
            binding?.cvTelemedicine?.visibility = View.VISIBLE
        binding?.cvTelemedicine?.setOnClickListener {
//            startActivity(Intent(requireActivity(), VideoCallActivity::class.java))
        }

        (activity as MainLandScreenActivity).emrStartDate = getCurrentDate("dd-MM-yyyy")

        (activity as MainLandScreenActivity).emrEndDate = getCurrentDate("dd-MM-yyyy")

        utils?.convertDateFormat((activity as MainLandScreenActivity).emrStartDate,"dd-MM-yyyy","yyyy-MM-dd")
            ?.let {
                getPatientsInfo(
                    it,
                    utils?.convertDateFormat((activity as MainLandScreenActivity).emrEndDate,"dd-MM-yyyy","yyyy-MM-dd")!!,
                    selectedGenderId,
                    selectedSessionId
                )
            }
        getSession()
        getGender()

        binding?.spinnerSession?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()
                if(pos != 0) {
                    selectedSessionId =
                        sessionList.filterValues { it == itemValue }.keys.toList()[0].toString()
                }else{
                    selectedSessionId = ""
                }
            }

        }

        binding?.spinnerGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val itemValue = parent?.getItemAtPosition(pos).toString()
                if(pos != 0) {
                    selectedGenderId =
                        genderList.filterValues { it == itemValue }.keys.toList()[0].toString()
                }else{
                    selectedGenderId = ""
                }
            }

        }

        return binding!!.root
    }



    fun initPerfernces(){
        Utils(requireContext()).setCalendarLocale("en", requireContext())
        utils = Utils(requireContext())
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        name = appPreferences?.getString(AppConstants.INSTITUTION_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        emrcheck = appPreferences?.getBoolean(AppConstants.EMRCHECK)
        LmisCheck = appPreferences?.getBoolean(AppConstants.LMISCHECK)
        registercheck = appPreferences?.getBoolean(AppConstants.REGISTRATIONCHECK)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        userNameTextView?.setText(userDataStoreBean?.first_name)
    }



    fun initViewModel( inflater: LayoutInflater, container: ViewGroup?){
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.home_layout,
                container,
                false
            )

        viewModel = DashboardViewModelFactory(
            requireActivity().application
        ).create(DashboardViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
    }

    private fun setupTabIcons() {
        tabLayout.getTabAt(0)!!.setIcon(tabIcons[0])
        tabLayout.getTabAt(1)!!.setIcon(tabIcons[1])
        tabLayout.getTabAt(2)!!.setIcon(tabIcons[1])

    }


    private fun setUpViewPager() {
        //  nestedScrollView.isFillViewport = true
        fragmentAdapter =
            PatientsPagerAdapter(childFragmentManager, diagnosisData, chiefComplaintsData)
        viewPager.adapter = fragmentAdapter
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager)
        if(!isTablet(requireContext())) {
            initChiefComplientAdapter(chiefComplaintsData)
            initDiagnosisAdapter(diagnosisData)
            callZeroStock()
        }
    }

    private fun getPatientsInfo(fromDate: String, toDate: String, gender: String, session: String) {
        viewModel!!.getPatientsDetails(
            departmentUuid!!,
            fromDate,
            toDate,
            gender,
            session,
            facilityId!!,
            dashBoardDetailRetrofitCallBack
        )
    }


    private fun initPatientsAdapter() {
        patientsAdapter = PatientsAdapter(requireContext()!!, data)
        if(isTablet(requireContext())) {
            binding!!.rvPatientsCount!!.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }else{
            binding!!.rvPatientsCount!!.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding!!.rvPatientsCount!!.adapter = patientsAdapter
    }

    private fun displayFilterDialog() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        val currentDate = Calendar.getInstance()
        val mYear = currentDate[Calendar.YEAR] // current year
        val mMonth = currentDate[Calendar.MONTH] // current month
        val mDay = currentDate[Calendar.DAY_OF_MONTH] // current day
        /*val calendarRangeDialog = CalendarRangeDialog(requireContext())
        if (calendarRangeDialog.window != null) calendarRangeDialog.window!!
            .setGravity(Gravity.TOP or Gravity.END)*/

        if (startYear == 0 && endYear == 0) {
            startYear = mYear
            endYear = mYear
            startMonthOfYear = mMonth
            endMonthOfYear = mMonth
            startDayOfMonth = mDay
            endDayOfMonth = mDay
        } else {
            binding?.etStartDate?.setText(startDayOfMonth.toString() + "/" + (startMonthOfYear + 1) + "/" + startYear)
            binding?.etEndDate?.setText(endDayOfMonth.toString() + "/" + (endMonthOfYear + 1) + "/" + endYear)
            startDate[startYear, startMonthOfYear] = startDayOfMonth
            endDate[endYear, endMonthOfYear] = endDayOfMonth
        }

        binding?.etStartDate?.setText(startDayOfMonth.toString() + "/" + (startMonthOfYear + 1) + "/" + startYear)
        binding?.etEndDate?.setText(endDayOfMonth.toString() + "/" + (endMonthOfYear + 1) + "/" + endYear)



        binding?.etStartDate!!.setOnClickListener { // date picker dialog
            val datePickerDialog = DatePickerDialog(
                it.context,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    startYear = year
                    startMonthOfYear = monthOfYear
                    startDayOfMonth = dayOfMonth
                    // set day of month , month and year value in the edit text
                    binding?.etStartDate!!.setText(utils?.emrDisplayDate(dayOfMonth.toString() +
                            "-" + (monthOfYear + 1) + "-" + year,"dd-MM-yyyy"))
                    startDate[year, monthOfYear] = dayOfMonth
                },
                startYear, startMonthOfYear, startDayOfMonth
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.etEndDate!!.setOnClickListener { // date picker dialog
            val datePickerDialog = DatePickerDialog(
                it.context,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    endYear = year
                    endMonthOfYear = monthOfYear
                    endDayOfMonth = dayOfMonth
                    // set day of month , month and year value in the edit text
                    binding?.etEndDate!!.setText(utils?.emrDisplayDate(dayOfMonth.toString() +
                            "-" + (monthOfYear + 1) + "-" + year,"dd-MM-yyyy"))
                    endDate[year, monthOfYear] = dayOfMonth
                },
                endYear, endMonthOfYear, endDayOfMonth
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.btnApply?.setOnClickListener {
            if (/*calendarRangeDialog.spinnerSession?.selectedItem == null ||
                calendarRangeDialog.spinnerGender?.selectedItem == null ||*/
                binding?.etStartDate!!.text.toString() == "" ||
                binding?.etEndDate!!.text.toString() == ""
            ) {
                Toast.makeText(it.context, "Please select the mandatory fields", Toast.LENGTH_SHORT)
                    .show()
            } else if (endDate.timeInMillis - startDate.timeInMillis < 0) {
                Toast.makeText(
                    it.context,
                    "End date should greater than Begin Date",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                (activity as MainLandScreenActivity).emrStartDate =
                    dateFormat.format(startDate.time)
                (activity as MainLandScreenActivity).emrEndDate = dateFormat.format(endDate.time)


                utils?.convertDateFormat((activity as MainLandScreenActivity).emrStartDate,"dd-MM-yyyy","yyyy-MM-dd")
                    ?.let { it1 ->
                        getPatientsInfo(
                            it1,
                            utils?.convertDateFormat((activity as MainLandScreenActivity).emrEndDate,"dd-MM-yyyy","yyyy-MM-dd")!!,
                            selectedGenderId,
                            selectedSessionId
                        )
                    }
                binding?.drawerLayout?.closeDrawer(GravityCompat.END)
            }

        }

        binding?.btnCancel?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.END)
            if (binding?.etStartDate!!.text.toString() != "" ||
                binding?.etEndDate!!.text.toString() != ""
            ) {
                binding?.drawerLayout?.closeDrawer(GravityCompat.END)
            }
//            startYear = 0
//            endYear = 0
//            startMonthOfYear = 0
//            endMonthOfYear = 0
//            startDayOfMonth = 0
//            endDayOfMonth = 0
        }


    }

    private fun getSession() {
        viewModel?.getSession(facilityId!!, getSessionRespCallback)
    }

    private fun getGender() {
        val body = GetGenderReq(
            codename = "001",
            is_active = 1,
            pageNo = 0,
            paginationSize = 10,
            search = "Superhmis",
            sortField = "name",
            sortOrder = "DESC"
        )
        viewModel?.getGender(facilityId!!, body, getGenderRespCallback)
    }

    private val getGenderRespCallback = object : RetrofitCallback<GetGenderResp?> {
        override fun onSuccessfulResponse(responseBody: Response<GetGenderResp?>) {
            if (responseBody?.isSuccessful == true) {
                responseBody.body()?.let { getGenderResp ->
                    responseBody.body()?.let { getSessionResp ->
                        val data = GetGenderResp.ResponseContent()
                        data.name = "Select Gender"
                        data.uuid = 0
                        genderArrayList.add(data)
                        genderArrayList.addAll(responseBody?.body()!!.responseContents!!)
                        genderList =
                            genderArrayList?.map { it?.uuid!! to it.name!! }!!.toMap()
                                .toMutableMap()
                        for (i in genderArrayList?.indices!!) {
                            genderListUuid[i] =
                                genderArrayList?.get(i)?.uuid!!
                        }
                        val adapter =
                            ArrayAdapter<String>(
                                requireContext(),
                                R.layout.spinner_item,
                                genderList.values.toMutableList()
                            )
                        adapter.setDropDownViewResource(R.layout.spinner_item)
                        spinnerGender?.adapter = adapter
                        spinnerGender?.setSelection(0)
                    }
                }
            }
        }

        override fun onBadRequest(errorBody: Response<GetGenderResp?>) {
            val gson = GsonBuilder().create()
            val responseModel: DashBoardResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    DashBoardResponse::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            Toast.makeText(context!!, "Server Error", Toast.LENGTH_LONG).show()
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }

        override fun onFailure(s: String?) {

        }
    }


    private val getSessionRespCallback = object : RetrofitCallback<GetSessionResp?> {
        override fun onSuccessfulResponse(responseBody: Response<GetSessionResp?>) {
            if (responseBody?.isSuccessful == true) {
                responseBody.body()?.let { getSessionResp ->
                    val data =  GetSessionResp.ResponseContent()
                    data.name = "Select Session"
                    data.uuid = 0
                    sessionArrayList.add(data)
                    sessionArrayList.addAll(responseBody?.body()!!.responseContents!!)
                    sessionList =
                        sessionArrayList?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
                    for (i in sessionArrayList?.indices!!) {
                        sessionListUuid[i]=
                            sessionArrayList?.get(i)?.uuid!!
                    }
                    val adapter =
                        ArrayAdapter<String>(
                            requireContext(),
                            R.layout.spinner_item,
                            sessionList.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(R.layout.spinner_item)
                    spinnerSession?.adapter= adapter
                    spinnerSession?.setSelection(0)
                }
            }
        }



        override fun onBadRequest(errorBody: Response<GetSessionResp?>) {
            val gson = GsonBuilder().create()
            val responseModel: DashBoardResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    DashBoardResponse::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            Toast.makeText(context!!, "Server Error", Toast.LENGTH_LONG).show()
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }

        override fun onFailure(s: String?) {

        }
    }


    private val dashBoardDetailRetrofitCallBack = object : RetrofitCallback<DashBoardResponse?> {
        override fun onSuccessfulResponse(responseBody: Response<DashBoardResponse?>) {
            prepareDataForPatientsAdapter(responseBody?.body()?.responseContents)
            chiefComplaintsData = responseBody?.body()?.responseContents?.cieif_complaints!!
            diagnosisData = responseBody?.body()?.responseContents?.diagnosis!!
            try {
                setUpViewPager()
                setupTabIcons()
                prepareDataForGraphLines(responseBody?.body()?.responseContents!!)
            } catch (e: Exception) {

            }

        }

        override fun onBadRequest(errorBody: Response<DashBoardResponse?>) {
            val gson = GsonBuilder().create()
            val responseModel: DashBoardResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    DashBoardResponse::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            Toast.makeText(context!!, "Server Error", Toast.LENGTH_LONG).show()
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }


        override fun onFailure(s: String?) {

        }
    }

    private val emrWorkFlowRetrofitCallBack = object : RetrofitCallback<EmrWorkFlowResponseModel?> {
        override fun onSuccessfulResponse(response: Response<EmrWorkFlowResponseModel?>) {
            if (response.body()?.responseContents?.isNotEmpty()!!) {

                if (patientType.equals(AppConstants.OUT_PATIENT)) {

                    (activity as MainLandScreenActivity).replaceFragment(OutPatientFragment())
                } else {

                    (activity as MainLandScreenActivity).replaceFragment(InpatientFragment())


                }
            } else {

                val emr = ConfigActivity.newInstance(patientType!!, true)
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
                    mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()

        }

        override fun onFailure(failure: String?) {
            if(failure!=null)
            utils?.showToast(
                R.color.negativeToast,
                mainLayout!!,
                failure
            )
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }

    fun prepareDataForPatientsAdapter(responseContents: DashBoardContent?) {
        data.clear()
        if (responseContents?.consulted!!.isNotEmpty()) {
            val consultendData = responseContents?.consulted!![0]
            val groupDataOne = CommonCount(
                "F-" + consultendData.F_Count!!.toString(),
                "M-" + consultendData.M_Count!!.toString(),
                "TG-" + consultendData.T_Count!!.toString(),
                consultendData.Tot_Count!!.toString(),
                "Consulted-Patients",
                ContextCompat.getColor(requireContext(), R.color.cp)
            )
            data.add(groupDataOne)
        }

        val orderData = responseContents?.orders
        val groupDataTwo = CommonCount(
            "RAD-" + orderData?.rad_count!!.toString(),
            "LAB-" + orderData.lab_count!!.toString(),
            "INV-" + orderData.inv_count!!.toString(),
            orderData.total_count!!.toString(),
            "Orders",
            ContextCompat.getColor(requireContext(), R.color.orders)
        )

        data.add(groupDataTwo)
        patientsAdapter?.updateAdapter(data)
        if (responseContents?.prescription!!.isNotEmpty()) {
            val prescriptionData = responseContents?.prescription!![0]
            val groupDataTwo = CommonCount(
                "F-" + prescriptionData.F_Count!!.toString(),
                "M-" + prescriptionData.M_Count!!.toString(),
                "TG-" + prescriptionData.T_Count!!.toString(),
                prescriptionData.Tot_Count!!.toString(),
                "Prescriptions",
                ContextCompat.getColor(requireContext(), R.color.prescriptionsYellow)
            )

            data.add(groupDataTwo)
        }
        patientsAdapter?.updateAdapter(data)
    }

    fun prepareDataForGraphLines(responseContents: DashBoardContent) {
        var consMap: LinkedHashMap<String?, Int?>? = LinkedHashMap<String?, Int?>()
        consMap = responseContents?.cons_graph!!

        val graphValue1: MutableCollection<Int?> = consMap.values
        val listGraphValues1: ArrayList<Int> = ArrayList<Int>(graphValue1)!!
        val graphKey1: MutableCollection<String?> = consMap.keys
        val listKey1: ArrayList<String> = ArrayList<String>(graphKey1)!!

        var orderMap: LinkedHashMap<String?, Int?>? = LinkedHashMap<String?, Int?>()
        orderMap = responseContents.orders_graph!!

        val graphValue2: MutableCollection<Int?> = orderMap.values
        val listGraphValues2: ArrayList<Int> = ArrayList<Int>(graphValue2)!!
        val graphKey2: MutableCollection<String?> = orderMap.keys
        val listKey2: ArrayList<String> = ArrayList<String>(graphKey2)!!

        val xAxisLabel1 = ArrayList<String>()
        for (i in listKey1.indices) {
            xAxisLabel1.add(listKey1[i])
        }

        graphview.getLayoutParams().height = 250
        graphview.invalidate()

        val xAxis: XAxis = graphview.getXAxis()
        xAxis.setValueFormatter(IndexAxisValueFormatter(getDate(xAxisLabel1)));
        xAxis.textSize = 12f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = ColorTemplate.getHoloBlue()
        xAxis.isEnabled = true
        xAxis.enableGridDashedLine(10f, 10f, 0f)

        val leftAxis: YAxis = graphview.getAxisLeft()
        leftAxis.removeAllLimitLines()
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMinimum = 0f
        leftAxis.textSize= 12f
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawLimitLinesBehindData(true)
        leftAxis.setDrawGridLines(true)

        graphview.getAxisRight().setEnabled(false)

        /*  val entries1 = ArrayList<Entry>()
          val  entries2 = ArrayList<Entry>()*/

        val entries1 = ArrayList<Entry>()

        val entries2 = ArrayList<Entry>()

        for (i in listKey1.indices) {
            entries1.add(Entry(i.toFloat(), listGraphValues1[i].toFloat()))
        }
        for (i in listKey2.indices) {
            entries2.add(Entry(i.toFloat(), listGraphValues2[i].toFloat()))
        }

        val lines = ArrayList<ILineDataSet>()

        val set1 = LineDataSet(entries1, "Patients")
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.8f
        set1.circleRadius = 4f
        set1.setCircleColor(Color.GREEN)
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.color = Color.parseColor("#ffcf9f")
        set1.fillColor = Color.parseColor("#ffcf9f")
        set1.fillAlpha = 100
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> graphview.getAxisLeft().getAxisMinimum() }

        lines.add(set1)


        val set2 = LineDataSet(entries2, "Orders")
        set2.mode = LineDataSet.Mode.CUBIC_BEZIER
        set2.cubicIntensity = 0.2f
        set2.setDrawFilled(true)
        set2.setDrawCircles(false)
        set2.lineWidth = 1.8f
        set2.circleRadius = 4f
        set2.setCircleColor(Color.GREEN)
        set2.highLightColor = Color.rgb(244, 117, 117)
        set2.color = Color.parseColor("#9accfd")
        set2.fillColor = Color.parseColor("#9accfd")
        set2.fillAlpha = 100
        set2.setDrawHorizontalHighlightIndicator(false)
        set2.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> graphview.getAxisLeft().getAxisMinimum() }
        lines.add(set2)
        val data = LineData(lines)
        data.setValueTextSize(9f)
        data.setDrawValues(false)
        graphview.axisLeft.textSize = 12f
        graphview.axisRight.textSize = 12f
        graphview.setData(data)

    }

    fun getDate(xAxisLabel1: ArrayList<String>): ArrayList<String>? {
        val label = java.util.ArrayList<String>()
        for (i in xAxisLabel1.indices) label.add(xAxisLabel1.get(i))
        return label
    }

    private fun getCurrentDate(format: String): String {
        val currentDate = SimpleDateFormat(format, Locale.getDefault()).format(Date())
        return currentDate
    }


    fun trackDashBoardAnalyticsVisit() {
//        AnalyticsManager.getAnalyticsManager().trackDashBoardVisit()
    }

    fun initDiagnosisAdapter(data: ArrayList<Diagnosis>?){
        var diagnosisAdapter: DiagnosisAdapter? =DiagnosisAdapter(requireContext(),data)
        binding?.rvTopD?.layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvTopD?.adapter = diagnosisAdapter
    }

    fun initChiefComplientAdapter(data: ArrayList<ChiefComplients>?){
        var diagnosisAdapter: CheifComplaintsApater? =CheifComplaintsApater(requireContext(),data)
        binding?.rvTopC?.layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvTopC?.adapter = diagnosisAdapter
    }

    fun initZeroStockAdapter(data: List<ZeroStockResponseContent>?){
        var diagnosisAdapter: ZeroStockAdapter? =ZeroStockAdapter(requireContext(),data!!)
        binding?.rvZeroS?.layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvZeroS?.adapter = diagnosisAdapter
    }

    fun callZeroStock(){
        viewModel?.getZeroStock( facilityId!!,zeroStockCallBack)
    }

    fun initDropDownCliCk(){
        binding?.rlTopDiagonis?.setOnClickListener {
            if (binding?.cvTopD?.isvisible()!!) {
                hideDiaDropDown()
            } else {
                showDiaDropDown()
            }
        }

        binding?.rlTopCompilents?.setOnClickListener {
            if (binding?.cvTopC?.isvisible()!!) {
                hideChieDropDown()
            } else {
                showChieDropDown()
            }
        }

        binding?.rlHeaderZeroStock?.setOnClickListener {
            if (binding?.cvZeroS?.isvisible()!!) {
                hideZeroStockDropDown()
            } else {
                showZeroStockDropDown()
            }
        }
    }

    fun showDiaDropDown(){
        // slide_down(requireContext(), binding?.cvTopD!!)
        binding?.cvTopD?.show()
        binding?.ivArrowTd?.rotation = 270F
    }

    fun hideDiaDropDown(){
        //  slide_down(requireContext(), binding?.cvTopD!!)
        binding?.ivArrowTd?.rotation = 90F
        binding?.cvTopD?.hide()
    }

    fun showChieDropDown(){
        // slide_down(requireContext(), binding?.cvTopC!!)
        binding?.cvTopC?.show()
        binding?.ivArrowTc?.rotation = 270F
    }

    fun hideChieDropDown(){
        //  slide_down(requireContext(), binding?.cvTopC!!)
        binding?.ivArrowTc?.rotation = 90F
        binding?.cvTopC?.hide()
    }

    fun showZeroStockDropDown(){
        // slide_down(requireContext(), binding?.cvZeroS!!)
        binding?.cvZeroS?.show()
        binding?.ivArrowZc?.rotation = 270F
    }

    fun hideZeroStockDropDown(){
        //slide_down(requireContext(), binding?.cvZeroS!!)
        binding?.ivArrowZc?.rotation = 90F
        binding?.cvZeroS?.hide()
    }




    val zeroStockCallBack = object : RetrofitCallback<ZeroStockResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<ZeroStockResponseModel>?) {
            if(responseBody?.body()?.responseContents?.isNotEmpty()!!) {
                view?.rvPatientsComplients?.visibility = View.VISIBLE
                view?.tv_no_data_avaliable?.visibility = View.GONE
                Log.e("zerStock", responseBody?.body()?.responseContents.toString())
                initZeroStockAdapter(responseBody?.body()?.responseContents)
            }
        }

        override fun onBadRequest(errorBody: Response<ZeroStockResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: ZeroStockResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ZeroStockResponseModel::class.java
                )

                Log.e("zerStock", "BadRequest" + responseModel.message!!)

            } catch (e: Exception) {
                Log.e("zerStock", "BadRequest")
                e.printStackTrace()
            }

            // Log.e("postAllergyData", "BadRequest")

        }

        override fun onServerError(response: Response<*>?) {

            Log.e("zerStock", "server")
        }

        override fun onUnAuthorized() {
            Log.e("zerStock", "UnAuth")
        }

        override fun onForbidden() {
            Log.e("postAllergyData", "ForBidd")
        }

        override fun onFailure(s: String?) {
            Log.e("zerStock", s.toString())
        }

        override fun onEverytime() {

        }

    }



}
