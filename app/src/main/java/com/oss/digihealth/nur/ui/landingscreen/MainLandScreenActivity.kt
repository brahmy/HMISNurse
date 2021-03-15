package com.oss.digihealth.nur.ui.landingscreen

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.R
import com.oss.digihealth.nur.callbacks.FragmentBackClick
import com.oss.digihealth.nur.callbacks.RetrofitCallback
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import com.oss.digihealth.nur.databinding.ActivityMainLandScreenBinding
import com.oss.digihealth.nur.ui.dashboard.view.DashBoardActivity
import com.oss.digihealth.nur.ui.dashboard.view.LanguagesDialogFragemnt
import com.oss.digihealth.nur.ui.institute.view.*
import com.oss.digihealth.nur.ui.login.model.SimpleResponseModel
import com.oss.digihealth.nur.ui.login.model.UserDetailsRoomRepository
import kotlinx.android.synthetic.main.activity_main_land_screen.*
import kotlinx.android.synthetic.main.land_layout.view.*
import kotlinx.android.synthetic.main.navigation_layout.*
import retrofit2.Response
import java.util.*

class MainLandScreenActivity : AppCompatActivity(),ManageInstituteDialogFragment.DialogListener,
    LanguagesDialogFragemnt.OnLanguageProcessListener,
    FragmentBackClick {

    private var backPressed: Long = 0
    var binding: ActivityMainLandScreenBinding? = null

    private var viewModel: MainLandViewModel? = null

    var appPreferences: AppPreferences? = null

    var name:String?=null

    var emrcheck:Boolean?=null
    var tutorialcheck:Boolean?=null
    var helpDeskcheck:Boolean?=null
    private var customdialog: Dialog?=null
    var registercheck:Boolean?=null
    var nurseDeskCheck:Boolean?= null

    var helpDeskCheck:Boolean?=null
    var LmisCheck:Boolean?=null

    var labreportCheck:Boolean?=null

    var tutorialCheck:Boolean?=null

    var rmisCheck:Boolean?=null

    var ipCheck:Boolean?=null

    var appMasterCheck:Boolean?=null

    var pharmacyCheck:Boolean?=null

    var bedReportCheck:Boolean?=null

    var pharmasyLayout:LinearLayout?=null

    var regReportCheck:Boolean?=null

    var ipAdmissionReportCheck:Boolean?=null

    private var utils: Utils? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var emrStartDate: String = ""
    var emrEndDate: String = ""
    var emrGender: String = ""
    var emrSession: String = ""

    private var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_land_screen)

        if (Utils(this).isTablet(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_land_screen)

        viewModel = MainLandViewModelFactory(
            application
        ).create(MainLandViewModel::class.java)

        binding?.viewModel = viewModel

        binding?.lifecycleOwner = this

        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)
        setSupportActionBar(homeLayoutInclude?.toolbar)


        val toggle = object :
            ActionBarDrawerToggle(
                this,
                drawerLayout, homeLayoutInclude?.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {}
        drawerLayout?.addDrawerListener(toggle)

        toggle.toolbarNavigationClickListener =
            View.OnClickListener { drawerLayout?.openDrawer(GravityCompat.START) }
        homeLayoutInclude?.toolbar?.setNavigationIcon(R.drawable.ic_hamburger_icon)

        pharmasyLayout=findViewById(R.id.pharmacy)

        appMasterCheck= appPreferences?.getBoolean(AppConstants.APPLICATIONMANGERCHECK)

        emrcheck= appPreferences?.getBoolean(AppConstants.EMRCHECK)
        tutorialcheck=true // appPreferences?.getBoolean(AppConstants.TUTORIALCHECK)
        helpDeskcheck= true//appPreferences?.getBoolean(AppConstants.HELPDESKCHECK)
        LmisCheck= appPreferences?.getBoolean(AppConstants.LMISCHECK)
        registercheck= appPreferences?.getBoolean(AppConstants.REGISTRATIONCHECK)
        labreportCheck= appPreferences?.getBoolean(AppConstants.LABREPORTS)

        nurseDeskCheck= appPreferences?.getBoolean(AppConstants.CHECK_NURSE)

        helpDeskCheck= appPreferences?.getBoolean(AppConstants.CHECK_HELPDESK)

        tutorialCheck= appPreferences?.getBoolean(AppConstants.CHECK_TUTORIAL)

        rmisCheck= appPreferences?.getBoolean(AppConstants.CHECK_RMIS)

        ipCheck= appPreferences?.getBoolean(AppConstants.CHECK_IPMANAGEMENT)

        pharmacyCheck= appPreferences?.getBoolean(AppConstants.CHECK_PHARMASY)

        bedReportCheck= appPreferences?.getBoolean(AppConstants.CHECK_BEEDREPORT)

        regReportCheck= appPreferences?.getBoolean(AppConstants.CHECKREGISTRATIONREPOERT)

        ipAdmissionReportCheck= appPreferences?.getBoolean(AppConstants.CHECKIPADMISSIONREPORT)

        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        var departmentname=appPreferences?.getString(AppConstants.DEPARTMENT_NAME)

        userNameTextView?.text = ""+userDataStoreBean?.title?.name+userDataStoreBean?.first_name+"\n"+"($departmentname)"
        utils = Utils(this)
        val language = appPreferences?.getString(AppConstants.LANGUAGE)
        if(language?.equals("Tamil")!!)
        {

            val Str_register: String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.registration, this)!!
            val Str_Quick_reg : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.quickreg, this)!!
            val Str_covidreg : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.covidreg, this)!!


            val Str_lmis : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.lmis, this)!!

            val Str_new_order : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.neworder, this)!!
            val Str_labtest : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labtesttext, this)!!
            val Str_test_process : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.testprocess, this)!!
            val Str_test_approval : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.testapproval, this)!!
            val Str_sample_dispatch : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.samplediaptch, this)!!
            val Str_result_dispatch : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.resultdispath, this)!!
            val Str_orderstatus : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.orderstatus, this)!!
            /////////////
            val Str_labreport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labreport, this)!!

            val Str_consolidate_report : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.consolidatereport, this)!!
            val Str_districtwisepatientreport: String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.districtwisepatientreport, this)!!
            val Str_districtwisetestreport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.districtwisetestreport, this)!!
            val Str_labwisereport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labwisereport, this)!!
            val Str_labtestwisereport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labtestwisereport, this)!!

            ///////
            val Str_labsettings : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.action_settings, this)!!
            val Str_change_password : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.change_password, this)!!
            val Str_languages : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.languages, this)!!

            val Str_logout : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.logout, this)!!

            val Str_emr : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.emr, this)!!

            val Str_tutorial : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.tutorial, this)!!

            val Str_videotutorial : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.videoTutorial, this)!!
            val Str_usermanual : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.userManual, this)!!

            val Str_helpDesk : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.helpDesk, this)!!

            val Str_tickets : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.tickets, this)!!



/*


            binding?.navigationItem?.registrationText!!.text = Str_register
            binding?.navigationItem?.lmistext!!.text = Str_lmis
            binding?.navigationItem?.labreporttext!!.text = Str_labreport
            binding?.navigationItem?.settingstext!!.text = Str_labsettings
            binding?.navigationItem?.logouttext!!.text = Str_logout

            binding?.navigationItem?.quickRegText?.text = Str_Quick_reg
            binding?.navigationItem?.covidregtext?.text = Str_covidreg

            //lmis
            binding?.navigationItem?.newordertext?.text = Str_new_order
            binding?.navigationItem?.labtest?.text = Str_labtest
            binding?.navigationItem?.textprocesstext?.text = Str_test_process
            binding?.navigationItem?.testapprovaltext?.text = Str_test_approval
            binding?.navigationItem?.sampledispathtext?.text = Str_sample_dispatch
            binding?.navigationItem?.resultdispathtext?.text = Str_result_dispatch
            binding?.navigationItem?.orderstatustext?.text = Str_orderstatus
            //

            binding?.navigationItem?.consolidatedReportText?.text = Str_consolidate_report
            binding?.navigationItem?.districtWisePatienReportText?.text = Str_districtwisepatientreport
            binding?.navigationItem?.districtwisetestreporttext?.text = Str_districtwisetestreport
            binding?.navigationItem?.labwisereporttext?.text = Str_labwisereport
            binding?.navigationItem?.labtestwisereporttext?.text = Str_labtestwisereport
//////////////////

            binding?.navigationItem?.changepasswordtext?.text = Str_change_password
            binding?.navigationItem?.languagetext?.text = Str_languages

            binding?.navigationItem?.emrtext?.text = Str_emr

            //tutorial
            binding?.navigationItem?.tutorialtext?.text = Str_tutorial
            binding?.navigationItem?.videoTutorialtext?.text = Str_videotutorial
            binding?.navigationItem?.userManualtext?.text = Str_usermanual

            //Help Desk
            binding?.navigationItem?.helpDesktext?.text = Str_helpDesk
            binding?.navigationItem?.ticketstext?.text = Str_tickets
*/
        }
        else if(language.equals("English"))
        {
            val Str_register: String =
                utils?.getLocaleStringResource(Locale("en"), R.string.registration, this)!!
            val Str_lmis : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.lmis, this)!!
            val Str_labreport : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.labreport, this)!!
            val Str_tutorial : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.tutorial, this)!!
            val Str_helpDesk : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.helpDesk, this)!!
            val Str_labsettings : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.action_settings, this)!!
            val Str_logout : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.logout, this)!!
/*
            binding?.navigationItem?.registrationText!!.text = Str_register
            binding?.navigationItem?.lmistext!!.text = Str_lmis
            binding?.navigationItem?.labreporttext!!.text = Str_labreport
            binding?.navigationItem?.tutorialtext!!.text = Str_tutorial
            binding?.navigationItem?.helpDesktext!!.text = Str_helpDesk
            binding?.navigationItem?.settingstext!!.text = Str_labsettings
            binding?.navigationItem?.logouttext!!.text = Str_logout*/
        }


        languages?.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val dialog = LanguagesDialogFragemnt()
            dialog.show(ft, "Tag")
        }
        if(this.emrcheck!!){

            emr.visibility=View.VISIBLE

        }
        else{

            emr.visibility=View.GONE

        }

        if(this.tutorialcheck!!){
            tutorial.visibility=View.VISIBLE
        }else{
            tutorial.visibility=View.GONE
        }

        if(this.helpDeskcheck!!){
            helpDesk.visibility=View.VISIBLE
        }else{
            helpDesk.visibility=View.GONE
        }

        if(this.LmisCheck!!){

            lab.visibility=View.VISIBLE

        }
        else{

            lab.visibility=View.GONE

        }

        if(this.registercheck!!){

            registration.visibility=View.VISIBLE

        }
        else{

            registration.visibility=View.GONE

        }

        if(this.bedReportCheck!!){

            Bedmangementreport.visibility= View.VISIBLE
        }
        else{

            Bedmangementreport.visibility= View.GONE

        }



        if(this.labreportCheck!!){

            lab_report.visibility=View.VISIBLE


        }
        else{

            lab_report.visibility=View.GONE

        }

        if(this.nurseDeskCheck!!){

            nurse_desk.visibility=View.VISIBLE
        }
        else{

            nurse_desk.visibility=View.GONE
        }

        if(this.helpDeskCheck!!){
            helpDesk.visibility=View.VISIBLE
        }
        else{

            helpDesk.visibility=View.GONE
        }

        if(this.tutorialCheck!!){
            tutorial.visibility=View.VISIBLE
        }
        else{

            tutorial.visibility=View.GONE
        }


        if(this.rmisCheck!!){

            radilogy.visibility=View.VISIBLE

        }
        else{

            radilogy.visibility=View.GONE
        }


        if(this.ipCheck!!){

            ipmangement.visibility=View.VISIBLE

        }
        else{

            ipmangement.visibility=View.GONE
        }


        if(this.regReportCheck!!){

            reg_report.visibility=View.VISIBLE

        }
        else{

            reg_report.visibility=View.GONE
        }


        if(this.pharmacyCheck!!){

            pharmasyLayout?.visibility=View.VISIBLE
        }
        else{

            pharmasyLayout?.visibility=View.GONE
        }


        if(this.ipAdmissionReportCheck!!){

            ipAdmission_report?.visibility=View.VISIBLE
        }
        else{

            ipAdmission_report?.visibility=View.GONE
        }

        if(this.appMasterCheck!!){

            appMaster?.visibility=View.VISIBLE

        }

        else{

            appMaster?.visibility=View.GONE

        }

        emr?.setOnClickListener {


            registerList?.visibility = View.GONE

            lmisList?.visibility=View.GONE

            rmislayout?.visibility=View.GONE

            regreportlayout?.visibility= View.GONE

            ipadmissionreportlayout?.visibility= View.GONE

            labreportLayout?.visibility= View.GONE

            tutorialLayout?.visibility= View.GONE

            helpdesklayout?.visibility= View.GONE

            pharmacylayout?.visibility= View.GONE

            bedreportlayout?.visibility= View.GONE

            ipmanagementlayout?.visibility= View.GONE

            settingLayout?.visibility= View.GONE


            add_image?.setImageResource(R.drawable.ic_add)

            lab_image?.setImageResource(R.drawable.ic_add)

            rad_image?.setImageResource(R.drawable.ic_add)

            reg_report_image?.setImageResource(R.drawable.ic_add)

            admission_report_image?.setImageResource(R.drawable.ic_add)

            lab_report_image?.setImageResource(R.drawable.ic_add)

            tutorial_image?.setImageResource(R.drawable.ic_add)

            helpDesk_image?.setImageResource(R.drawable.ic_add)

            pharmacy_image?.setImageResource(R.drawable.ic_add)

            bedmangementreport_image?.setImageResource(R.drawable.ic_add)

            ipmangement_image?.setImageResource(R.drawable.ic_add)

            settings_image?.setImageResource(R.drawable.ic_add)





            val op=DashBoardActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

            drawerLayout!!.closeDrawer(GravityCompat.START)
        }

        nurse_desk?.setOnClickListener {



            registerList?.visibility = View.GONE

            lmisList?.visibility=View.GONE

            rmislayout?.visibility=View.GONE

            regreportlayout?.visibility= View.GONE

            ipadmissionreportlayout?.visibility= View.GONE

            labreportLayout?.visibility= View.GONE

            tutorialLayout?.visibility= View.GONE

            helpdesklayout?.visibility= View.GONE

            pharmacylayout?.visibility= View.GONE

            bedreportlayout?.visibility= View.GONE

            ipmanagementlayout?.visibility= View.GONE

            settingLayout?.visibility= View.GONE


            add_image?.setImageResource(R.drawable.ic_add)

            lab_image?.setImageResource(R.drawable.ic_add)

            rad_image?.setImageResource(R.drawable.ic_add)

            reg_report_image?.setImageResource(R.drawable.ic_add)

            admission_report_image?.setImageResource(R.drawable.ic_add)

            lab_report_image?.setImageResource(R.drawable.ic_add)

            tutorial_image?.setImageResource(R.drawable.ic_add)

            helpDesk_image?.setImageResource(R.drawable.ic_add)

            pharmacy_image?.setImageResource(R.drawable.ic_add)

            bedmangementreport_image?.setImageResource(R.drawable.ic_add)

            ipmangement_image?.setImageResource(R.drawable.ic_add)

            settings_image?.setImageResource(R.drawable.ic_add)



            val nursedesk= NurseEmrWorkFlowActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, nursedesk)
            fragmentTransaction.commit()

            drawerLayout!!.closeDrawer(GravityCompat.START)

        }

        appMaster?.setOnClickListener {

            if(appMasterList?.visibility== View.GONE){

                appMasterList?.visibility= View.VISIBLE

                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                appMaster_image?.setImageResource(R.drawable.ic_minus_white)

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)

                val appdashboard =
                    appPreferences?.getBoolean(AppConstants.CHECKREGDATEWISESESSION)

                val healthOfficecheck =
                    appPreferences?.getBoolean(AppConstants.CHECKREGDATEWISE)

                val departmentCheak =
                    appPreferences?.getBoolean(AppConstants.CHECKREGDAYWISEPATIENTLIST)

                val instutionCheck =
                    appPreferences?.getBoolean(AppConstants.CHECKREGSESSIONWISE)

                if (appdashboard!!) {

                    appDashBoard?.visibility = View.VISIBLE

                } else {

                    appDashBoard?.visibility = View.GONE
                }

                if (healthOfficecheck!!) {

                    healthoffice?.visibility = View.VISIBLE

                } else {

                    healthoffice?.visibility = View.GONE
                }

                if (departmentCheak!!) {

                    department?.visibility = View.VISIBLE
                } else {
                    department?.visibility = View.GONE
                }

                if(instutionCheck!!){

                    application_institution?.visibility = View.VISIBLE

                }
                else{

                    application_institution?.visibility = View.GONE

                }

            } else {


                appMasterList?.visibility= View.GONE

                appMaster_image?.setImageResource(R.drawable.ic_add)


            }

        }

        registration?.setOnClickListener {

            if (registerList?.visibility == View.GONE) {

                appMasterList?.visibility= View.GONE

                registerList?.visibility = View.VISIBLE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE


                add_image?.setImageResource(R.drawable.ic_minus_white)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)

                val covidCheck=appPreferences?.getBoolean(AppConstants.COVIDREGISTER)

                if(covidCheck!!){

                    covid_reg?.visibility = View.VISIBLE

                }
                else{

                    covid_reg?.visibility = View.GONE
                }

                val quickcheck=appPreferences?.getBoolean(AppConstants.QUICKREGISTER)

                if(quickcheck!!){

                    quick_Reg?.visibility = View.VISIBLE

                }
                else{

                    quick_Reg?.visibility = View.GONE

                }


                detailedRegistration?.visibility=View.VISIBLE



            } else {
                registerList?.visibility = View.GONE
                add_image?.setImageResource(R.drawable.ic_add)


            }
        }


        lab?.setOnClickListener {

            if (lmisList?.visibility == View.VISIBLE) {

                lmisList?.visibility=View.GONE

                lab_image?.setImageResource(R.drawable.ic_add)

            } else {
                registerList?.visibility = View.GONE

                appMasterList?.visibility= View.GONE

                lmisList?.visibility=View.VISIBLE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE


                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_minus_white)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)


                val labtest=appPreferences?.getBoolean(AppConstants.LABTEST)

                if(labtest!!){

                    lab_test?.visibility = View.VISIBLE
                }
                else{

                    lab_test?.visibility = View.GONE
                }

                val labApprovel=appPreferences?.getBoolean(AppConstants.LABAPPROVEL)

                if(labApprovel!!){

                    lab_approvl?.visibility = View.VISIBLE

                }
                else{

                    lab_approvl?.visibility = View.GONE

                }

                val labProcess=appPreferences?.getBoolean(AppConstants.LABPROCESS)

                if(labProcess!!){

                    lab_process?.visibility = View.VISIBLE

                }
                else{

                    lab_process?.visibility = View.GONE

                }

                val labNew=appPreferences?.getBoolean(AppConstants.LABNEWORDER)

                if(labNew!!){

                    new_order.visibility=View.VISIBLE

                }
                else{

                    new_order.visibility=View.GONE
                }

                val labSampleDispatch=appPreferences?.getBoolean(AppConstants.LABSAMPLEDISPATCH)

                if(labSampleDispatch!!){

                    sample_dispatch.visibility=View.VISIBLE

                }
                else{

                    sample_dispatch.visibility=View.GONE
                }


                val labResultDispatch=appPreferences?.getBoolean(AppConstants.LABRESULTDISPATCH)

                if(labResultDispatch!!){

                    result_dispatch.visibility=View.VISIBLE

                }
                else{

                    result_dispatch.visibility=View.GONE
                }

                val labOderStatus=appPreferences?.getBoolean(AppConstants.LABORDERSTATUS)

                if(labOderStatus!!){

                    order_status.visibility=View.VISIBLE

                }
                else{

                    order_status.visibility=View.GONE
                }


            }
        }


        radilogy?.setOnClickListener {

            if(rmislayout?.visibility==View.GONE){

                appMasterList?.visibility= View.GONE

                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.VISIBLE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_minus_white)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)



                val radapprovlCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TESTAPPROVAL)

                val radNewOrderCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TECH)

                val radTestCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TEST)

                val radProcessCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TESTPROCESS)

                val radResultCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_DISPATCH)

                val radorderCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_ORDER)

                val radDashboard=true//appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_DASHBORD)

                if(radapprovlCheck!!){

                    rad_approvl?.visibility = View.VISIBLE

                }
                else{

                    rad_approvl?.visibility = View.GONE
                }


                if(radNewOrderCheck!!){

                    rad_new_order?.visibility = View.VISIBLE

                }
                else{

                    rad_new_order?.visibility = View.GONE
                }

                if(radDashboard){

                    rad_dashboard?.visibility = View.VISIBLE
                }
                else{
                    rad_dashboard?.visibility = View.GONE
                }

                if(radTestCheck!!){

                    rad_test?.visibility = View.VISIBLE

                }
                else{

                    rad_test?.visibility = View.GONE
                }

                if(radProcessCheck!!){

                    rad_process?.visibility = View.VISIBLE

                }
                else{

                    rad_process?.visibility = View.GONE
                }

                if(radResultCheck!!){
                    rad_result_dispatch?.visibility = View.VISIBLE

                }
                else{

                    rad_result_dispatch?.visibility = View.GONE

                }
                if(radorderCheck!!){

                    rad_order_status?.visibility = View.VISIBLE

                }
                else{

                    rad_order_status?.visibility = View.GONE
                }


            } else {

                rmislayout?.visibility = View.GONE

                rad_image?.setImageResource(R.drawable.ic_add)


            }


        }



        reg_report?.setOnClickListener {

            //      if ((dateWiseSession_report?.visibility == View.GONE && dateWise_report?.visibility == View.GONE) && (sessionWise_report?.visibility == View.GONE && dayWisePatient_report?.visibility == View.GONE)) {

            if(regreportlayout?.visibility== View.GONE){

                appMasterList?.visibility= View.GONE
                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.VISIBLE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_minus_white)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)




                val dateWiseSession =
                    appPreferences?.getBoolean(AppConstants.CHECKREGDATEWISESESSION)

                val dateWise =
                    appPreferences?.getBoolean(AppConstants.CHECKREGDATEWISE)

                val dateWisPatient =
                    appPreferences?.getBoolean(AppConstants.CHECKREGDAYWISEPATIENTLIST)

                val sessionWise =
                    appPreferences?.getBoolean(AppConstants.CHECKREGSESSIONWISE)

                if (dateWiseSession!!) {

                    dateWiseSession_report?.visibility = View.VISIBLE

                } else {

                    dateWiseSession_report?.visibility = View.GONE
                }


                if (dateWise!!) {

                    dateWise_report?.visibility = View.VISIBLE

                } else {

                    dateWise_report?.visibility = View.GONE
                }

                if (sessionWise!!) {

                    sessionWise_report?.visibility = View.VISIBLE
                } else {
                    sessionWise_report?.visibility = View.GONE
                }

                if(dateWisPatient!!){
                    dayWisePatient_report?.visibility = View.VISIBLE

                }
                else{

                    dayWisePatient_report?.visibility = View.GONE

                }

/*
                if (admissionDateWise!!) {

                    admission_day_patient_Wise_report?.visibility = View.VISIBLE
                } else {
                    admission_day_patient_Wise_report?.visibility = View.GONE
                }
*/


                /*    if(radTestCheck!!){

                    rad_test?.visibility = View.VISIBLE

                }
                else{

                    rad_test?.visibility = View.GONE
                }*/


            } else {


                regreportlayout?.visibility= View.GONE

                reg_report_image?.setImageResource(R.drawable.ic_add)


            }

        }



        ipAdmission_report?.setOnClickListener {



            if(ipadmissionreportlayout?.visibility== View.GONE){

                appMasterList?.visibility= View.GONE
                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.VISIBLE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_minus_white)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)




                val DWPL =
                    appPreferences?.getBoolean(AppConstants.CHECKIPADMISSIONADMISSIONDWP)

                val WW =
                    appPreferences?.getBoolean(AppConstants.CHECKIPADMISSIONADMISSIONWW)

                val DW =
                    appPreferences?.getBoolean(AppConstants.CHECKIPADMISSIONADMISSIODW)

                val SL =
                    appPreferences?.getBoolean(AppConstants.CHECKIPADMISSIONADMISSIONSL)

                val DL =
                    appPreferences?.getBoolean(AppConstants.CHECKIPADMISSIONADMISSIONDL)

                val DC =
                    appPreferences?.getBoolean(AppConstants.CHECKIPADMISSIONADMISSIONDC)

                if (DWPL!!) {

                    admission_day_patient_Wise_report?.visibility = View.VISIBLE

                } else {

                    admission_day_patient_Wise_report?.visibility = View.GONE
                }


                if (WW!!) {

                    admission_ward_Wise_report?.visibility = View.VISIBLE

                } else {

                    admission_ward_Wise_report?.visibility = View.GONE
                }

                if (DW!!) {

                    admission_doctor_Wise_report?.visibility = View.VISIBLE

                } else {

                    admission_doctor_Wise_report?.visibility = View.GONE

                }

                if(SL!!){

                    admission_state_level_report?.visibility = View.VISIBLE

                }
                else{

                    admission_state_level_report?.visibility = View.GONE

                }


                if(DL!!){

                    admission_district_level_report?.visibility = View.VISIBLE

                }
                else{

                    admission_district_level_report?.visibility = View.GONE

                }

                if(DC!!){

                    discharge_report_count_layout?.visibility = View.VISIBLE

                }
                else{

                    discharge_report_count_layout?.visibility = View.GONE

                }



            } else {

                /*   admission_day_patient_Wise_report?.visibility = View.GONE

                   admission_ward_Wise_report?.visibility = View.GONE

                   admission_doctor_Wise_report?.visibility = View.GONE

                   admission_state_level_report?.visibility = View.GONE

                   admission_district_level_report?.visibility = View.GONE

                   discharge_report_count_layout?.visibility = View.GONE*/

                ipadmissionreportlayout?.visibility= View.GONE

                admission_report_image?.setImageResource(R.drawable.ic_add)


            }

        }



        lab_report?.setOnClickListener {

            if (labreportLayout?.visibility == View.VISIBLE) {

                labreportLayout?.visibility = View.GONE

                /*     labWise_report.visibility=View.GONE

                     labTestWise_report.visibility=View.GONE

                     sessionWise_report.visibility=View.GONE

                     dateWiseSession_report.visibility=View.GONE


                     dateWise_report.visibility=View.GONE


                     districtwise_report.visibility=View.GONE

                     districtwisetest_report.visibility=View.GONE*/

                lab_report_image?.setImageResource(R.drawable.ic_add)

            } else {


                appMasterList?.visibility= View.GONE

                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.VISIBLE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_minus_white)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)



                var consolidateReport=appPreferences?.getBoolean(AppConstants.REPORTCONSOLIDATEDREPORT)

                consolidateReport=true

                if(consolidateReport){

                    consolidated_report?.visibility = View.VISIBLE
                }
                else{

                    consolidated_report?.visibility = View.GONE
                }

                var labWiseReport=appPreferences?.getBoolean(AppConstants.REPORTLABWISEREPORT)

                labWiseReport=true

                if(labWiseReport){

                    labWise_report?.visibility = View.VISIBLE
                }
                else{

                    labWise_report?.visibility = View.GONE
                }

                var labTestWiseReport=appPreferences?.getBoolean(AppConstants.REPORTLABTESTWISEREPORT)

                labTestWiseReport=true

                if(labTestWiseReport){

                    labTestWise_report?.visibility = View.VISIBLE
                }
                else{

                    labTestWise_report?.visibility = View.GONE
                }

                var districtWiseReport=appPreferences?.getBoolean(AppConstants.REPORTDISTRICTWISEPATIENTCODE)

                districtWiseReport=true

                if(districtWiseReport){

                    districtwise_report?.visibility = View.VISIBLE
                }
                else{

                    districtwise_report?.visibility = View.GONE
                }

                var districtWiseTestReport=appPreferences?.getBoolean(AppConstants.REPORTDISTRICTWISETESTCODE)

                districtWiseTestReport=true

                if(districtWiseTestReport){

                    districtwisetest_report?.visibility = View.VISIBLE
                }
                else{

                    districtwisetest_report?.visibility = View.GONE
                }


                lab_report_image?.setImageResource(R.drawable.ic_minus_white)

            }
        }


        tutorial?.setOnClickListener {

            if (tutorialLayout?.visibility == View.VISIBLE) {

                tutorialLayout?.visibility = View.GONE

                //userManual.visibility=View.GONE

                tutorial_image?.setImageResource(R.drawable.ic_add)

            } else {

                appMasterList?.visibility= View.GONE
                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.VISIBLE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_minus_white)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)




                var video_tutoarial=appPreferences?.getBoolean(AppConstants.TUTORIALVIDEOTUTORIAL)

                video_tutoarial=true

                if(video_tutoarial){

                    videoTutorial?.visibility = View.VISIBLE
                }
                else{

                    videoTutorial?.visibility = View.GONE
                }

                var user_manual=appPreferences?.getBoolean(AppConstants.TUTORIALUSERMANUAL)

                user_manual=true

                if(user_manual){
                    userManual?.visibility = View.VISIBLE
                }
                else{
                    userManual?.visibility = View.GONE
                }
                //   tutorial_image?.setImageResource(R.drawable.ic_minus_white)
            }
        }
        helpDesk?.setOnClickListener {

            if (helpdesklayout?.visibility == View.VISIBLE) {

                helpdesklayout?.visibility = View.GONE

                helpDesk_image?.setImageResource(R.drawable.ic_add)

            } else {
                appMasterList?.visibility= View.GONE
                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.VISIBLE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_minus_white)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)


                var ticket=appPreferences?.getBoolean(AppConstants.HELPDESKTICKETS)

                ticket=true

                if(ticket){

                    tickets?.visibility = View.VISIBLE
                }
                else{

                    tickets?.visibility = View.GONE
                }

                helpDesk_image?.setImageResource(R.drawable.ic_minus_white)

            }
        }


        pharmacy?.setOnClickListener {

            if (pharmacylayout?.visibility == View.GONE) {

                appMasterList?.visibility= View.GONE

                pharmacy_dashboard?.visibility = View.VISIBLE

                pharmacy_dispance?.visibility=View.VISIBLE

                pharmacy_return?.visibility=View.VISIBLE

                pharmacy_injrectionWorklist?.visibility=View.VISIBLE

                pharmacy_return_text?.visibility=View.VISIBLE

                //            pharmacy_image?.setImageResource(R.drawable.ic_minus_white
                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.VISIBLE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_minus_white)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)





            } else {
                pharmacylayout?.visibility = View.GONE
                /*   pharmacy_dispance?.visibility=View.GONE
                   pharmacy_injrectionWorklist?.visibility=View.GONE
                   pharmacy_return?.visibility=View.GONE*/
                pharmacy_image?.setImageResource(R.drawable.ic_add)
            }


        }

        Bedmangementreport?.setOnClickListener {

            if (bedreportlayout?.visibility == View.GONE) {
/*
                Bedstatushw?.visibility = View.VISIBLE

                bedmangementreport_image?.setImageResource(R.drawable.ic_minus_white)*/


                appMasterList?.visibility= View.GONE

                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.VISIBLE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_minus_white)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)




                val Bedrep=appPreferences?.getBoolean(AppConstants.CHECK_BEDSTHW)

                if(Bedrep!!){

                    Bedstatushw?.visibility = View.VISIBLE

                }
                else{

                    Bedstatushw?.visibility = View.GONE
                }




            } else {

                bedreportlayout?.visibility = View.GONE


                bedmangementreport_image?.setImageResource(R.drawable.ic_add)


            }
        }

        ipmangement?.setOnClickListener {

            if (ipmanagementlayout?.visibility == View.VISIBLE
            /*||
            ipmangement_admission?.visibility == View.VISIBLE ||
            ipmangement_emergency_casualty_patient_list?.visibility == View.VISIBLE || ipwardmasterlayout?.visibility == View.VISIBLE*/
            ) {


                ipmanagementlayout?.visibility = View.GONE

                /*  ipmangement_admission?.visibility = View.GONE

                  ipwardmasterlayout?.visibility = View.GONE

                  ipmangement_emergency_casualty_patient_list?.visibility = View.GONE
  */
                ipmangement_image?.setImageResource(R.drawable.ic_add)


            } else {


                appMasterList?.visibility= View.GONE

                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.VISIBLE

                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_minus_white)

                settings_image?.setImageResource(R.drawable.ic_add)



                val ipdashboard=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_IP_DASHBOARD)

                if(ipdashboard!!){

                    ipmangement_dashboard?.visibility = View.VISIBLE

                }
                else{

                    ipmangement_dashboard?.visibility = View.GONE
                }


                val ipadmissioncheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_IP_ADMISSION)

                if(ipadmissioncheck!!){

                    ipmangement_admission?.visibility = View.VISIBLE

                }
                else{

                    ipmangement_admission?.visibility = View.GONE

                }

                val wardcheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_IP_WARD_MASTER)

                if(wardcheck!!){

                    ipwardmasterlayout?.visibility = View.VISIBLE

                }
                else{

                    ipwardmasterlayout?.visibility = View.GONE

                }


                val ecplcheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_IP_ECPL)

                if(ecplcheck!!){

                    ipmangement_emergency_casualty_patient_list?.visibility = View.VISIBLE

                }
                else{

                    ipmangement_emergency_casualty_patient_list?.visibility = View.GONE

                }



            }
        }

        Bedstatushw.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= BedStatusHospitalWiseFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }



        ipmangement_dashboard?.setOnClickListener{
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= IpManageDashBoardFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        healthoffice?.setOnClickListener{
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= HealthofficeListFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }


        settings.setOnClickListener {

            if (settingLayout.visibility == View.VISIBLE) {

                settingLayout?.visibility= View.GONE

                settings_image.setImageResource(R.drawable.ic_add)

            } else {


                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                regreportlayout?.visibility= View.GONE

                ipadmissionreportlayout?.visibility= View.GONE

                labreportLayout?.visibility= View.GONE

                tutorialLayout?.visibility= View.GONE

                helpdesklayout?.visibility= View.GONE

                pharmacylayout?.visibility= View.GONE

                bedreportlayout?.visibility= View.GONE

                ipmanagementlayout?.visibility= View.GONE

                settingLayout?.visibility= View.VISIBLE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                reg_report_image?.setImageResource(R.drawable.ic_add)

                admission_report_image?.setImageResource(R.drawable.ic_add)

                lab_report_image?.setImageResource(R.drawable.ic_add)

                tutorial_image?.setImageResource(R.drawable.ic_add)

                helpDesk_image?.setImageResource(R.drawable.ic_add)

                pharmacy_image?.setImageResource(R.drawable.ic_add)

                bedmangementreport_image?.setImageResource(R.drawable.ic_add)

                ipmangement_image?.setImageResource(R.drawable.ic_add)

                settings_image.setImageResource(R.drawable.ic_minus_white)

                changePassword.visibility = View.VISIBLE
                languages.visibility = View.VISIBLE


            }
        }

        //Application

        application_institution?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val app_institution = InstitutionFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, app_institution)
            fragmentTransaction.commit()
        }

        ipwardmasterlayout?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op = WardMasterFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }



        ipmangement_admission?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= AdmissionListFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        ipmangement_emergency_casualty_patient_list?.setOnClickListener {
            drawerLayout?.closeDrawer(GravityCompat.START)
            val fragment = EmergencyCasualtyPatientListFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, fragment)
            fragmentTransaction.commit()
        }

        rad_test?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= RmisTestActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        rad_dashboard?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= RMISDashboardFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        pharmacy_injrectionWorklist?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= NurseDeskInjectionFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }




        pharmacy_dispance?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= PharmacyDispenseFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        pharmacy_dashboard?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= PharmacyDashboardFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        pharmacy_return.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= PharmacyReturnFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        changePassword?.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val dialog = ChangePasswordFragemnt()
            dialog.show(ft, "Tag")
        }


        covid_reg?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op=QuickRegistrationActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //       fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        quick_Reg?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op=QuickRegistrationNew()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //   fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        OPCorrection?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= OpCorrectionFragement()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        bluePrintlayout?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= BluePrintFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        ipCorrectionlayout?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= IpCorrectionFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        detailedRegistration?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op=DetailedRegistration()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)

            fragmentTransaction.commit()
        }



        lab_test?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op=LabTestActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }
        lab_approvl?.setOnClickListener {


            drawerLayout!!.closeDrawer(GravityCompat.START)


            val op=LabTestApprovalActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        consolidated_report?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= LabConsolidatedReportsActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        videoTutorial?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= VideoTutorialActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }


        tickets?.setOnClickListener {
            var loginType = appPreferences?.getString(AppConstants.LOGINTYPE)!!
            Log.e("loginType",loginType)

            drawerLayout!!.closeDrawer(GravityCompat.START)
            var op:Fragment?=null
            if(loginType.equals("HDA")){
                op= AgentTicketListFragment()
            }else{
                op= UserTicketsListFragment()
            }
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }


        userManual?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= UserManualActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        districtwise_report?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op= LabDistrictWisePatientReportsActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        districtwisetest_report?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op= LabDistrictWiseTestReportsActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        labWise_report?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op= LabWiseReportsActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        labTestWise_report?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op= LabTestWiseReportsActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        sessionWise_report?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op=
                SessionWiseReportFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        dateWise_report?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op= DateWiseReportFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        dateWiseSession_report?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, DateWiseSessionReportFragment())
            fragmentTransaction.commit()

        }
        departmentWise_report?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, DepartmentWiseReportFragment())
            fragmentTransaction.commit()

        }

        dayWisePatient_report?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, DayWisePatientReportFragment())
            fragmentTransaction.commit()
        }

        admission_day_patient_Wise_report?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, AdmissionDayPatientReportFragment())
            fragmentTransaction.commit()
        }

        //admission_ward_Wise_report
        admission_ward_Wise_report.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, AdmissionWardWiseReportFragment())
            fragmentTransaction.commit()
        }

        //admission_doctor_Wise_report
        admission_doctor_Wise_report.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, AdmissionDoctorWiseReportFragment())
            fragmentTransaction.commit()
        }

        //admission_state_level_report
        admission_state_level_report.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, AdmissionStateLevelReportFragment())
            fragmentTransaction.commit()
        }

        //admission_district_level_report
        admission_district_level_report.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, AdmissionDistrictLevelReportFragment())
            fragmentTransaction.commit()
        }

        //admission_discharge_count_wise_report
        discharge_report_count_layout.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, DischargeReportCountWiseReportFragment())
            fragmentTransaction.commit()
        }

        lab_process?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op=LabTestProcessActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }

        new_order?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op=LimsNewOrderFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }
        sample_dispatch?.setOnClickListener {


            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op=SampleDispatchActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }
        result_dispatch?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= ResultDispatchActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }

        order_status?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op=OrderStatusActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }

        rad_order_status?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op=RmisOrderStatusfragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }

        rad_result_dispatch?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= RMISResultDispatchFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }



        rad_new_order.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= RMISNewOrderFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }

        rad_process.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= RmisTestProcessActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        rad_approvl.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= RmisTestApprovalActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        if (BuildConfig.DEBUG && BuildConfig.FLAVOR == "dev")
            llTelemedicine.visibility = View.VISIBLE
        else
            llTelemedicine.visibility = View.GONE
        llTelemedicine?.setOnClickListener {
            if (llAppointmentSession.visibility == View.GONE || llAppointmentBook.visibility == View.GONE) {
                llAppointmentSession.visibility = View.VISIBLE
                llAppointmentBook.visibility = View.VISIBLE
                ivTelemedicine?.setImageResource(R.drawable.ic_minus_white)
            } else {
                llAppointmentSession.visibility = View.GONE
                llAppointmentBook.visibility = View.GONE
                ivTelemedicine?.setImageResource(R.drawable.ic_add)
            }
        }

        llAppointmentSession?.setOnClickListener {
            startActivity(Intent(this@MainLandScreenActivity, AppointmentActivity::class.java).apply {
                putExtra(AppointmentActivity.ISAPPOINTMENTSESSION, true)
            })
        }
        llAppointmentBook?.setOnClickListener {
            startActivity(Intent(this@MainLandScreenActivity, AppointmentActivity::class.java).apply {
                putExtra(AppointmentActivity.ISAPPOINTMENTSESSION, false)
            })
        }


        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)

        name = appPreferences?.getString(AppConstants.INSTITUTION_NAME)

        /*   userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
           val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
           */
        landofficeName!!.text = ""+userDataStoreBean?.title?.name+userDataStoreBean?.first_name+" / "+name


        iv_change_institue?.setOnClickListener {

            val logintype=appPreferences?.getString(AppConstants.LOGINTYPE)

            if(logintype==AppConstants.LABINCHARGE) {

                val ft = supportFragmentManager.beginTransaction()
                val dialog = SelectInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else  if((logintype==AppConstants.PHYSICIANASSISTANT || logintype==AppConstants.PHYSICIANTRAINEE ) || (logintype== AppConstants.HELPDESK || logintype == AppConstants.GENDRALCODE)) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = DoctorInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

            else  if(logintype==AppConstants.PHARMACIST) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = PharmacistInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else  if(logintype==AppConstants.RADINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = RadiologyInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

            else  if(logintype==AppConstants.NURSELOGIN ) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = NurseInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else{
                val ft = supportFragmentManager.beginTransaction()

                val dialog = DoctorInstituteDialogFragment()
                // val dialog = InstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

/*
            if(type!=AppConstants.LABINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = InstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else{
                val ft = supportFragmentManager.beginTransaction()
                val dialog = SelectInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }*/
        }

        landofficeName?.setOnClickListener {

            val logintype=appPreferences?.getString(AppConstants.LOGINTYPE)

            if(logintype==AppConstants.LABINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()

                val dialog = SelectInstituteDialogFragment()

                dialog.show(ft, "Tag")
            }
            else  if((logintype==AppConstants.PHYSICIANASSISTANT || logintype==AppConstants.PHYSICIANTRAINEE ) || (logintype== AppConstants.HELPDESK || logintype == AppConstants.GENDRALCODE)) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = DoctorInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

            else  if(logintype==AppConstants.PHARMACIST) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = PharmacistInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else  if(logintype==AppConstants.RADINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = RadiologyInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

            else  if(logintype==AppConstants.NURSELOGIN ) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = NurseInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else{
                val ft = supportFragmentManager.beginTransaction()

                val dialog = DoctorInstituteDialogFragment()
                // val dialog = InstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

/*
            if(type!=AppConstants.LABINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = InstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else{
                val ft = supportFragmentManager.beginTransaction()
                val dialog = SelectInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }*/
        }


        val land= appPreferences?.getString(AppConstants.LANDSCREEN)

        val landurl= appPreferences?.getString(AppConstants.LANDURL)

        if(land!! == AppConstants.LABAPPROVELCODE){

            val op=LabTestApprovalActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.COVIDREGISTERCODE){

            val op=QuickRegistrationActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.QUICKREGISTERCODE){

            var op=QuickRegistrationNew()

            var bundle:Bundle= Bundle()

            bundle.putInt("PIN",0)

            op.arguments=bundle

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        else if(land == AppConstants.LABPROCESSCODE){

            val op=LabTestProcessActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.LABTESTCODE){

            val op=LabTestActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        else if(land == AppConstants.ACTIVITY_CODE_NURSE_CONFIG || land == AppConstants.BEDMANNGEMENT ){

            val op=NurseEmrWorkFlowActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        else if(land == AppConstants.LABNEWORDERCODE){

            /* val op=DashBoardActivity()

             val fragmentTransaction = supportFragmentManager.beginTransaction()
             fragmentTransaction.replace(R.id.landfragment, op)
             fragmentTransaction.addToBackStack(null)
             fragmentTransaction.commit()*/
        }

        else if(land == AppConstants.ACTIVITY_IP_WARD_MASTER){

            val op=WardMasterFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.EMRDASHBOARDCODE){

            val op=DashBoardActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.HELPDESKTICKETS){

            val op=UserTicketsListFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        else if(land == AppConstants.ACTIVITY_RMIS_TECH){

            val op=RMISNewOrderFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.ACTIVITY_RMIS_TESTAPPROVAL){

            val op=RmisTestApprovalActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        if(land == AppConstants.ACTIVITY_BEDSTHW){

            val op=BedStatusHospitalWiseFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.ACTIVITY_IP_ADMISSION){

            val op=AdmissionListFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else{


            if(landurl!! ==AppConstants.OPROUTEURL){

                val op= OutPatientFragment()

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.landfragment, op)
                fragmentTransaction.addToBackStack("home")
                fragmentTransaction.commit()
            }
            else if(landurl ==AppConstants.IPROUTEURL){

                val op= InpatientFragment()

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.landfragment, op)
                fragmentTransaction.commit()
            }
            else{
/*

                val op=DashBoardActivity()

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.landfragment, op)
                fragmentTransaction.commit()
*/

            }}
        changePassword?.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val dialog = ChangePasswordFragemnt()
            dialog.show(ft, "Tag")
        }

        logout?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            customdialog = Dialog(this)
            customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
            customdialog!! .setCancelable(false)
            customdialog!! .setContentView(R.layout.logout_dialog)
            val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView
            closeImageView.setOnClickListener {

                customdialog?.dismiss()
            }

            val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView

            val noBtn = customdialog!! .findViewById(R.id.no) as CardView

            yesBtn.setOnClickListener {

                viewModel?.LogOutseassion(appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,LoginSeasionRetrofitCallBack)


                appPreferences?.saveInt(AppConstants.LAB_UUID, 0)

                appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID, "")

                startActivity(Intent(this, LoginActivity::class.java))

                finishAffinity()



                customdialog!!.dismiss()

            }
            noBtn.setOnClickListener {
                customdialog!! .dismiss()


            }
            customdialog!! .show()
        }


    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.landfragment, fragment).addToBackStack("").commit()
    }

    fun replaceFragmentNoBack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.landfragment, fragment).commit()
    }

    fun replaceFragmentbundledata(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.landfragment, fragment).commit()
    }

    override fun onInstitutionCallback() {
        name = appPreferences?.getString(AppConstants.INSTITUTION_NAME)
        landofficeName!!.text = name
        Log.i("instution","Change")
    }

    override fun onRefreshLanguage() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        if (selectedFragment is DashBoardActivity) {
            if (backPressed + 2000 > System.currentTimeMillis())
                finish()
            else {
                Toast.makeText(this@MainLandScreenActivity, "Press once again to exit", Toast.LENGTH_SHORT)
                    .show()
            }
            backPressed = System.currentTimeMillis()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun setSelectedFragment(fragment: Fragment?) {
        this.selectedFragment = fragment
    }



    val LoginSeasionRetrofitCallBack = object : RetrofitCallback<SimpleResponseModel?> {

        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel?>) {

            if (responseBody?.body()?.statusCode == 200) {


            }

        }

        override fun onBadRequest(response: Response<SimpleResponseModel?>) {

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
