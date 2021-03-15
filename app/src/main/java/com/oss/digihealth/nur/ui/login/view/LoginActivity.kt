package com.oss.digihealth.nur.ui.login.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oss.digihealth.doc.ui.login.model.login_response_model.LoginResponseContents
import com.oss.digihealth.doc.ui.login.model.login_response_model.LoginResponseModel
import com.oss.digihealth.doc.ui.login.model.login_response_model.ModuleDetail
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.BuildConfig
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.ActivityLoginBinding
import com.oss.digihealth.nur.ui.emr_workflow.history.surgery.model.response.SurgeryInstitutionResponseModel
import com.oss.digihealth.nur.ui.emr_workflow.model.FavouritesResponseModel
import com.oss.digihealth.nur.ui.institute.model.DepartmentResponseModel
import com.oss.digihealth.nur.ui.institute.model.OfficeResponseModel
import com.oss.digihealth.nur.ui.institute.model.wardlist.Ward
import com.oss.digihealth.nur.ui.institute.model.wardlist.WardListResponseModel
import com.oss.digihealth.nur.ui.institute.view.*
import com.oss.digihealth.nur.ui.login.model.*
import com.oss.digihealth.nur.ui.login.setpassword.view.SetPasswordFragment
import com.oss.digihealth.nur.ui.login.view_model.LoginViewModel
import com.oss.digihealth.nur.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList


class LoginActivity : AppCompatActivity() {
    private var facilityUserID: Int? = 0
    private var departmentID: Int? = 0
    private var facilitylevelID: Int? = 0
    private var module_details: List<ModuleDetail?>? = ArrayList()
    private var binding: ActivityLoginBinding? = null
    private var viewModel: LoginViewModel? = null
    private var utils: Utils? = null
    var action: Boolean? = null
    var enableeye: Boolean? = true
    var version: String? = null
    var type: String = ""
    var resourse: Resources? = null
//    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var customProgressDialog: CustomProgressDialog? = null
    var appPreferences: AppPreferences? = null
//    private val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
//    private var firebaseDefaultMap: HashMap<String, Int>? = null
    val VERSION_CODE_KEY = "latest_app_version"
    private val TAG = "MainActivity"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Utils(this).isTablet(this)) {
            this.setTheme(R.style.Hmis_LoginTablet)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            this.setTheme(R.style.Hmis_Login)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        AnalyticsManager.getAnalyticsManager().tarckLoginVisit(this)
/*
         viewModel = LoginViewModelFactory(
             application,
             loginRetrofitCallBack,
             otpRetrofitCallBack,
             changePasswordRetrofitCallBack
         ).create(LoginViewModel::class.java)
*/
        viewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(LoginViewModel::class.java)
        viewModel!!.loginRetrofitCallBack=loginRetrofitCallBack
        viewModel!!.otpRetrofitCallBack=otpRetrofitCallBack
        viewModel!!.changePasswordRetrofitCallBack=changePasswordRetrofitCallBack


        binding!!.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(this)
        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            val version: String = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        binding?.tvVersion?.text = "Version" + " " + BuildConfig.VERSION_NAME

        if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {
            binding?.tvBottomView?.text = getString(R.string.copyright_msg_pune)
        } else {
            binding?.tvBottomView?.text = getString(R.string.copyright_msg)
        }
        // Obtain the FirebaseAnalytics instance.
//        firebaseAnalytics = Firebase.analytics
        //This is default Map
        //Setting the Default Map Value with the current version code
/*
        firebaseDefaultMap = HashMap()
        firebaseDefaultMap!![VERSION_CODE_KEY] = getCurrentVersionCode()
        mFirebaseRemoteConfig.setDefaultsAsync(firebaseDefaultMap!! as Map<String, Any>)

        mFirebaseRemoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        )
        //Fetching the values here
        mFirebaseRemoteConfig.fetch()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mFirebaseRemoteConfig.activateFetched()

                    //calling function to check if new version is available or not
                    checkForUpdate()
                } else {
                    Toast.makeText(
                        this@LoginActivity, "Something went wrong please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

*/

        customProgressDialog = CustomProgressDialog(this)
        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)
        val language = appPreferences?.getString(AppConstants.LANGUAGE)
        val pInfo = this.packageManager.getPackageInfo(packageName, 0)
        version = pInfo.versionName
//        binding?.version?.text = "Version " + BuildConfig.VERSION_NAME
        if (language?.equals("")!!) {
            appPreferences?.saveString(AppConstants.LANGUAGE, "English")

        }
        appPreferences?.saveInt(AppConstants.WARDUUID, 0)
        appPreferences?.saveInt(AppConstants.FACILITY_UUID, 0)
        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)

        if (language.equals("Tamil")) {

            val Str_sign: String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.signin, this)!!
            binding?.sigintitle?.text = Str_sign
            val Str_sign_button: String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.signinbutton, this)!!
            binding?.siginbutton?.text = Str_sign_button
            val Str_forgetpassword: String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.forgot, this)!!
            binding?.forgetpassword?.text = Str_forgetpassword

        } else if (language.equals("English")) {
            val Str_sign: String =
                utils?.getLocaleStringResource(Locale("en"), R.string.signin, this)!!
            val Str_sign_button: String =
                utils?.getLocaleStringResource(Locale("en"), R.string.signinbutton, this)!!
            binding?.siginbutton?.text = Str_sign_button
            binding?.sigintitle?.text = Str_sign
        }
        AppConstants.LANGUAGE
/*        viewModel!!.errorText.observe(this, Observer { toastMessage ->
            utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        viewModel!!.progress.observe(this,
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
         })
 */
        binding?.click?.setOnClickListener {
            val encryptvalue = Utils.encrypt(viewModel?.password?.value)


            Log.e("username",""+viewModel?.username?.value)
            Log.e("pwd",""+encryptvalue)
            viewModel?.onLoginClicked(encryptvalue)
        }
        binding?.termsAndCndition?.setOnClickListener {
            loadTermsAndCondtions()

        }
        binding?.termsAndCndition1?.setOnClickListener {
            loadTermsAndCondtions()

        }
        binding?.termsAndCndition2?.setOnClickListener {
            loadTermsAndCondtions()

        }
        binding?.tvPrivacyPolicy?.setOnClickListener {
            loadPrivacyPolicy()

        }
        binding?.tvPrivacyPolicy1?.setOnClickListener {
            loadPrivacyPolicy()

        }
        binding?.tvPrivacyPolicy2?.setOnClickListener {
            loadPrivacyPolicy()

        }


        binding!!.userName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                /*if(datasize == 25){
                    binding!!.userName.error="Please enter Maximum 25 characters"
                }
                else*/

                if (datasize >= 3) {
                    binding!!.userName.error = null
                } else {
                    binding!!.userName.error = "Please enter minimum 3 characters"
                }
            }
        })
        binding!!.passwordEdittext.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize >= 6) {
                    binding!!.passwordEdittext.error = null
                } else {
                    binding!!.passwordEdittext.error = "Please enter minimum 6 chracters"
                }
            }
        })

        binding!!.newPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize >= 6) {
                    binding!!.newPasswordEditText.error = null
                } else {
                    binding!!.newPasswordEditText.error = "Please enter minimum 6 chracters"
                }
            }
        })

        binding!!.confirmNewPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize >= 6) {
                    binding!!.confirmNewPasswordEditText.error = null
                } else {
                    binding!!.confirmNewPasswordEditText.error = "Please enter minimum 6 chracters"
                }
            }
        })

        binding?.passwordEdittext?.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            binding?.passwordEdittext?.clearFocus()
            binding?.passwordEdittext?.requestFocus()
            val m: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding?.passwordEdittext?.right!! - binding?.passwordEdittext?.compoundDrawables
                        ?.get(DRAWABLE_RIGHT)?.bounds?.width()!!
                ) {

                    if (this.enableeye!!) {
                        binding?.passwordEdittext?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye__open,
                            0
                        )
                        enableeye = false
                        binding?.passwordEdittext?.inputType =
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                    } else {
                        binding?.passwordEdittext?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye_close,
                            0
                        )
                        enableeye = true
                        binding?.passwordEdittext?.inputType = InputType.TYPE_CLASS_TEXT or
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }

                    return@OnTouchListener true
                }
            }
            false
        })




        binding?.newPasswordEditText?.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            binding?.newPasswordEditText?.clearFocus()
            binding?.newPasswordEditText?.requestFocus()
            val m: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding?.newPasswordEditText?.right!! - binding?.newPasswordEditText?.compoundDrawables
                        ?.get(DRAWABLE_RIGHT)?.bounds?.width()!!
                ) {

                    if (this.enableeye!!) {
                        binding?.newPasswordEditText?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye__open,
                            0
                        )
                        enableeye = false
                        binding?.newPasswordEditText?.inputType =
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                    } else {
                        binding?.newPasswordEditText?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_eye_close,
                            0
                        )
                        enableeye = true
                        binding?.newPasswordEditText?.inputType = InputType.TYPE_CLASS_TEXT or
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }

                    return@OnTouchListener true
                }
            }
            false
        })
        binding?.rlLogin?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff: Int =
                binding?.rlLogin?.rootView?.height!! - binding?.rlLogin?.height!!
            if (heightDiff > dpToPx(this@LoginActivity, 250F)) {
                binding?.rlBottomLayout?.visibility = View.GONE
                binding?.rlBottomLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.transparent
                    )
                )
            } else {
                binding?.rlBottomLayout?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                binding?.rlBottomLayout?.visibility = View.VISIBLE
            }
        }
    }


    private fun dpToPx(context: Context, valueInDp: Float): Float {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
    }

/*
    private fun checkForUpdate() {
        val latestAppVersion =
            mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY).toInt()
        if (latestAppVersion > getCurrentVersionCode()) {
            AlertDialog.Builder(this).setTitle("Please Update the App")
                .setMessage("A new version of this app is available. Please update it")
                .setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    navigateToPlayStore()
                }.setCancelable(false).show()
        } else {
//            Toast.makeText(this, "This app is already up to date", Toast.LENGTH_SHORT).show()
        }
    }
*/

    private fun navigateToPlayStore() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                        or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
        }
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    private fun getCurrentVersionCode(): Int {
        try {
            return packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

/*
    val loginRetrofitCallBack = object : RetrofitCallback<LoginResponseModel?> {

        override fun onSuccessfulResponse(response: Response<LoginResponseModel?>) {

            if (response!!.body()!!.statusCode == 200) {

                loginSeassion(response.body()!!.responseContents!!)

                if (response.body()!!.responseContents!!.userDetails!!.first_time_login != null &&
                    response.body()!!.responseContents!!.userDetails!!.first_time_login != false
                )
                {


                    Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()


                } else {

                    viewModel!!.username.value = ""
                    viewModel!!.password.value = ""
                    val userDetailsRoomRepository = UserDetailsRoomRepository(application)
                    userDetailsRoomRepository.insertData(response.body()!!.responseContents?.userDetails!!)

                    val ft = supportFragmentManager.beginTransaction()
                    val dialog = SetPasswordFragment()
                    dialog.show(ft, "Tag")

                }
            } else {


                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    response.body()!!.msg.toString()
                )

            }

        }

        override fun onBadRequest(response: Response<LoginResponseModel?>) {
*/
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*//*

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
*/
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*//*

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
*/
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.unauthorized))
*//*

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
*/
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*//*

            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(this@LoginActivity, s)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
*/
        val loginRetrofitCallBack = object : RetrofitCallback<LoginResponseModel?> {

        override fun onSuccessfulResponse(response: Response<LoginResponseModel?>) {

            if (response!!.body()!!.statusCode == 200) {

                loginSeassion(response.body()!!.responseContents!!)

                if (response.body()!!.responseContents!!.userDetails!!.first_time_login != null &&
                    response.body()!!.responseContents!!.userDetails!!.first_time_login != false
                ) {


                    appPreferences?.saveInt(AppConstants.LAB_UUID, 0)

                    appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)

                    appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID, "")

                    //appPreferences?.saveInt(AppConstants.ROLEUUID,  response.body()?.responseContents?.activityDetails?.get(0)?.uuid!!)

                    module_details = response.body()?.responseContents?.moduleDetails

                    // check emr,lmis, register ,result & nurse desk role
                    val checkEMR =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_EMR }

                    appPreferences?.saveBoolean(AppConstants.EMRCHECK, checkEMR)

                    val checkLIMS =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_LMIS }

                    appPreferences?.saveBoolean(AppConstants.LMISCHECK, checkLIMS)

                    val checkREGISTER =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_Registration }

                    appPreferences?.saveBoolean(AppConstants.REGISTRATIONCHECK, checkREGISTER)

                    val roleLab =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_LABREPORT }

                    appPreferences?.saveBoolean(AppConstants.LABREPORTS, roleLab)


                    val checkReport =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.LABREPORTSCODE }

                    appPreferences?.saveBoolean(AppConstants.LABREPORTS, checkReport)

                    val CheckNurse =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_NURSE }

                    appPreferences?.saveBoolean(AppConstants.CHECK_NURSE, CheckNurse)

                    val CheckHelpdesk =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_HELPDESK }

                    appPreferences?.saveBoolean(AppConstants.CHECK_HELPDESK, CheckHelpdesk)


                    val CheckTutorial =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_TUTORIAL }

                    appPreferences?.saveBoolean(AppConstants.CHECK_TUTORIAL, CheckTutorial)

                    //ip mangement

                    val CheckIpMangement =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_IPMANAGEMENT }

                    appPreferences?.saveBoolean(AppConstants.CHECK_IPMANAGEMENT, CheckIpMangement)

                    // PhARMACY
                    val CheckPharmacy =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_PHARMASY }

                    appPreferences?.saveBoolean(AppConstants.CHECK_PHARMASY, CheckPharmacy)

                    //RMIS

                    val CheckRMIS =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_RMIS }

                    appPreferences?.saveBoolean(AppConstants.CHECK_RMIS, CheckRMIS)


                    val Checkbedreport =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.ROLE_BEDREPORT }

                    appPreferences?.saveBoolean(AppConstants.CHECK_BEEDREPORT, Checkbedreport)

                    val CheckRegreport =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.REGISTRATIONREPOERTCODE }

                    appPreferences?.saveBoolean(
                        AppConstants.CHECKREGISTRATIONREPOERT,
                        CheckRegreport
                    )


                    val CheckIpreport =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.IPADMISSIONREPORTCODE }

                    appPreferences?.saveBoolean(AppConstants.CHECKIPADMISSIONREPORT, CheckIpreport)


                    val CheckAdmission =
                        response.body()?.responseContents?.moduleDetails!!.any { it!!.code == AppConstants.APPLICATIONMANGERCODE }

                    appPreferences?.saveBoolean(AppConstants.APPLICATIONMANGERCHECK, CheckAdmission)


                    val userDetailsRoomRepository = UserDetailsRoomRepository(application)
                    userDetailsRoomRepository.insertData(response.body()!!.responseContents?.userDetails!!)

                    val landString =
                        response.body()?.responseContents?.activityDetails?.get(0)!!.activity!!.code

                    appPreferences?.saveString(AppConstants.LANDSCREEN, landString)


                    val landurl =
                        response.body()?.responseContents?.activityDetails?.get(0)!!.activity!!.route_url

                    appPreferences?.saveString(AppConstants.LANDURL, landurl)

                    // load Activity
                    for (i in module_details!!.indices) {

                        if (module_details!![i]?.code == AppConstants.ROLE_Registration) {

                            val details = module_details!![i]!!.role_activities

                            val checkcovid =
                                details!!.any { it!!.activity.code == AppConstants.COVIDREGISTERCODE }

                            appPreferences?.saveBoolean(AppConstants.COVIDREGISTER, checkcovid)


                            val checkQuick =
                                details.any { it!!.activity.code == AppConstants.QUICKREGISTERCODE }

                            appPreferences?.saveBoolean(AppConstants.QUICKREGISTER, checkQuick)
                        }
                        if (module_details!![i]?.code == AppConstants.ROLE_LMIS) {

                            val details = module_details!![i]!!.role_activities


                            val checkLabTest =
                                details!!.any { it!!.activity.code == AppConstants.LABTESTCODE }

                            appPreferences?.saveBoolean(AppConstants.LABTEST, checkLabTest)

                            val checkLabApprovel =
                                details.any { it!!.activity.code == AppConstants.LABAPPROVELCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.LABAPPROVEL,
                                checkLabApprovel
                            )

                            val checkLabProcess =
                                details.any { it!!.activity.code == AppConstants.LABTESTCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.LABPROCESS,
                                checkLabProcess
                            )

                            val checkLabneworder =
                                details.any { it!!.activity.code == AppConstants.LABNEWORDERCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.LABNEWORDER,
                                checkLabneworder
                            )


                            val checkLabsampledispatch =
                                details.any { it!!.activity.code == AppConstants.LABSAMPLEDISPATCHCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.LABSAMPLEDISPATCH,
                                checkLabsampledispatch
                            )

                            val checkLabresultdispatch =
                                details.any { it!!.activity.code == AppConstants.LABRESULTDISPATCHCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.LABRESULTDISPATCH,
                                checkLabresultdispatch
                            )


                            val checkOrderStatus =
                                details.any { it!!.activity.code == AppConstants.LABORDERSTATUSCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.LABORDERSTATUS,
                                checkOrderStatus
                            )


                        }
                        if (module_details!![i]?.code == AppConstants.REPORTCODE) {

                            val details = module_details!![i]!!.role_activities

                            var checkconsildatedReports =
                                details!!.any { it!!.activity.code == AppConstants.REPORTCONSOLIDATEDREPORTCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.REPORTCONSOLIDATEDREPORT,
                                checkconsildatedReports
                            )

                            var checkdistrictWiseReport = true
                            // details!!.any { it!!.activity.code == AppConstants.REPORTDISTRICTWISEPATIENTCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.REPORTDISTRICTWISEPATIENTCODE,
                                checkdistrictWiseReport
                            )


                            var checkdistrictWiseTestResport = true
                            // details!!.any { it!!.activity.code == AppConstants.REPORTDISTRICTWISETESTCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.REPORTDISTRICTWISETESTCODE,
                                checkdistrictWiseTestResport
                            )


                            /*       var checkconsildatedReports =
                                       details!!.any { it!!.activity.code == AppConstants.REPORTCONSOLIDATEDREPORTCODE }

                                   appPreferences?.saveBoolean(
                                       AppConstants.REPORTCONSOLIDATEDREPORT,
                                       checkconsildatedReports
                                   )
                                   var checkdistrictwiseReports =
                                       details!!.any { it!!.activity.code == AppConstants.REPORTDISTRICTWISEPATIENTCODE }

                                   appPreferences?.saveBoolean(
                                       AppConstants.REPORTDISTRICTWISEPATIENTCODE,
                                       checkdistrictwiseReports
                                   )

                                   var checkdistrictWiseTestResport =
                                       details!!.any { it!!.activity.code == AppConstants.REPORTDISTRICTWISETESTCODE }

                                   appPreferences?.saveBoolean(
                                       AppConstants.REPORTDISTRICTWISETESTCODE,
                                       checkdistrictWiseTestResport
                                   )


                                   var checkLabWiseReports =
                                       details!!.any { it!!.activity.code == AppConstants.REPORTLABWISEREPORTCODE }

                                   appPreferences?.saveBoolean(
                                       AppConstants.REPORTLABWISEREPORT,
                                       checkLabWiseReports
                                   )

                                   var checkLabTestWiseReports =
                                       details!!.any { it!!.activity.code == AppConstants.REPORTLABTESTWISEREPORTCODE }

                                   appPreferences?.saveBoolean(
                                       AppConstants.REPORTLABTESTWISEREPORT,
                                       checkLabTestWiseReports
                                   )
       */
                        }

                        if (module_details!![i]?.code == AppConstants.ROLE_IPMANAGEMENT) {

                            val details = module_details!![i]!!.role_activities

                            val checkDashboard =
                                details!!.any { it!!.activity.code == AppConstants.ACTIVITY_IP_DASHBOARD }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_IP_DASHBOARD,
                                checkDashboard
                            )


                            val checkAdmission =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_IP_ADMISSION }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_IP_ADMISSION,
                                checkAdmission
                            )


                            val checkWardMaster =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_IP_WARD_MASTER }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_IP_WARD_MASTER,
                                checkWardMaster
                            )


                            val checkedpl =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_IP_ECPL }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_IP_ECPL,
                                checkedpl
                            )
                        }

                        if (module_details!![i]?.code == AppConstants.APPLICATIONMANGERCHECK) {

                            val details = module_details!![i]!!.role_activities

                            val checkDashboard =
                                details!!.any { it!!.activity.code == AppConstants.APPLICATIONDASHBOARDCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.APPLICATIONDASHBOARDCHECK,
                                checkDashboard
                            )


                            val checkHealth =
                                details.any { it!!.activity.code == AppConstants.HEALTHOFFICECODE }

                            appPreferences?.saveBoolean(
                                AppConstants.HEALTHOFFICECHECK,
                                checkHealth
                            )


                            val checkDepartment =
                                details.any { it!!.activity.code == AppConstants.DEPARTMENTCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.DEPARTMENTCHECK,
                                checkDepartment
                            )


                            val checkInstution =
                                details.any { it!!.activity.code == AppConstants.INSTUTIONCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.INSTUTIONCHECK,
                                checkInstution
                            )
                        }


                        if (module_details!![i]?.code == AppConstants.REGISTRATIONREPOERTCODE) {

                            val details = module_details!![i]!!.role_activities

                            val checkSessionWise =
                                details!!.any { it!!.activity.code == AppConstants.REGSESSIONWISECODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKREGSESSIONWISE,
                                checkSessionWise
                            )


                            val checkDateWise =
                                details.any { it!!.activity.code == AppConstants.REGDATEWISECODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKREGDATEWISE,
                                checkDateWise
                            )


                            val checkDateWiseSession =
                                details.any { it!!.activity.code == AppConstants.REGDATEWISESESSIONCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKREGDATEWISESESSION,
                                checkDateWiseSession
                            )


                            val checkDayWisePatientList =
                                details.any { it!!.activity.code == AppConstants.REGDAYWISEPATIENTLISTCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKREGDAYWISEPATIENTLIST,
                                checkDayWisePatientList
                            )
                        }


                        if (module_details!![i]?.code == AppConstants.IPADMISSIONREPORTCODE) {

                            val details = module_details!![i]!!.role_activities

                            val checkIpadmissiondwp =
                                details!!.any { it!!.activity.code == AppConstants.IPADMISSIONADMISSIONDWPCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKIPADMISSIONADMISSIONDWP,
                                checkIpadmissiondwp
                            )


                            val checkWardWise =
                                details.any { it!!.activity.code == AppConstants.IPADMISSIONADMISSIONWWCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKIPADMISSIONADMISSIONWW,
                                checkWardWise
                            )


                            val checkDoctorWiseSession =
                                details.any { it!!.activity.code == AppConstants.IPADMISSIONADMISSIONDWCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKIPADMISSIONADMISSIODW,
                                checkDoctorWiseSession
                            )


                            val checkStateWise =
                                details.any { it!!.activity.code == AppConstants.IPADMISSIONADMISSIONSLCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKIPADMISSIONADMISSIONSL,
                                checkStateWise
                            )

                            val checkDistictWise =
                                details.any { it!!.activity.code == AppConstants.IPADMISSIONADMISSIONDLCODE }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKIPADMISSIONADMISSIONDL,
                                checkDistictWise
                            )

                            val checkDischargeReportCountwise =
                                details.any { it!!.activity.code == AppConstants.IPADMISSIONADMISSIONDC }

                            appPreferences?.saveBoolean(
                                AppConstants.CHECKIPADMISSIONADMISSIONDC,
                                checkDischargeReportCountwise
                            )
                        }


                        if (module_details!![i]?.code == AppConstants.ROLE_PHARMASY) {

                            val details = module_details!![i]!!.role_activities

                            val checkDashboard =
                                details!!.any { it!!.activity.code == AppConstants.ACTIVITY_PHY_DASHBOARD }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_PHY_DASHBOARD,
                                checkDashboard
                            )

                        }

                        if (module_details!![i]?.code == AppConstants.ROLE_BEDREPORT) {

                            val details = module_details!![i]!!.role_activities

                            val checkbedstatus =
                                details!!.any { it!!.activity.code == AppConstants.ACTIVITY_BEDSTHW }

                            appPreferences?.saveBoolean(AppConstants.CHECK_BEDSTHW, checkbedstatus)

                        }


                        if (module_details!![i]?.code == AppConstants.ROLE_RMIS) {

                            val details = module_details!![i]!!.role_activities


                            val checkLabTest =
                                details!!.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TEST }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TEST,
                                checkLabTest
                            )

                            val checkLabApprovel =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TESTAPPROVAL }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TESTAPPROVAL,
                                checkLabApprovel
                            )

                            val checkLabProcess =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TESTPROCESS }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TESTPROCESS,
                                checkLabProcess
                            )

                            val checkLabneworder =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_TECH }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_TECH,
                                checkLabneworder
                            )

                            val checkLabsampledispatch =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_DISPATCH }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_DISPATCH,
                                checkLabsampledispatch
                            )

                            val checkOrderStatus =
                                details.any { it!!.activity.code == AppConstants.ACTIVITY_RMIS_ORDER }

                            appPreferences?.saveBoolean(
                                AppConstants.ACTIVITY_CHECK_RMIS_ORDER,
                                checkOrderStatus
                            )


                        }
                    }


                    /* facilty check
 */
                    if ((checkEMR || checkLIMS) || (checkREGISTER || roleLab) || (CheckNurse || CheckHelpdesk) ||
                        (CheckIpMangement || CheckPharmacy) || (CheckRMIS || CheckIpreport) || Checkbedreport
                    ) {

                        if (module_details?.size!! > 0) {

                            val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

                            Log.i("", "" + userDataStoreBean?.user_type?.revision)

                            if (response.body()!!.responseContents?.userDetails!!.user_type!!.code != null
                                && response.body()!!.responseContents?.userDetails!!.user_type!!.code != ""
                            ) {

                                type =
                                    response.body()!!.responseContents?.userDetails!!.user_type!!.code!!

                                appPreferences?.saveString(AppConstants.LOGINTYPE, type)

                            } else {

                                type =
                                    response.body()!!.responseContents?.userDetails!!.user_type!!.name!!

                                appPreferences?.saveString(AppConstants.LOGINTYPE, type)

                            }
//                            type= "Lab Incharge"


                            if (response.body()!!.responseContents?.userDetails?.office_user == null ||
                                response.body()!!.responseContents?.userDetails?.office_user == false
                            ) {

                                viewModel!!.getfacilityCallback(
                                    response.body()!!.responseContents?.userDetails?.uuid,
                                    facilitycallbackRetrofitCallback
                                )
                            } else {

                                viewModel!!.getOfficeList(officeRetrofitCallBack)


                            }
                        }
                    } else {

                        utils?.showToast(
                            R.color.negativeToast,
                            binding?.mainLayout!!,
                            "Invalid User"
                        )

                    }

                } else {

                    viewModel!!.username.value = ""
                    viewModel!!.password.value = ""
                    val userDetailsRoomRepository = UserDetailsRoomRepository(application)
                    userDetailsRoomRepository.insertData(response.body()!!.responseContents?.userDetails!!)

                    val ft = supportFragmentManager.beginTransaction()
                    val dialog = SetPasswordFragment()
                    dialog.show(ft, "Tag")

                }
            } else {


                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    response.body()!!.msg.toString()
                )

            }

        }

        override fun onBadRequest(response: Response<LoginResponseModel?>) {
//            AnalyticsManager.getAnalyticsManager()
//                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
//            AnalyticsManager.getAnalyticsManager()
//                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
//            AnalyticsManager.getAnalyticsManager()
//                .trackLoginFailed(this@LoginActivity, getString(R.string.unauthorized))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
//            AnalyticsManager.getAnalyticsManager()
//                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(s: String?) {
            if (s != null) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(this@LoginActivity, s)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    private fun loginSeassion(responseContents: LoginResponseContents) {

        var sessionRequest: LoginSessionRequest = LoginSessionRequest()

        sessionRequest.ApplicationId = 10

        sessionRequest.Password = responseContents.userDetails?.SessionId

        sessionRequest.LoginId = responseContents.userDetails?.uuid.toString()

        sessionRequest.Logininfo = responseContents.userDetails?.first_name

        sessionRequest.RoleInfo = responseContents.userDetails?.role?.role_name
        sessionRequest.UserName = responseContents.userDetails?.first_name
        sessionRequest.UserTypeId = responseContents.userDetails?.user_type_uuid.toString()


        if (Utils.isNetworkConnected(application)) {
            // Internet available
            if (Utils.isConnectedWifi(application)) {
                // WIFI


                val wm =
                    applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val ipAddress: String =
                    BigInteger.valueOf(wm.dhcpInfo.netmask.toLong()).toString()

                sessionRequest.IPAddress = ipAddress

            }
            // Internet available
            if (Utils.isConnectedInternet(application)) {
                // MOBILE DATA
                sessionRequest.IPAddress = utils?.getIPAddress(true)
            }
        }


        viewModel?.Loginseassion(responseContents, sessionRequest, LoginSeasionRetrofitCallBack)


    }


    val LoginSeasionRetrofitCallBack = object : RetrofitCallback<SimpleResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel?>) {

            if (responseBody?.body()?.statusCode == 200) {


            }

        }

        override fun onBadRequest(response: Response<SimpleResponseModel?>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
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
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    val officeRetrofitCallBack =
        object : RetrofitCallback<OfficeResponseModel?> {
            override fun onSuccessfulResponse(response: Response<OfficeResponseModel?>) {
                Log.i("", "" + response.body())
                if (response.body()?.responseContents!!.isNotEmpty()) {

                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, 0)
                    appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
//                    startActivity(Intent(this@LoginActivity, MainLandScreenActivity::class.java))
                    finish()


                }

            }

            override fun onBadRequest(response: Response<OfficeResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
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


    val otpRetrofitCallBack = object : RetrofitCallback<ChangePasswordOTPResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<ChangePasswordOTPResponseModel?>) {

            if (responseBody?.body()?.status == "success") {

                Log.e("SendOTP", responseBody.body()?.toString()!!)
                viewModel!!.forgetUsernemeLayout.value = 8
                viewModel!!.changePasswordLayout.value = 0

              /*  viewModel!!.otp.value = responseBody?.body()?.responseContents?.otp

                viewModel!!.otp.value = responseBody?.body()?.responseContents?.otp
*/
                Toast.makeText(this@LoginActivity, responseBody.body()?.msg, Toast.LENGTH_LONG)
                    .show()
                //binding?.otpLayout!!.visibility = View.GONE
            }

        }

        override fun onBadRequest(response: Response<ChangePasswordOTPResponseModel?>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
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
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val changePasswordRetrofitCallBack = object : RetrofitCallback<PasswordChangeResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<PasswordChangeResponseModel?>) {
            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.msg!!
            )
            viewModel!!.visisbleLogin()

            Toast.makeText(this@LoginActivity, responseBody?.body()?.msg!!, Toast.LENGTH_LONG)
                .show()


        }

        override fun onBadRequest(response: Response<PasswordChangeResponseModel?>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
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
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


    val facilitycallbackRetrofitCallback =
        object : RetrofitCallback<SurgeryInstitutionResponseModel?> {

            override fun onSuccessfulResponse(responseBody: Response<SurgeryInstitutionResponseModel?>) {

                Log.i("", "" + responseBody?.body()?.responseContents)

                if (responseBody?.body()?.responseContents?.size!! > 1) {
                    if (type == AppConstants.LABINCHARGE) {
                        val ft = supportFragmentManager.beginTransaction()

                        val dialog = SelectInstituteDialogFragment()

                        dialog.show(ft, "Tag")
                    } else if ((type == AppConstants.PHYSICIANASSISTANT || type == AppConstants.PHYSICIANTRAINEE) ||
                        (type == AppConstants.HELPDESK || type == AppConstants.GENDRALCODE)
                    ) {
                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = DoctorInstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    } else if (type == AppConstants.NURSELOGIN) {
                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = NurseInstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    } else if (type == AppConstants.PHARMACIST) {
                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = PharmacistInstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    } else if (type == AppConstants.RADINCHARGE) {
                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = RadiologyInstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    } else {
                        val ft = supportFragmentManager.beginTransaction()

                        val dialog = DoctorInstituteDialogFragment()
                        //   val dialog = InstituteDialogFragment()
                        dialog.show(ft, "Tag")
                    }
                } else if (responseBody.body()?.responseContents?.size!! == 1) {
                    facilitylevelID = responseBody.body()?.responseContents?.get(0)?.facility_uuid
                    facilityUserID = responseBody.body()?.responseContents?.get(0)!!.user_uuid
                    appPreferences?.saveString(
                        AppConstants.INSTITUTION_NAME,
                        responseBody.body()?.responseContents?.get(0)!!.facility?.name
                    )
                    viewModel?.getDepartmentList(facilitylevelID, facilityUserID, depatmentCallback)
                } else {

                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.Instutionmaperror)
                    )
                }
            }

            override fun onBadRequest(response: Response<SurgeryInstitutionResponseModel?>) {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(this@LoginActivity, getString(R.string.unauthorized))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
//                AnalyticsManager.getAnalyticsManager()
//                    .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                if (s != null) {
//                    AnalyticsManager.getAnalyticsManager().trackLoginFailed(this@LoginActivity, s)
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    /*
    Department
     */

    val depatmentCallback = object : RetrofitCallback<DepartmentResponseModel?> {
        override fun onSuccessfulResponse(responseBody: Response<DepartmentResponseModel?>) {
            Log.i("", "" + responseBody?.body()?.responseContents)
            var jsonresponse = Gson().toJson(responseBody?.body()?.responseContents)
            Log.i("", "" + jsonresponse)
            Log.i("", "" + jsonresponse)
            Log.i("", "" + jsonresponse)
            Log.i("", "" + jsonresponse)

            if (responseBody?.body()?.responseContents?.size!! > 1) {

                if (type == AppConstants.LABINCHARGE) {

                    val ft = supportFragmentManager.beginTransaction()

                    val dialog = SelectInstituteDialogFragment()

                    dialog.show(ft, "Tag")
                } else if ((type == AppConstants.PHYSICIANASSISTANT || type == AppConstants.PHYSICIANTRAINEE)
                    || (type == AppConstants.HELPDESK || type == AppConstants.GENDRALCODE)
                ) {

                    val ft = supportFragmentManager.beginTransaction()

                    //need to implement

                    val dialog = DoctorInstituteDialogFragment()

                    dialog.show(ft, "Tag")

                } else if (type == AppConstants.NURSELOGIN) {

                    val ft = supportFragmentManager.beginTransaction()

                    val dialog = NurseInstituteDialogFragment()

                    dialog.show(ft, "Tag")

                } else if (type == AppConstants.PHARMACIST) {
                    val ft = supportFragmentManager.beginTransaction()

                    //need to implement
                    val dialog = PharmacistInstituteDialogFragment()
                    dialog.show(ft, "Tag")
                } else if (type == AppConstants.RADINCHARGE) {
                    val ft = supportFragmentManager.beginTransaction()
                    val dialog = RadiologyInstituteDialogFragment()
                    dialog.show(ft, "Tag")
                } else {

                    val ft = supportFragmentManager.beginTransaction()

                    //   val dialog = InstituteDialogFragment()

                    //need to implement
                    val dialog = DoctorInstituteDialogFragment()

                    dialog.show(ft, "Tag")

                }
            } else if (responseBody.body()?.responseContents?.size!! == 1) {

                facilitylevelID = responseBody.body()?.responseContents?.get(0)?.facility_uuid

                departmentID = responseBody.body()?.responseContents?.get(0)?.department_uuid

                var departmentName = responseBody.body()?.responseContents?.get(0)?.department?.name

                if (type == AppConstants.LABINCHARGE) {

                 /*   viewModel!!.getLocationMaster(
                        facilitylevelID!!, LocationMasterResponseCallback
                    )*/

                } else if (type == AppConstants.NURSELOGIN) {

                    viewModel!!.getWardList(
                        facilitylevelID!!, WardRetrofitCallBack
                    )

                } else {

                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)
                    appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, departmentID!!)

                    appPreferences?.saveString(AppConstants.DEPARTMENT_NAME, departmentName!!)
//                    startActivity(Intent(this@LoginActivity, MainLandScreenActivity::class.java))
                    finish()

                }


            } else {

                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.Departmantmaperror)
                )
            }


        }

        override fun onBadRequest(response: Response<DepartmentResponseModel?>) {
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*/
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onServerError(response: Response<*>?) {
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
*/
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
/*
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.unauthorized))
*/
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
            if (s != null) {
//                AnalyticsManager.getAnalyticsManager().trackLoginFailed(this@LoginActivity, s)
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

/*
    val LocationMasterResponseCallback = object : RetrofitCallback<LocationMasterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModel>?) {
            Log.i("", "locationdata" + responseBody!!.body()!!.responseContents)
            val data = responseBody.body()!!.responseContents
            if (data.isNotEmpty()) {

                if (responseBody.body()?.responseContents?.size!! > 1) {

                    val ft = supportFragmentManager.beginTransaction()
                    val dialog = SelectInstituteDialogFragment()
                    dialog.show(ft, "Tag")

                } else {

                    val labUUID: Int = responseBody.body()!!.responseContents[0].uuid

                    val LabdepartmentId =
                        responseBody.body()!!.responseContents[0].department_uuid

                    appPreferences?.saveInt(AppConstants.FACILITY_UUID, facilitylevelID!!)

                    appPreferences?.saveInt(AppConstants.LAB_UUID, labUUID)

                    if (LabdepartmentId != null) {

                        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, LabdepartmentId)

                    } else {

                        appPreferences?.saveInt(AppConstants.DEPARTMENT_UUID, 0)
                    }

                    var tolocationMap =
                        responseBody.body()!!.responseContents[0].to_location_department_maps

                    if (tolocationMap.isNotEmpty()) {

                        var otherdepaertment: ArrayList<Int> = ArrayList()

                        for (i in tolocationMap.indices) {

                            otherdepaertment.add(tolocationMap[i].department_uuid)

                        }

                        val res = otherdepaertment.toString()

                        Log.i("department", "" + res)

                        appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID, res)
                    }

                    AnalyticsManager.getAnalyticsManager().trackLoginSuccess(this@LoginActivity)


                    startActivity(Intent(this@LoginActivity, MainLandScreenActivity::class.java))

                    finish()
                }

            }

        }

        override fun onBadRequest(errorBody: Response<LocationMasterResponseModel>?) {
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, "Bad Request")
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LocationMasterResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {

            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.unauthorized))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            AnalyticsManager.getAnalyticsManager()
                .trackLoginFailed(this@LoginActivity, getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            if (failure != null) {
                AnalyticsManager.getAnalyticsManager()
                    .trackLoginFailed(this@LoginActivity, failure)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)

            }
        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8

        }
    }
*/

    val WardRetrofitCallBack = object : RetrofitCallback<WardListResponseModel?> {
            override fun onSuccessfulResponse(responseBody: Response<WardListResponseModel?>) {
                val data = responseBody.body()!!.responseContents!! as ArrayList<Ward>
                if (data.isNotEmpty()) {

                    if (responseBody.body()?.responseContents?.size!! > 1) {

                        val ft = supportFragmentManager.beginTransaction()
                        val dialog = NurseInstituteDialogFragment()
                        dialog.show(ft, "Tag")

                    } else {


                        departmentID = data[0].department_uuid

                        appPreferences!!.saveInt(AppConstants.DEPARTMENT_UUID, departmentID!!)

                        var ward_uuid = data[0].ward_uuid

                        appPreferences!!.saveInt(AppConstants.WARDUUID, ward_uuid!!)


//                        AnalyticsManager.getAnalyticsManager().trackLoginSuccess(this@LoginActivity)


/*
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                MainLandScreenActivity::class.java
                            )
                        )
*/

                        finish()
                    }

                }

            }

            override fun onBadRequest(response: Response<WardListResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
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
                if (failure != null) {
                    utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun loadTermsAndCondtions() {
        /*val ft = (this)!!.supportFragmentManager.beginTransaction()
        val dialog = TremsAndConditionActivity()
        dialog.show(ft, "Tag")*/
        val intent = Intent(this@LoginActivity, TermsAndConditionActivity::class.java)
        intent.putExtra("isTermsCondition", true)
        startActivity(intent)
    }

    private fun loadPrivacyPolicy() {
        /*val ft = (this)!!.supportFragmentManager.beginTransaction()
        val dialog = TremsAndConditionActivity()
        dialog.show(ft, "Tag")*/
        val intent = Intent(this@LoginActivity, TermsAndConditionActivity::class.java)
        intent.putExtra("isTermsCondition", false)
        startActivity(intent)
    }

}