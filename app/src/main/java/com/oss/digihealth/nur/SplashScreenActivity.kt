package com.oss.digihealth.nur

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.oss.digihealth.doc.utils.Utils
import com.oss.digihealth.nur.databinding.ActivitySplashScreenBinding
import com.oss.digihealth.nur.ui.login.view.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private var binding: ActivitySplashScreenBinding? = null
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000

    //    private val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private var firebaseDefaultMap: HashMap<String, Int>? = null
    val VERSION_CODE_KEY = "latest_app_version"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Utils(this).isTablet(this)) {
            this.setTheme(R.style.Hmis_SplashTablet)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            this.setTheme(R.style.Hmis_Splash)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        binding?.tvVersion?.setText("Version" + " " + BuildConfig.VERSION_NAME)

        if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {
            binding?.tvBottomView?.text = getString(R.string.copyright_msg_pune)
        } else {
            binding?.tvBottomView?.text = getString(R.string.copyright_msg)
        }

        /* val factory =
             SplashViewModelFactory(
                 application,
                 deviceDetailsRetrofitCallback
             )

         viewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)

         */
        //binding?.viewModel = viewModel

        //This is default Map
        //Setting the Default Map Value with the current version code
        /* if (BuildConfig.FLAVOR == "puneprod") {*/
        /* firebaseDefaultMap = HashMap()
         firebaseDefaultMap!![VERSION_CODE_KEY] = getCurrentVersionCode()
         mFirebaseRemoteConfig.setDefaultsAsync(firebaseDefaultMap!! as Map<String, Any>)
         //Setting that default Map to Firebase Remote Config
         //Setting Developer Mode enabled to fast retrieve the values
         *//* mFirebaseRemoteConfig.setConfigSettings(
             FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
                 .build()
         )*//*
        //Fetching the values here
        mFirebaseRemoteConfig.fetch()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mFirebaseRemoteConfig.activateFetched()

                    //calling function to check if new version is available or not
                    checkForUpdate()
                } else {
                    Toast.makeText(
                        this, "Something went wrong please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }*/
    }

//    private fun checkForUpdate() {
//        val latestAppVersion = mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY).toInt()
//        if (latestAppVersion > getCurrentVersionCode()) {
//            val dialogBuilder = android.app.AlertDialog.Builder(this)
//            dialogBuilder.setTitle("Please Update the App")
//            dialogBuilder.setMessage("A new version of this app is available. Please update it")
//                .setCancelable(false)
//                .setPositiveButton("Update") { dialog, _ ->
//                    dialog.dismiss()
//                    navigateToPlayStore()
//                }
//            val alert = dialogBuilder.create()
//            alert.setOnShowListener {
//                val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
//                btnPositive.textSize = 16F
//                btnPositive.typeface = ResourcesCompat.getFont(this, R.font.poppins)
//                val btnNegative: Button = alert.getButton(Dialog.BUTTON_NEGATIVE)
//                btnNegative.textSize = 16F
//                btnNegative.typeface = ResourcesCompat.getFont(this, R.font.poppins)
//            }
//            alert.show()
//
//        } else {
//            mDelayHandler = Handler()
//            mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
//        }
//    }


    private fun getCurrentVersionCode(): Int {
        try {
            return packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }


    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

    private fun navigateToPlayStore() {
        val uri = Uri.parse("market://details?id=" + packageName)
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
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }


}
