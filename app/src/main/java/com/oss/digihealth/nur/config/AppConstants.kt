package com.oss.digihealth.nur.config

//import com.oss.digihealth.doc.ui.emr_workflow.model.favourite.FavouritesModel
import java.util.ArrayList

object AppConstants {

    //DEV
//        const val BASE_URL = "https://qahmisgateway.oasyshealth.co/DEV"

    //QA
    // Request URL: https://qahmisgateway.oasyshealth.co/QAHMIS-Login/1.0.0/api/authentication/loginNew
    //const val BASE_URL = "https://qahmisgateway.oasyshealth.co/"

    //UAT
    // const val BASE_URL = "https://uathmisgateway.oasyshealth.co/"


    const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    const val TIMEOUT_VALUE = 60000
    const val SUCCESS_RESPONSE_CODE = 200
    const val SHARE_PREFERENCE_NAME = "hims_preference"
    const val WSO2_TOKEN =
        "ZmpvTWJZSHJoUmZuek1RTHhCaUFhSElESHNVYTp6UFdKR2ZTZk53NG5ObG5aaUxwWjROU0xWYkVh"

    const val BEARER_AUTH = "Bearer "

    const val ALERTDIALOG = "dialog"
    const val LANGUAGE = "language"
    const val ALERTDIALOGTITLE = "Alert"
    const val ACCEPT_LANGUAGE_EN = "en"
    const val RESPONSECONTENT = "ResponseContant"
    const val RESPONSETYPE = "ResponseType"
    const val RESPONSEORDERARRAY = "ResponseArray"
    const val RESPONSEDISPATCH = "ResponseDispatch"
    const val RESPONSECONTENTS = "ResponseContant"
    const val RESPONSENEXT = "ResponseNext"
    const val DIAGNISISRESPONSECONTENT = "DiadnosisResponseContant"
    const val PRESCRIPTIONRESPONSECONTENT = "PrescriptionResponseContant"
    const val RADIOLOGYRESPONSECONTENT = "RadiologyResponseContent"
    const val INVESTIGATIONRESPONSECONTENT = "InvestigationResponseContent"
    const val ACTIVITY_CODE_CHIEF_COMPLAINTS = "OPE008"
    const val ACTIVITY_CODE_LAB_RESULT = "OPE014"
    const val ACTIVITY_CODE_LAB = "OPE004"
    const val ACTIVITY_ALLEGERY_CODE ="OPE019"
    const val ACTIVITY_CODE_RADIOLOGY = "OPE005"
    const val ACTIVITY_CODE_INVESTIGATION = "OPE011"
    const val ACTIVITY_CODE_DIAGNOSIS = "OPE012"
    const val ACTIVITY_CODE_VITALS = "OPE010"
    const val ACTIVITY_CODE_PRESCRIPTION = "OPE006"
    const val ACTIVITY_CODE_HISTORY = "OPE009"
    const val ACTIVITY_CODE_FAMILY_HISTORY = "FAM"
    const val ACTIVITY_CODE_RADIOLOGY_RESULT = "OPE015"
    const val ACTIVITY_CODE_TREATMENT_KIT = "OPE007"
    const val ACTIVITY_CODE_OP_NOTES = "OPE003"
    const val ACTIVITY_CODE_OT_NOTES = "gggggg"
    const val ACTIVITY_CODE_ANESTHESIA_NOTES = "anesh"
    const val ACTIVITY_CODE_DISCHARGEMEDICATION = "medication"
    const val ACTIVITY_ID_DISCHARGEMEDICATION = 260
    //    const val ACTIVITY_CODE_DOCUMENT = "DOCUM"
    const val ACTIVITY_CODE_DOCUMENT = "OPE017"
    const val ACTIVITY_CODE_BLOOD_REQUEST = "OPE023"
    const val ACTIVITY_CODE_SPECIALITY_SKETCH = "SSK"
    const val ACTIVITY_CODE_IP_CASE_SHEET = "CASE"
    const val ACTIVITY_ID_IP_CASE_SHEET = 372
    const val ACTIVITY_CODE_OT_SCHEDULE = "OPE026"
    const val ACTIVITY_ID_OT_SCHEDULE = 255
    const val ACTIVITY_CODE_INVESTIGATION_RESULT = "OPE016"
    const val ACTIVITY_CODE_ADMISSION = "OPE013"
    const val ACTIVITY_CODE_CERTIFICATE = "OPE039"
    const val ACTIVITY_ID_CERTIFICATE = 382
    const val ACTIVITY_CODE_MRD = "OPE040"
    const val ACTIVITY_ID_MRD = 383
    const val ACTIVITY_CODE_DIET = "Diet"
    const val ACTIVITY_CODE_PROGRESS_NOTES = "QQQQ"
    const val LABINCHARGE = "Lab Incharge"
    const val PHYSICIANTRAINEE = "Physician"
    const val PHYSICIANASSISTANT = "Surgeon"
    const val OLDPINNUMBER = "OldPatientPin"

    const val PHARMACIST = "Pharmacist"

    const val RADINCHARGE = "Radiology Incharege"

    const val HELPDESK = "HDA"
    const val PDFDATA = "PDFDATA"
    const val Ward_MASTER_UUID = "WardmasterUUID"
    const val NumberOfRooms = "NumberOfRooms"
    const val Ward_MASTER_NAME = "ward_mastername"


    const val REQ_CODE_MANAGE_FRAGMENT = 3


    const val ENCOUNTER_TYPE_UUID_IP = 2


    const val SELECTED_ARRAY_LIST = "selectedArrayList"
    const val FAV_TYPE_ID_DIAGNOSIS = 6
    const val FAV_TYPE_ID_CHIEF_COMPLAINTS = 5
    const val FAV_TYPE_ID_LAB = 2
    const val FAV_TYPE_ID_PRESCRIPTION = 1
    const val FAV_TYPE_ID_Vitual = 4
    const val FAV_TYPE_ID_INVESTIGATION = 7
    const val FAV_TYPE_ID_TREATMENTKIT = 8
    const val FAV_TYPE_ID_DIET = 9
    const val FAV_TYPE_ID_SPECIALITY_SKETCH = 10
    const val FAV_TYPE_ID_RADIOLOGY = 3

    const val TEM_TYPE_ID_VITALS = 4
    const val LAB_TESTMASTER_UUID = 1
    const val RMIS_TESTMASTER_UUID = 2
    const val INVESTIGATION_TESTMASTER_UUID = 3


    const val LAB_MASTER_TYPE_ID_RADIOLOGY = 2
    const val LAB_MASTER_TYPE_ID = "LabMasterUUID"
    const val ImageURI = "URI"

    const val FAV_TYPE_ID_CHIEF = 5
    const val SEARCHKEYMOBILE: String = "SEARCHKEYMOBILE"
    const val SEARCHKEYPIN: String = "SEARCHKEYPIN"
    const val SEARCHNAME: String = "SEARCHNAME"

    const val History = 5


    const val ENCRYPT_KEY = "aesEncryptionKey"
    const val IV = "oasysoasysoasys1"
    const val ALGORITHAM = "AES"
    const val AES_SELECTED_MODE = "AES/CBC/PKCS7Padding"

    const val OFFICE_UUID = "office_uid"
    const val OFFICE_NAME = "office_name"

    const val FACILITY_UUID = "facility_uid"
    const val FAV_TYPE_ID = "fav_type_id"
    const val DEPARTMENT_UUID = "department_uuid"

    const val DEPARTMENT_NAME = "department_name"
    const val LAB_UUID = "lab_uuid"
    const val OTHER_DEPARTMENT_UUID = "other_department_uuid"

    const val PATIENT_UUID = "patientUuid"
    const val DoctorUUID = "doctorUUID"
    const val PATIENT_NAME = "patientName"
    const val PATIENT_AGE = "patientAge"
    const val PATIENT_GENDER = "patientGender"
    const val PATIENT_UHID = "patientUhid"

    const val ENCOUNTER_TYPE = "encounterType"
    const val ENCOUNTER_DOCTOR_UUID = "encounter_doctor_uuid"
    const val ENCOUNTER_UUID = "encounter_uuid"
    const val USER_NAME = "user_name"
    const val KEYFAVOURITEID = "favouriteid"
    const val STOREMASTER_UUID = "store_master_uuid"
    const val STOREMASTER_NAME = "store_master_name"
    const val INSTITUTION_NAME = "ins_name"
    const val WARDNAME = "ward_name"
    const val LASTPIN = "lastpin"
    const val ROLEUUID = "RoleID"
    const val FirstTime = "firsttime"
    const val PRESCRIPTIONTYPE = "PrescrtionDataType"

    const val EMRCHECK = "EMRCHECK"
    const val TUTORIALCHECK = "TUTORIALCHECK"
    const val HELPDESKCHECK = "HELPDESKCHECK"
    const val REGISTRATIONCHECK = "REGISTRATIONCHECK"
    const val LMISCHECK = "LMISCHECK"
    const val Comments = "Comments"

    const val ROLE_LMIS = "LIS"
    const val ROLE_Registration = "Registration"
    const val ROLE_LABREPORT = "Lab Reports"
    const val ROLE_EMR = "EMR"
    const val ROLE_HELPDESK = "Help Desk"
    const val ROLE_TUTORIAL = "Tutorials"
    const val ROLE_IPMANAGEMENT = "IP Management"
    const val ROLE_PHARMASY = "Pharmacy"
    const val ROLE_RMIS = "RMIS"

    const val ROLE_BEDREPORT = "Bed Management Reports"

    const val TYPE_OUT_PATIENT = 1
    const val TYPE_IN_PATIENT = 2


    const val IN_PATIENT = "InPatient"
    const val OUT_PATIENT = "OutPatient"
    const val CONFRIGURATION = "configuration"
    const val PATIENT_TYPE = "patientType"

    const val LANDSCREEN = "LandScreen"

    const val LANDURL = "LandUrl"

    const val QUICKREGISTER = "QuickRegisterView"

    const val LOGINTYPE = "LoginType"

    const val COVIDREGISTER = "CovidRegisterView"

    const val LABTEST = "LabTestView"

    const val LABAPPROVEL = "LabApprovelView"

    const val LABREPORTS = "LabReportsView"

    const val LABPROCESS = "LabProcessView"

    const val LABNEWORDER = "LabNewOrderView"

    const val LABSAMPLEDISPATCH = "LabSampleDispatchView"

    const val LABRESULTDISPATCH = "ResultDispath"

    const val LABORDERSTATUS = "OrderStatus"

    const val REPORTCONSOLIDATEDREPORT = "consolidatedreport"

    const val REPORTLABWISEREPORT = "LabWisereport"

    const val REPORTLABTESTWISEREPORT = "LabTestWisereport"

    const val REPORTOPSESSIONWISEREPORT = "LabTestWisereport"

    const val REPORT = "LabReports"

    const val TUTORIALVIDEOTUTORIAL = "videoTutorial"

    const val TUTORIALUSERMANUAL = "userManual"

    const val HELPDESKTICKETS = "tickets"

    const val COVIDREGISTERCODE = "REGQ"

    const val QUICKREGISTERCODE = "REGB"

    const val LABTESTCODE = "LABTestOrder"

    const val LABAPPROVELCODE = "LabTestApproval"

    const val LABPROCESSCODE = "LabTestProcess"

    const val LABNEWORDERCODE = "LABTech"

    const val LABREPORTSCODE = "Lab Reports"

    const val EMRDASHBOARDCODE = "OPE001"

    const val LABSAMPLEDISPATCHCODE = "LabSampleDispatch"

    const val LABRESULTDISPATCHCODE = "ResultDispatces"

    const val LABORDERSTATUSCODE = "PatOrderStatus"

    const val REPORTCONSOLIDATEDREPORTCODE = "ConsolidatedReport"

    const val REPORTLABWISEREPORTCODE = "LabWiseReport"

    const val REPORTLABTESTWISEREPORTCODE = "LabTestWiseReport"

    const val REPORTDISTRICTWISEPATIENTCODE = "DistrictWisePatient Report"

    const val REPORTDISTRICTWISETESTCODE = "DistrictWiseTest Report"

    const val REPORTCODE = "Lab Reports"


    const val ANALYTECODE = "Analyte"

    const val APLASPLCODE = "AlphanumericSpl"

    const val ALPANUMBERCODE = "Alphanumeric"

    const val DROPDOWNCODE = "Dropdown"

    const val NOTETEMPLATECODE = "NoteTemplate"

    const val LONGTEXTCODE = "LongTextBox"

    const val NUMERICCODE = "Numeric"

    const val OPROUTEURL = "emr/op"

    const val IPROUTEURL = "emr/ip"

    const val CALCULATIOCODE = "Calculation "
    const val AGE = "age"
    const val GENDER = "GENDER"
    const val Createdate = "creatdate"

    // Nurse Desk

    const val WARDUUID = "WardUUId"

    const val ROLE_NURSE = "Nurse Desk"

    const val CHECK_NURSE = "NurseDeskCheck"

    const val NURSELOGIN = "Nurse Incharge"

    const val BEDMANNGEMENT = "BEDMAN001"

    const val PATIENT_ORDER_UUID = "PatientOrderUUID"

    // Help Desk

    const val TICKET_CODE = "tic07"

    const val CHECK_HELPDESK = "HelpdeskCheck"

    // Tutorial Check

    const val CHECK_TUTORIAL = "TurorialsCheck"

    const val ACTIVITY_VIDEO = "Video Tutorial"

    const val ACTIVITY_USER = "User Manual"

    const val ACTIVITY_CHECK_VIDEO = "VideoCheckTutorial"

    const val ACTIVITY_CHECK_USER = "UserCheckManual"


    const val ACTIVITY_IP_WARD_MASTER = "w01"

    const val ACTIVITY_IP_ECPL = "cas03"

    // Application manager
    const val CHECK_APPLICATIONMANAGER = "Applicationmanager"

    // IP Management
    const val CHECK_IPMANAGEMENT = "IPManagementCheck"

    const val ACTIVITY_IP_DASHBOARD = "WardDash"

    const val ACTIVITY_CHECK_IP_DASHBOARD = "IPDashboardCheck"

    const val ACTIVITY_IP_ADMISSION = "Ad666"


    const val ACTIVITY_CHECK_IP_ADMISSION = "Ad666Check"


    const val ACTIVITY_CHECK_IP_WARD_MASTER = "WARDMASTERCheck"


    const val ACTIVITY_CHECK_IP_ECPL = "CHECKcas03"

    //Pharmacy
    const val CHECK_PHARMASY = "PharmacyCheck"

    const val ACTIVITY_PHY_DASHBOARD = "DSHBRD"

    const val ACTIVITY_CHECK_PHY_DASHBOARD = "DSHBRDCHECK"


    //bed report

    const val ACTIVITY_BEDSTHW = "Bed Status Hospital Wise"

    const val CHECK_BEDSTHW = "Check Bed Status Hospital Wise"

    // RMIS
    const val CHECK_RMIS = "RMISCheck"

    const val CHECK_BEEDREPORT = "Bedreportcheck"

    const val ACTIVITY_RMIS_TECH = "RMISTech"

    const val ACTIVITY_RMIS_TEST = "RMISTestOrder"

    const val ACTIVITY_RMIS_TESTPROCESS = "RMISTestProcess"

    const val ACTIVITY_RMIS_TESTAPPROVAL = "RMISTestApproval"

    const val ACTIVITY_RMIS_DISPATCH = "RmisDispatch"

    const val ACTIVITY_RMIS_ORDER = "RMISOrderstatus"

    const val ACTIVITY_CHECK_RMIS_TECH = "RMISTechCheck"

    const val ACTIVITY_CHECK_RMIS_TEST = "RMISTestOrderCheck"

    const val ACTIVITY_CHECK_RMIS_TESTPROCESS = "RMISTestProcessCheck"

    const val ACTIVITY_CHECK_RMIS_TESTAPPROVAL = "RMISTestApprovalCheck"

    const val ACTIVITY_CHECK_RMIS_DISPATCH = "RmisDispatchCheck"

    const val ACTIVITY_CHECK_RMIS_ORDER = "RMISOrderstatusCheck"

    const val ACTIVITY_CHECK_RMIS_DASHBORD = "RMISDashboardCheck"


    const val PROFILE_TYPE_OP_NOTES = 1
    const val PROFILE_TYPE_OT_NOTES = 2
    const val PROFILE_TYPE_ANESTHESIA_NOTES = 3
    const val PROFILE_TYPE_IP_CASE_SHEET = 4
    const val PROFILE_TYPE_NURSE_DESK_NOTES = 6

    const val CCC_VENTILATOR_TYPE = 1
    const val CCC_ABG_TYPE = 2
    const val CCC_MONITOR_TYPE = 3
    const val CCC_INTAKE_OUTPUT_TYPE = 4
    const val CCC_BP_TYPE = 5
    const val CCC_DIABETICS_TYPE = 6
    const val CCC_DIALYSIS_TYPE = 7
    const val CCC_VITAL_CHART_TYPE = 8


    const val LAB_MASTER_ID = 1
    const val RADIOLOGY_MASTER_ID = 2
    const val INVESTIGATION_MASTER_ID = 3

    const val GENDRALCODE = "General"

    const val IPADMISSIO = "Ad666"


    const val ACTIVITY_CODE_NURSE_CONFIG = "nur02"
    const val ACTIVITY_CODE_NURSE_LAB = "Lab02"
    const val ACTIVITY_CODE_NURSE_INVESTIGATION = "Inve03"
    const val ACTIVITY_CODE_NURSE_DIET = "Diet1"
    const val ACTIVITY_CODE_NURSE_PRESCRIPTION = "drug01"
    const val ACTIVITY_CODE_NURSE_RADIOLOGY = "rad02"
    const val ACTIVITY_CODE_NURSE_DISCHARGE_SUMMARY = "DIS001"
    const val ACTIVITY_CODE_NURSE_BED_MANAGEMENT = "BEDMAN001"
    const val ACTIVITY_CODE_NURSE_CRITICAL_CARE_CHART = "Crit06"
    const val ACTIVITY_CODE_NURSE_NOTES = "No04"
    const val ACTIVITY_CODE_CONFIG = "nur02"

    //const val ACTIVITY_CODE_NURSE_VITAL= "VIT001"
    const val ACTIVITY_CODE_NURSE_VITAL = "01"


    const val SALUTAION = "SAL"
    const val PROFESTIONALSULUTAION = "PROFSAL"
    const val MIDDLENAME = "MNAME"
    const val LASTNAME = "LNAME"
    const val FATHERNAME = "FATHERNM"
    const val SUFFIXCODE = "SCODE"
    const val DATEOFBIRTH = "DOB"
    const val REMARK = "REMARK"


    const val COMMUNITY = "COMMUN"
    const val UNIT = "UNIT"
    const val ALTERNATIVENUMBER = "ALTNUM"
    const val DRNUMBNER = "DRNUM"
    const val STNAMME = "STNAME"
    const val COUNTRY = "COUNTRY"
    const val STATE = "STATE"
    const val DISTIRICT = "DISTRICT"
    const val TALUK = "TALUK"
    const val VILLAGE = "VILLAGE"
    const val BLOCK = "BLOCK"
    const val PINCODE = "PINCODE"
    const val TRPLAN = "TRPLAN"
    const val INCOME = "INCOME"
    const val AADHARNOR = "AADHAAR"
    const val OCCUPATION = "OCCUPATION"
    const val PLACE = "PLACE"
    const val RELIGION = "RELIGION"
    const val BLCLASS = "BLCLASS"
    const val PINSTATUS = "PINSTATUS"
    const val OPSTATUS = "OPSTATUS"
    const val REGDATE = "REGDATE"
    const val REFDET = "REFDET"
    const val REFHISTORY = "REFHISTORY"
    const val VISITHIS = "VISITHIS"
    const val MATANITY = "ISMAT"

    //Reg reports


    const val REGISTRATIONREPOERTCODE = "Registration Reports"

    const val CHECKREGISTRATIONREPOERT = "Registration Reports CHECK"
    const val REGSESSIONWISECODE = "Session Wise"
    const val REGDATEWISESESSIONCODE = "Date Wise Session"
    const val REGDAYWISEPATIENTLISTCODE = "Day Wise Patient List"
    const val REGDATEWISECODE = "Date Wise"


    const val CHECKREGDATEWISE = "Date Wise CHECK"
    const val CHECKREGSESSIONWISE = "CHECKREG Session Wise"
    const val CHECKREGDATEWISESESSION = "CHECKREG Date Wise Session"
    const val CHECKREGDAYWISEPATIENTLIST = "CHECKREG Day Wise Patient List"


    const val CHECKREGAdmissionDAYWISEPATIENTLIST = "CHECKREG  Admission Day Wise Patient List"

    const val IPADMISSIONREPORTCODE = "IP Admission Reports"
    const val IPADMISSIONADMISSIONDWCODE = "Admission Doctor  Wise"
    const val IPADMISSIONADMISSIONWWCODE = "Admission Ward  Wise"
    const val IPADMISSIONADMISSIONDWPCODE = "Admission Day  Wise Patients"
    const val IPADMISSIONADMISSIONDLCODE = "Admission District level"
    const val IPADMISSIONADMISSIONSLCODE = "Admission State Level"
    const val IPADMISSIONADMISSIONDC = "Discharge Report Count wise"


    const val CHECKIPADMISSIONREPORT = "IP Admission Reports Check"
    const val CHECKIPADMISSIONADMISSIODW = "Admission Doctor  Wise  Check"
    const val CHECKIPADMISSIONADMISSIONWW = "Admission Ward  Wise  Check"
    const val CHECKIPADMISSIONADMISSIONDWP = "Admission Day  Wise Patients Check"
    const val CHECKIPADMISSIONADMISSIONDL = "Admission District level  Check"
    const val CHECKIPADMISSIONADMISSIONDC = "Admission Discharge Count Wise Check"
    const val CHECKIPADMISSIONADMISSIONSL = "Admission State Level Check"


    const val APPLICATIONMANGERCODE = "Application Manager"
    const val APPLICATIONDASHBOARDCODE = "APPDASH"
    const val HEALTHOFFICECODE = "HLO001"
    const val DEPARTMENTCODE = "DEP001"
    const val INSTUTIONCODE = "INS001"
    const val ROLECODE = "ROL006"
    const val USERPROFILECODE = "USR001"
    const val REFERENCEDATACODE = "REFA001"


    const val APPLICATIONMANGERCHECK = "Application Manager Check"
    const val APPLICATIONDASHBOARDCHECK = "APPDASHCheck"
    const val HEALTHOFFICECHECK = "HLO001Check"
    const val DEPARTMENTCHECK = "DEP001Check"
    const val INSTUTIONCHECK = "INS001Check"
    const val ROLECHECK = "ROL006Check"
    const val USERPROFILECHECK = "USR001Check"
    const val REFERENCEDATACHECK = "REFA001Check"

    const val SAVE_AND_ORDER = "Save and Order"
    const val SAVE_AS_TEMPLATE = "Save"
}