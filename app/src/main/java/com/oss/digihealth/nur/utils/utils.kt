package com.oss.digihealth.doc.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.oss.digihealth.nur.config.AppConstants
import com.oss.digihealth.nur.config.AppPreferences
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.URISyntaxException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Stream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.reflect.KProperty1

class Utils(val context: Context) {

    val appPreferences = AppPreferences.getInstance(context, AppConstants.SHARE_PREFERENCE_NAME)

    companion object {


        fun isNetworkConnected(application: Context): Boolean {
            val cm =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < 23) {
                val ni = cm.activeNetworkInfo
                if (ni != null) {
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    ) || nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                }
            }

            return false
        }

        fun isConnectedWifi(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.type === ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true
                }
            } else {
                // not connected to the internet
                return false
            }
            return false
        }


        fun isConnectedInternet(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.type === ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true
                }
            } else {
                // not connected to the internet
                return false
            }
            return false
        }

        /**
         * Expand View
         *
         * @param v
         */


        fun stringToWords(mnemonic: String): List<String> {
            val words = ArrayList<String>()
            for (word in mnemonic.trim { it <= ' ' }.split(",".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()) {
                if (word.isNotEmpty()) {
                    words.add(word)
                }
            }
            return words
        }

        fun stringToIntArray(mnemonic: String): List<Int> {
            val words = ArrayList<Int>()
            for (word in mnemonic.trim { it <= ' ' }.split(",".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()) {
                if (word.isNotEmpty()) {
                    words.add(word.trim().toInt())
                }
            }
            return words
        }


        fun stringToInt(mnemonic: String): Int {
            val words = ArrayList<Int>()

            //  var ret:Double= 0.0

            var ret: Int = 0


            for (word in mnemonic.trim { it <= ' ' }.split("-".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()) {
                if (word.isNotEmpty()) {

                    try {
                        words.add(word.trim().toInt())
                        ret += word.trim().toInt()

                    } catch (e: Exception) {

                    }
                }
            }
            return ret
        }

        fun stringToWords(mnemonic: String, sub: String): List<String> {
            val words = ArrayList<String>()
            for (word in mnemonic.trim { it <= ' ' }.split(sub.toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()) {
                if (word.isNotEmpty()) {
                    words.add(word)
                }
            }
            return words
        }


        fun expand(horizontalScrollView: View) {


            val matchParentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(
                    (horizontalScrollView.parent as View).width,
                    View.MeasureSpec.EXACTLY
                )
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            horizontalScrollView.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            val targetHeight = horizontalScrollView.measuredHeight
            horizontalScrollView.layoutParams.height = 1
            horizontalScrollView.visibility = View.VISIBLE
            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    horizontalScrollView.layoutParams.height = if (interpolatedTime == 1f)
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    else
                        (targetHeight * interpolatedTime).toInt()
                    horizontalScrollView.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.duration =
                (targetHeight / horizontalScrollView.context.resources.displayMetrics.density).toInt()
                    .toLong()
            horizontalScrollView.startAnimation(a)
        }


        /**
         * Collapsing Views
         *
         * @param v
         */
        fun collapse(v: View) {
            val initialHeight = v.measuredHeight
            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.duration =
                (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
            v.startAnimation(a)
        }


        @SuppressLint("SimpleDateFormat")
        fun parseDate(
            inputDateString: String,
            inputDateFormat: String,
            outputDateFormat: String
        ): String? {
            val date: Date?
            var outputDateString: String? = ""
            try {
                date = SimpleDateFormat(inputDateFormat).parse(inputDateString)
                outputDateString = SimpleDateFormat(outputDateFormat).format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return outputDateString
        }


        fun encrypt(value: String?): String {
            var encode: String? = null
            val iv = IvParameterSpec(AppConstants.IV.toByteArray(Charsets.UTF_8))

            val skeySpec = SecretKeySpec(
                AppConstants.ENCRYPT_KEY.toByteArray(Charsets.UTF_8),
                AppConstants.ALGORITHAM
            )

            val cipher = Cipher.getInstance(AppConstants.AES_SELECTED_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            val encrypted = cipher?.doFinal(value?.toByteArray())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encode = Base64.getEncoder().encodeToString(encrypted)
            } else {
                encode = android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT)
                encode = encode.replace("\n", "")

            }
            Log.i("", "" + encode)
            return encode!!
        }
    }

    fun showToast(color: Int? = null, view: View? = null, message: String) {
        /* val rect = Rect()
         view.getLocalVisibleRect(rect)
         val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
         val layout = inflater.inflate(R.layout.custom_toast, null)
         val customToastLayout = layout.findViewById(R.id.customToastLayout) as LinearLayout
         customToastLayout.setBackgroundColor(ContextCompat.getColor(context, color))
         val toastMessageTextView = layout.findViewById(R.id.toastMessageTextView) as TextView
         val toast = Toast(context)
         toast.duration = Toast.LENGTH_SHORT
         toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, rect.left, rect.top)
         toast.view = layout
         toastMessageTextView.text = message
         toast.show()*/
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }


    fun getAgeMonth(DOBMonth: Int): String {
        var age: Int
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        val c = Calendar.getInstance()

        c.time = Date()

        c.add(Calendar.MONTH, -DOBMonth)

        val d = c.time
        val res = format.format(d)


        return res
    }

    fun getAgedayDifferent(numOfDaysAgo: Int): String {
        var age: Int
        val format = SimpleDateFormat("yyyy-MM-dd")

        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DAY_OF_YEAR, -1 * numOfDaysAgo)
        val d = c.time
        val res = format.format(d)
        return res
    }

    fun getDateDaysAgo(numOfDaysAgo: Int): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DAY_OF_YEAR, -1 * numOfDaysAgo)
        val d = c.time
        val res = format.format(d)
        return res
    }

    fun getYear(numOfDaysAgo: Int): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.YEAR, -numOfDaysAgo)
        val d = c.time
        val res = format.format(d)
        return res
    }

    fun reqDate(selectedDate: String): String {
        val sdf1 = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
        val dateObj1 = sdf1.parse(selectedDate)
        var tar = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(dateObj1)
        tar = tar + "Z"
        return tar
    }

    fun historyISOdate(isoDate: String): String {
        val sdfISO = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val dateObjISO = sdfISO.parse(isoDate)
        val tarISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(dateObjISO!!)
        return tarISO
    }

    fun getDocumentDate(documentInputDate: String): String {
        val sdf1 = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
        val dateObj1 = sdf1.parse(documentInputDate)
        val targetDate = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss zz").format(dateObj1)

        return targetDate
    }

    fun currentDate(): String {

        val date = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(date.time)

        return currentDate

    }

    fun compareDate(inputDate: String): String {

        val opFormat = SimpleDateFormat("yyyy-MM-dd")
        val ipFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = ipFormat.parse(inputDate)
        val compareDate = opFormat.format(date)

        return compareDate
    }

/*
    @RequiresApi(Build.VERSION_CODES.N)
    fun containsName(
        list: List<FavAddAllDepatResponseContent>,
        name: String
    ): Stream<KProperty1<FavAddAllDepatResponseContent, Int>>? {
        return list.stream().map { FavAddAllDepatResponseContent::uuid }.filter(name::equals)
    }
*/


    fun getMimeType(uri: Uri): String? {
        var mimeType: String? = null
        mimeType = if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            val cr: ContentResolver = context.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension: String = MimeTypeMap.getFileExtensionFromUrl(
                uri
                    .toString()
            )
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase()
            )
        }
        return mimeType
    }

    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: java.lang.Exception) {
                // Eat it
            }


        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getKeyFromValue(list: ArrayList<String>, value: Any): Int {
        for (i in list.indices) {
            if (list[i] == value) return i
        }
        return 0
    }

    fun getCurrentDateTime(format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(Date())
    }

    fun fromISO8601UTC(dateStr: String?): Date? {
        val tz = TimeZone.getTimeZone("UTC")
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
        df.timeZone = tz
        try {
            return df.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    var serverdateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    fun convertServerDateToUserTimeZone(serverDate: String?): String? {
        var ourdate: String
        try {
            val formatter =
                SimpleDateFormat(serverdateFormat, Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(serverDate)
            val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val dateFormatter =
                SimpleDateFormat(serverdateFormat, Locale.ENGLISH) //this format changeable
            dateFormatter.timeZone = timeZone
            ourdate = dateFormatter.format(value)

            //Log.d("OurDate", OurDate);
        } catch (e: Exception) {
            ourdate = "0000-00-00 00:00:00"
        }
        return ourdate
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    @SuppressLint("ObsoleteSdkInt")
    fun getLocaleStringResource(
        requestedLocale: Locale?,
        resourceId: Int,
        context: Context
    ): String? {
        val result: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // use latest api
            val config = Configuration(context.resources.configuration)
            config.setLocale(requestedLocale)
            result = context.createConfigurationContext(config).getText(resourceId).toString()
        } else { // support older android versions
            val resources: Resources = context.resources
            val conf: Configuration = resources.configuration
            val savedLocale: Locale = conf.locale
            conf.locale = requestedLocale
            resources.updateConfiguration(conf, null)

            // retrieve resources from desired locale
            result = resources.getString(resourceId)

            // restore original locale
            conf.locale = savedLocale
            resources.updateConfiguration(conf, null)
        }
        return result
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun getDate(ourDate: String): String? {
        var ourDate1: String?
        try {
            val formatter =
                SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)
            val dateFormatter =
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.getDefault()
                ) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourDate1 = dateFormatter.format(value!!)

            //Log.d("ourDate", ourDate);
        } catch (e: java.lang.Exception) {
            ourDate1 = "00-00-0000 00:00"
        }
        return ourDate1
    }

    fun datecompare(fromdate: String?, todate: String?): Boolean {
        var check: Boolean = false
        val df: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
        try {
            val fromdatevalue: Date? = df.parse(fromdate!!)
            val todatevalue: Date? = df.parse(todate!!)
            check = todatevalue?.after(fromdatevalue)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return check
    }

    fun setCalendarLocale(language: String, context: Context) {
        val languageToLoad = language // your language
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

/*
    fun getPathfolder(activity: Activity, uri: Uri?): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(activity, uri)) {
            when {
                DocumentChildFragment.isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    return Environment.getExternalStorageDirectory()
                        .toString() + "/" + split[1]
                }
                DocumentChildFragment.isDownloadsDocument(uri) -> {
                    val id = DocumentsContract.getDocumentId(uri)
                    uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                }
                DocumentChildFragment.isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    when (type) {
                        "image" -> {
                            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
        }
        if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            val projection =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor?
            try {
                cursor = activity.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val columnIndex: Int
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    if (cursor.moveToFirst()) {
                        return cursor.getString(columnIndex)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }
*/

    /**
     * This method is used to convert given date format to another date format
     *
     * @param givenDate A string which has the given date
     * @param givenDateFormat A format in which the given date exists
     * @param toDateFormat A format in which the return date should be
     * @return A string value of the equivalent date in the required format
     */

    fun convertDateFormat(
        givenDate: String,
        givenDateFormat: String,
        toDateFormat: String
    ): String {
        return try {
            val dt = SimpleDateFormat(givenDateFormat, Locale.getDefault())
            val dateToDisplay = dt.parse(givenDate)
            val dt1 = SimpleDateFormat(toDateFormat, Locale.getDefault())
            dt1.format(dateToDisplay)
        } catch (e: Exception) {
            givenDate
        }
    }

    fun displayDate(
        givenDate: String,
        givenDateFormat: String
    ): String {
        return if (givenDate.substring(0, 4) == "0000" &&
            givenDateFormat.substring(0, 4) == "yyyy"
        ) {
            ""
        } else {
            convertDateFormat(givenDate, givenDateFormat, "dd-MMM-yyyy hh:mm a")
        }
    }

    fun displayTime(
        givenDate: String,
        givenDateFormat: String
    ): String {
        return convertDateFormat(givenDate, givenDateFormat, "hh:mm a")
    }

    fun ipDashboardDate(
        givenDate: String,
        givenDateFormat: String
    ): String {

        return convertDateFormat(givenDate, givenDateFormat, "MMM-dd")
    }

    fun emrDisplayDate(
        givenDate: String,
        givenDateFormat: String
    ): String {
        return convertDateFormat(givenDate, givenDateFormat, "dd-MMM-yyyy")
    }

    fun getEncounterType(): String {
        var type: String = ""
        val encounter = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)

        if (encounter == 1) {
            type = AppConstants.OUT_PATIENT
        } else if (encounter == 2) {
            type = AppConstants.IN_PATIENT
        }
        return type
    }

    fun getWorkFlow(): String {
        var workflow = ""
        val encounter = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        workflow = when (encounter) {
            1 -> AppConstants.OUT_PATIENT
            2 -> AppConstants.IN_PATIENT
            else -> ""
        }
        return workflow
    }

    fun isTablet(context: Context): Boolean {
        val xlarge = context.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == 4
        val large = context.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
        return xlarge || large
    }

    fun convertUtcToIst(
        givenDate: String,
        givenDateFormat: String
    ): String {
        return try {
            val utcFormat: DateFormat = SimpleDateFormat(givenDateFormat, Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val indianFormat: DateFormat = SimpleDateFormat(givenDateFormat, Locale.getDefault())
            indianFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val timestamp = utcFormat.parse(givenDate)
            val output: String = indianFormat.format(timestamp)
            output
        } catch (e: Exception) {
            givenDate
        }
    }

    fun convertIstToUtc(
        givenDate: String,
        givenDateFormat: String
    ): String {
        return try {
            val utcFormat: DateFormat = SimpleDateFormat(givenDateFormat, Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val indianFormat: DateFormat = SimpleDateFormat(givenDateFormat, Locale.getDefault())
            indianFormat.timeZone = TimeZone.getTimeZone("moment-timezone")
            val timestamp = utcFormat.parse(givenDate)
            val output: String = indianFormat.format(timestamp)
            output
        } catch (e: Exception) {
            givenDate
        }
    }

    fun displayInvesDate(
        givenDate: String,
        givenDateFormat: String
    ): String {
        return convertDateFormat(givenDate, givenDateFormat, "dd-MMM-yyyy hh:mm a")
    }

    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr: String = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
        } // for now eat exceptions
        return ""
    }

    @Throws(IOException::class)
    fun convertFileToBytes(file: File): ByteArray? {
        val b = file.readBytes()
        return b
    }

    fun showDialog(
        context: Context,
        title: String,
        msg: String,
        positiveButtonText: String,
        negativeButtonText: String,
        positiveButtonListener: ((dialog: DialogInterface, which: Int) -> Unit)?,
        negativeButtonListener: ((dialog: DialogInterface, which: Int) -> Unit)?
    ) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setIcon(android.R.drawable.ic_dialog_alert)

        positiveButtonListener?.let {
            dialog.setPositiveButton(positiveButtonText) { dialog, which ->
                positiveButtonListener(dialog, which)
            }
        }

        negativeButtonListener?.let {
            dialog.setNegativeButton(negativeButtonText) { dialog, which ->
                negativeButtonListener(dialog, which)
            }
        }

        dialog.show()
    }
}