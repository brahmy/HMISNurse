package com.oss.digihealth.nur.application

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.oss.digihealth.nur.api_service.APIService
import java.util.*


class HmisApplication : MultiDexApplication() {



    init {
        instance = this
    }


    private var mRetrofitService: APIService? = null
//    private var fireBaseAnalyticsManager: FirebaseAnalyticsManager? = null


    private val id: String = "1"
    private val name: String = "HmisApplication"
    override fun onCreate() {
        super.onCreate()
        app = this
        instance = this
        FirebaseApp.initializeApp(this)
/*

        //Injection used koin module instead of dagger
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@HmisApplication)
            loadKoinModules(REQUIRED_MODULE)
        }
*/



        // Obtain the FirebaseAnalytics instance.
        // need to remove in production
        // Stetho.initializeWithDefaults(this);

        // Obtain the FirebaseAnalytics instance.


        // Create a new Locale object
        // Create a new Locale object
        val locale = Locale("ta")
        Locale.setDefault(locale)
        // Create a new configuration object
        // Create a new configuration object
        val config = Configuration()
        // Set the locale of the new configuration
        // Set the locale of the new configuration
        config.locale = locale
        // Update the configuration of the Accplication context
        // Update the configuration of the Accplication context
        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )

    }
    fun getRetrofitService(): APIService? {
        if (mRetrofitService == null) {
            mRetrofitService = APIService.Factory.create(applicationContext)
        }
        return mRetrofitService
    }
/*
    val fireBaseManager: FirebaseAnalyticsManager
        get() {
            if (fireBaseAnalyticsManager == null)
                fireBaseAnalyticsManager =
                    FirebaseAnalyticsManager(this)
            return fireBaseAnalyticsManager as FirebaseAnalyticsManager
        }

*/

    companion object {
        lateinit var instance: HmisApplication

        fun applicationContext(): Context {
            return instance.applicationContext
        }

        fun get(context: Context): HmisApplication {
            return context.applicationContext as HmisApplication
        }

        private var app: HmisApplication? = null
        fun app(): HmisApplication = app!!

        fun isConnected(): Boolean {
            val connectivityManager =
                app!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null
        }
/*

        val REQUIRED_MODULE = listOf(
            NETWORKING_MODULE,
            REPOSITORY_MODULE,
            VIEW_MODEL_MODULE
        )


        var selectedDoctorByPatient: DoctorList? = null
        var selectedSlotByPatient: DoctorSlotList? = null
        var paymentSuccess : Boolean = false
    }
    */
    }



}