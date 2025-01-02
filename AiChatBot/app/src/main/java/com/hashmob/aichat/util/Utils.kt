package com.hashmob.aichat.util

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.TypedArray
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ResultReceiver
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Settings.Secure
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.google.android.gms.ads.AdSize
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hashmob.aichat.BuildConfig
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseApplication.Companion.preferences
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.model.firebase.data.GenerateImageService
import com.hashmob.aichat.data.model.firebase.data.all_service
import com.hashmob.aichat.main.home.premium.PremiumActivity
import com.hashmob.aichat.main.home.ui.drawer.PrivacyPolicyActivity
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.factories.GreekAlphabetFactory.getString
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


class Utils {


    public fun getRealPathFromURI(ctx: Context, contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = ctx.contentResolver.query(
            contentUri, null, null, null, null
        )
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }

    companion object {
        const val NUMBERS_AND_LETTERS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val NUMBERS = "0123456789"
        const val LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"
        public const val TAG = "Utils"
        public val DIGITS_LOWER = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        )
        public const val PREFERENCE_NAME = "GLOBAL_DATA"

        /**
         * ***************************************************************************
         * ****************************** MESSAGE UTILS ******************************
         * ***************************************************************************
         */
        fun setErrorOnTextInputLayout(textInputLayout: TextInputLayout?, text: String?) {
            if (textInputLayout != null) {
                if (text != null) {
                    textInputLayout.setErrorEnabled(true)
                    textInputLayout.setError(text)
                } else {
                    textInputLayout.setError(null)
                    textInputLayout.setErrorEnabled(false)
                }
            }
        }

        //Show Toast
        fun makeToast(ctx: Context?, text: String?) {
            if (!isEmpty(text)) {
                Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show()
            }
        }

        //SnackBar
        fun showSnackBar(text: String?, view: View?) {
            if (view != null) {
                val snackbar: Snackbar
                snackbar = Snackbar.make(view, text.toString(), Snackbar.LENGTH_SHORT)
                val snackBarView: View = snackbar.getView()
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context, R.color.colorPrimary
                    )
                )
                val textView: TextView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
                textView.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                snackbar.show()
            }
        }

        //SnackBar
        fun showSnackBar(text: String?, activity: Activity?) {
            if (activity != null && !activity.isFinishing() && activity.findViewById<View>(android.R.id.content) != null) {
                val snackbar: Snackbar
                snackbar = Snackbar.make(
                    activity.findViewById<View>(android.R.id.content),
                    text.toString(),
                    Snackbar.LENGTH_SHORT
                )
                val snackBarView: View = snackbar.getView()
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        activity, R.color.white
                    )
                )
                val textView: TextView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
                textView.setTextColor(ContextCompat.getColor(activity, R.color.black))
                snackbar.show()
            }
        }

        //SnackBarIndefinite
        fun showSnackBarIndefinite(text: String?, view: View?) {
            view?.let { Snackbar.make(it, text.toString(), Snackbar.LENGTH_INDEFINITE).show() }
        }

        // -------------------------------------------------//
        // -----------Show Custom Dialog-------------------//
        // -------------------------------------------------//
        open fun showAlert(msg: String, ctx: Context) {
            try {
                val alertDialogBuilder = AlertDialog.Builder(
                    ctx, R.style.MyAlertDialogStyle
                )
                alertDialogBuilder.setMessage("" + msg).setCancelable(false).setPositiveButton(
                    ctx.resources.getString(R.string.ok)
                ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * ***************************************************************************
         * **************************** VALIDATION UTILS *****************************
         * ***************************************************************************
         */
        fun isValidEmail(email: String?): Boolean {
            val check: Boolean
            val p: Pattern
            val m: Matcher
            val EMAIL_STRING =
                ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
            p = Pattern.compile(EMAIL_STRING)
            m = p.matcher(email)
            check = m.matches()
            return check
        }

        fun isValidPassword(password: String?): Boolean {
            val check: Boolean
            val p: Pattern
            val m: Matcher
            val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
            p = Pattern.compile(passwordRegex)
            m = p.matcher(password)
            check = m.matches()
            return check
        }

        fun isValidName(name: String?): Boolean {
            val check: Boolean
            val p: Pattern
            val m: Matcher
            val NAME_STRING = "[a-zA-z]+([ '-][a-zA-Z]+)*"
            p = Pattern.compile(NAME_STRING)
            m = p.matcher(name)
            check = m.matches()
            return check
        }

        fun isEmpty(str: CharSequence?): Boolean {
            return str == null || str.length == 0
        }

        //attachment file validation
        fun checkFileIsAttachmentFile(str: String?): Boolean {
            val check: Boolean
            val p: Pattern
            val m: Matcher
            // String PHONE_STRING = "^.*\.(jpg|JPG|gif|GIF|doc|DOC|pdf|PDF)$";
            val FILE_STRING = "^.*\\.(doc|DOC|pdf|PDF)$"
            p = Pattern.compile(FILE_STRING)
            m = p.matcher(str)
            check = m.matches()
            return check
        }

        fun getFileSize(size: Long): String {
            if (size <= 0) return "0"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(
                size / Math.pow(
                    1024.0, digitGroups.toDouble()
                )
            ) + " " + units[digitGroups]
        }

        @JvmOverloads
        fun removeLastChar(str: String, sign: Char = ','): String {
            var str = str
            if (!str.equals(
                    "", ignoreCase = true
                ) && str.length > 0 && str[str.length - 1] == sign
            ) {
                str = str.substring(0, str.length - 1)
            }
            return str
        }

        /**
         * ***************************************************************************
         * **************************** KEYBOARD UTILS *****************************
         * ***************************************************************************
         */
        //hide keyboard
        fun hideKeyBoard(editText: EditText) {
            val inputMethodManager = editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0)
        }

        fun hideKeyBoard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view: View? = activity.getCurrentFocus()
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            assert(imm != null)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun hideSoftKeyboard(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        @JvmOverloads
        fun showSoftKeyboard(view: View, resultReceiver: ResultReceiver? = null) {
            val config = view.context.resources.configuration
            if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
                val imm =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (resultReceiver != null) {
                    imm.showSoftInput(
                        view, InputMethodManager.SHOW_IMPLICIT, resultReceiver
                    )
                } else {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        fun changeKeyboardFocus(editText: EditText) {
            editText.requestFocus()
            val imm = editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }

        fun isInternetOn(ctx: Context): Boolean {
            var connected = false
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            if (netInfo != null && netInfo.isConnectedOrConnecting) {
                connected = true
            } else if (netInfo != null && netInfo.isConnected && cm.activeNetworkInfo!!.isAvailable) {
                connected = true
            } else if (netInfo != null && netInfo.isConnected) {
                try {
                    val url = URL("http://www.google.com")
                    val urlc = url.openConnection() as HttpURLConnection
                    urlc.connectTimeout = 3000
                    urlc.connect()
                    if (urlc.responseCode == 200) {
                        connected = true
                    }
                } catch (e1: MalformedURLException) {
                    e1.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (cm != null) {
                val netInfoAll = cm.allNetworkInfo
                for (ni in netInfoAll) {
                    println("get network type :::" + ni.typeName)
                    if ((ni.typeName.equals(
                            "WIFI",
                            ignoreCase = true
                        ) || ni.typeName.equals(
                            "MOBILE",
                            ignoreCase = true
                        )) && ni.isConnected && ni.isAvailable
                    ) {
                        connected = true
                        if (connected) {
                            break
                        }
                    }
                }
            }
            return connected
        }

        /**
         * ***************************************************************************
         * ******************************* DATE UTILS ********************************
         * ***************************************************************************
         */
        fun GetDateOnRequireFormat(
            date: String?, givenformat: String?, resultformat: String?
        ): String {
            var result = ""
            var sdf: SimpleDateFormat?
            var sdf1: SimpleDateFormat?
            try {
                if (date != null) {
                    sdf = SimpleDateFormat(givenformat, Locale.US)
                    sdf1 = SimpleDateFormat(resultformat, Locale.US)
                    result = sdf1.format(sdf.parse(date))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            } finally {
                sdf = null
                sdf1 = null
            }
            return result
        }

        public fun getTimeDistanceInMinutesForTotalTime(time: Long, now: Long): Int {
            val timeDistance = now - time
            return Math.round((Math.abs(timeDistance) / 1000 / 60).toFloat())
        }

        public fun getTimeDistanceInMinutes(time: Long): Int {
            val timeDistance = currentDate().time - time
            return Math.round((Math.abs(timeDistance) / 1000 / 60).toFloat())
        }

        fun getDateFromTimeStampInDate(milliSeconds: String?): Calendar {
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = java.lang.Long.valueOf(milliSeconds)
            return calendar
        }

        fun currentDate(): Date {/* TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(Constants.DATE_FULL_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        return calendar.getTime();*/
            val dateFormatGmt = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.ENGLISH)
            dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT")

//Local time zone
            val dateFormatLocal = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.ENGLISH)

//Time in GMT
            /*   try {
            return dateFormatLocal.parse(dateFormatGmt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
            //return new Date();
            return Calendar.getInstance().time
        }

        fun checkTimeBetweenTwoTimes(
            strCTime: String?, fromTime: String?, toTime: String?, format: String?
        ): Boolean {
            try {
                val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
                val time1 = dateFormat.parse(fromTime)
                val calendar1 = Calendar.getInstance()
                calendar1.time = time1
                val time2 = dateFormat.parse(toTime)
                val calendar2 = Calendar.getInstance()
                calendar2.time = time2
                val d = dateFormat.parse(strCTime)
                val calendar3 = Calendar.getInstance()
                calendar3.time = d
                val x = calendar3.time
                if (x.after(calendar1.time) && x.before(calendar2.time)) {
                    return true
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return false
        }

        fun dateIsBeforeThanProvidedDate(
            date_first: String, date_second: String, dateformat: String?
        ): Boolean {
            var is_before = false
            try {
                val sdf = SimpleDateFormat(dateformat)
                val date1 = sdf.parse(date_first)
                val date2 = sdf.parse(date_second)
                println(sdf.format(date1))
                println(sdf.format(date2))
                if (date1.compareTo(date2) > 0) {
                    is_before = true
                }
                LogUtils.Print(
                    "DateIsBeforeThanProvidedDate",
                    "date1.compareTo(date2)..." + date1.compareTo(date2)
                )
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
            LogUtils.Print("is_before", "is_before...$is_before")
            LogUtils.Print("DateIsBeforeThanProvidedDate", "date_first...$date_first")
            LogUtils.Print(
                "DateIsBeforeThanProvidedDate", "date_second...$date_second"
            )
            return is_before
        }

        fun dateIsBeforeStrickThanProvidedDate(
            date_first: String?, date_second: String?, dateformat: String?
        ): Boolean {
            var is_before = false
            try {
                val sdf = SimpleDateFormat(dateformat)
                val date1 = sdf.parse(date_first)
                val date2 = sdf.parse(date_second)
                println(sdf.format(date1))
                println(sdf.format(date2))
                if (date1.compareTo(date2) >= 0) {
                    is_before = true
                }
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
            return is_before
        }

        fun dateIsBeforeThanOrEqualProvidedDate(
            date_first: String?, date_second: String?, dateformat: String?
        ): Boolean {
            var is_before = false
            try {
                val sdf = SimpleDateFormat(dateformat)
                val date1 = sdf.parse(date_first)
                val date2 = sdf.parse(date_second)
                println(sdf.format(date1))
                println(sdf.format(date2))
                if (date1.compareTo(date2) <= 0) {
                    is_before = true
                }
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
            return is_before
        }

        fun isEqualDate(date_first: String?, date_second: String?, dateformat: String?): Boolean {
            var is_equal = false
            try {
                val sdf = SimpleDateFormat(dateformat)
                val date1 = sdf.parse(date_first)
                val date2 = sdf.parse(date_second)
                println(sdf.format(date1))
                println(sdf.format(date2))
                if (date1.compareTo(date2) == 0) {
                    is_equal = true
                }
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
            return is_equal
        }

        fun createUniqueID(): String {
            val cDate = Date()
            return SimpleDateFormat(Constants.DATE_PUSH_NOTIFICATION_FORMAT, Locale.ENGLISH).format(
                cDate
            )
        }

        fun GetCurrentTimeStamp(): String {
            val tsLong = System.currentTimeMillis()
            return tsLong.toString()
        }

        fun GetCurrentTimeStampLong(): Long {
            return System.currentTimeMillis()
        }

        fun GetCurrentTimeStampInLong(): Long {
            return System.currentTimeMillis()
        }

        fun GetCurrentUTCTimeStamp(): Long {
            val sdf = SimpleDateFormat(Constants.DATE_DD_MM_YYYY_FORMAT, Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            var date: Date? = null
            try {
                date = sdf.parse(sdf.format(Date()))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date?.time ?: 0
        }

        fun getOneDayMinusFromStringDate(strDate: String?, format: String?): Date {
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            var date: Date? = null
            try {
                date = sdf.parse(strDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.SECOND, 15)
            cal.add(Calendar.DATE, -1)
            LogUtils.Print(TAG, "One day --> " + cal.time)
            return cal.time
        }

        fun getDateFromString(strDate: String?, format: String?): Date? {
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            var date: Date? = null
            try {
                date = sdf.parse(strDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date
        }

        fun getWeekDayFromString(
            strDate: String?, dateFormat: String?, dayFormat: String?
        ): String {
            val inFormat = SimpleDateFormat(dateFormat)
            var date: Date? = null
            try {
                date = inFormat.parse(strDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val outFormat = SimpleDateFormat(dayFormat)
            return outFormat.format(date)
        }

        fun gmtToLocalDate(time: Long): Long {
//        String timeZone = Calendar.getInstance().getTimeZone().getID();
//        Date local = new Date(time + TimeZone.getTimeZone(timeZone).getOffset(time));
//        return local.getTime();
            val df = SimpleDateFormat(Constants.DATE_DD_MM_YYYY_FORMAT, Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            var date = calendar.time
            df.timeZone = TimeZone.getDefault()
            try {
                date = df.parse(df.format(date))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date?.time ?: 0
        }


        fun gmtToLocalDateInString(time: Long): String {
            val df = SimpleDateFormat(Constants.DATE_DD_MM_YYYY_FORMAT, Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            val date = calendar.time
            df.timeZone = TimeZone.getDefault()
            return df.format(date)
        }

        fun GetDefaultTimeZone(): String {
            val tz = TimeZone.getDefault()
            return try {
                "" + tz.id
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        fun GetCurrentNanoTime(): String {
            val tsLong = System.nanoTime()
            return tsLong.toString()
        }

        fun GetDateFromTimeStamp(milliSeconds: Long, dateFormat: String?): String {
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        fun getTimeFromMilliSeconds(milliSeconds: Long): String {
            // long minutes = (milliseconds / 1000) / 60;
            val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
            // long seconds = (milliseconds / 1000);
            val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
            return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
        }

        fun addDayInDate(dt: String?, dateFormat: String?, num: Int): String {
            val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val c = Calendar.getInstance()
            try {
                c.time = sdf.parse(dt)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            c.add(Calendar.DATE, 1)
            return sdf.format(c.time)
        }

        fun getnthDayOfCurrentMonth(day: Int): Calendar {
            val c = Calendar.getInstance() // this takes current date
            c[Calendar.DAY_OF_MONTH] = day
            println(c.time) // this returns java.util.Date
            LogUtils.Print(TAG, "getFirstDayOfCurrentMonth --> " + c.time)
            return c
        }

        // this takes current date
        // this returns java.util.Date
        val lastDayOfCurrentMonth: Calendar
            get() {
                val c = Calendar.getInstance() // this takes current date
                c[Calendar.DAY_OF_MONTH] =
                    Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
                println(c.time) // this returns java.util.Date
                LogUtils.Print(TAG, "getLastDayOfCurrentMonth --> " + c.time)
                return c
            }

        fun getStringDateFromCalendar(calendar: Calendar, inputFormat: String?): String {
            val format1 = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            LogUtils.Print(TAG, "getStringDateFromCalendar --> " + calendar.time)
            return format1.format(calendar.time)
        }

        fun getDateFromLocalDate(localDate: LocalDate, dateFormat: String?): String {
            try {
                val df = SimpleDateFormat(dateFormat, Locale.ENGLISH)
                val date = SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(localDate.toString())
                df.timeZone = TimeZone.getDefault()
                return df.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        fun getCountAction(context: Context?, completed: String, total: String): String {
            return "$completed/$total"
        }

        fun parseDate(date: String?, inputFormat: String?): Date {
            val inputParser = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            return try {
                inputParser.parse(date)
            } catch (e: ParseException) {
                Date(0)
            }
        }

        fun parseCalendar(date: String?, inputFormat: String?): Calendar? {
            val sdf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            var cal: Calendar? = null
            try {
                cal = Calendar.getInstance()
                cal.time = sdf.parse(date) // all done
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return cal
        }

        fun getAppName(context: Context, packageName: String?): String {
            var packageName = packageName
            val applicationName: String
            if (packageName == null) {
                packageName = context.packageName
            }
            applicationName = try {
                val packageManager: PackageManager = context.packageManager
                val packageInfo: PackageInfo = packageManager.getPackageInfo(
                    packageName!!, 0
                )
                context.getString(packageInfo.applicationInfo.labelRes)
            } catch (e: Exception) {
                Log.w("error", "Failed to get version number.$e")
                ""
            }
            return applicationName
        }

        fun getAppVersionNumber(context: Context, packageName: String?): String {
            var packageName = packageName
            val versionName: String
            if (packageName == null) {
                packageName = context.packageName
            }
            versionName = try {
                val packageManager: PackageManager = context.packageManager
                val packageInfo: PackageInfo = packageManager.getPackageInfo(
                    packageName!!, 0
                )
                packageInfo.versionName
            } catch (e: Exception) {
                // Log.e("Failed to get version number.",""+e);
                e.printStackTrace()
                ""
            }
            return versionName
        }

        // -------------------------------------------------//
        // ------------ parseDate----------------------//
        // -------------------------------------------------//
        fun getAppVersionCode(context: Context, packageName: String?): String {
            var packageName = packageName
            val versionCode: String
            if (packageName == null) {
                packageName = context.packageName
            }
            versionCode = try {
                val packageManager: PackageManager = context.packageManager
                val packageInfo: PackageInfo = packageManager.getPackageInfo(
                    packageName!!, 0
                )
                Integer.toString(packageInfo.versionCode)
            } catch (e: Exception) {
                Log.w("Failed ", e)
                ""
            }
            return versionCode
        }

        val sdkVersion: Int
            get() = try {
                Build.VERSION::class.java.getField("SDK_INT").getInt(null)
            } catch (e: Exception) {
                3
            }
        val isEmulator: Boolean
            get() = Build.MODEL == "sdk" || Build.MODEL == "google_sdk"

        /**
         * ***************************************************************************
         * ************************* NAVIGATE INTENT UTILS ***************************
         * ***************************************************************************
         */
        fun startWebActivity(context: Context, url: String?) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            context.startActivity(intent)
        }

        fun startWebSearchActivity(context: Context, url: String?) {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, url)
            context.startActivity(intent)
        }

        fun startEmailActivity(
            context: Context, toResId: Int, subjectResId: Int, bodyResId: Int
        ) {
            startEmailActivity(
                context,
                context.getString(toResId),
                context.getString(subjectResId),
                context.getString(bodyResId)
            )
        }

        fun startEmailActivity(
            context: Context, to: String, subject: String?, body: String?
        ) {
            val intent = Intent(Intent.ACTION_SEND)
            //intent.setType("message/rfc822");
            intent.setType("text/plain")
            if (!TextUtils.isEmpty(to)) {
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            }
            if (!TextUtils.isEmpty(subject)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            if (!TextUtils.isEmpty(body)) {
                intent.putExtra(Intent.EXTRA_TEXT, body)
            }
            val pm: PackageManager = context.packageManager
            try {
                if (pm.queryIntentActivities(
                        intent, PackageManager.MATCH_DEFAULT_ONLY
                    ).size == 0
                ) {
                    intent.setType("text/plain")
                }
            } catch (e: Exception) {
                Log.w("Error.", e)
            }
            context.startActivity(intent)
        }

        fun startGoogleMapNavigationActivity(
            context: Context,
            source_lat: Double,
            source_lng: Double,
            dest_lat: Double,
            dest_lng: Double
        ) {
            var lat2: Double
            var lng2: Double
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "http://maps.google.com/maps?" + "saddr=" + source_lat + "," + source_lng + "&daddr=" + dest_lat + "," + dest_lng
                )
            )
            intent.setClassName(
                "com.google.android.apps.maps", "com.google.android.maps.MapsActivity"
            )
            context.startActivity(intent)
        }

        fun startVideoPlayerActivity(context: Context, path: String?) {
            try {
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(path), "video/mp4")
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun sharePostViaIntent(context: Context, textToShare: String?, imgURL: String) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, textToShare)
            if (imgURL != "") {
                val imageUri = Uri.parse(imgURL)
                intent.putExtra(Intent.EXTRA_STREAM, imageUri)
            }
            intent.setType("text/plain")
            context.startActivity(Intent.createChooser(intent, "Share via..."))
        }

        fun navigateUserToStore(context: Context) {
            val appPackageName =
                context.packageName // getPackageName() from Context or Activity object
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        /**
         * ***************************************************************************
         * ***************************** IMAGE FILE UTILS ****************************
         * ***************************************************************************
         */
        fun getRealPathFromURI(contentUri: Uri, activity: Activity): String? {
            return try {
                val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
                val cursor: Cursor = activity.managedQuery(
                    contentUri, proj, null, null, null
                )
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(column_index)
            } catch (e: Exception) {
                contentUri.path
            }
        }

        fun createBitmapFromLayout(tv: View): Bitmap? {
            val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            tv.measure(spec, spec)
            tv.layout(0, 0, tv.measuredWidth, tv.measuredHeight)
            val b = Bitmap.createBitmap(
                tv.measuredWidth, tv.measuredHeight, Bitmap.Config.ARGB_8888
            )
            val c = Canvas(b)
            c.translate((-tv.scrollX).toFloat(), (-tv.scrollY).toFloat())
            tv.draw(c)
            return b
        }

        fun convertBitmapToString(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun getOutputDirectory(context: Context): File {
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(
                    it, context.resources.getString(R.string.app_name)
                ).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
        }

        fun compressImage(imagepath: String): Bitmap {
            var scaledBitmap: Bitmap? = null
            val options: BitmapFactory.Options = BitmapFactory.Options()

            // by setting this field as true, the actual bitmap pixels are not
            // loaded in the memory. Just the bounds are loaded. If
            // you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true
            var bmp: Bitmap = BitmapFactory.decodeFile(imagepath, options)
            var actualHeight: Int = options.outHeight
            var actualWidth: Int = options.outWidth

            // max Height and width values of the compressed image is taken as
            // 816x612
            val maxHeight = 816.0f
            val maxWidth = 612.0f
            var imgRatio = (actualWidth / actualHeight).toFloat()
            val maxRatio = maxWidth / maxHeight

            // width and height values are set maintaining the aspect ratio of the
            // image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                } else {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }

            // setting inSampleSize value allows to load a scaled down version of
            // the original image
            options.inSampleSize = calculateInSampleSize(
                options, actualWidth, actualHeight
            )

            // inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false

            // this options allow android to claim the bitmap memory if it runs low
            // on memory
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)
            try {
                // load the bitmap from its path
                bmp = BitmapFactory.decodeFile(imagepath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            try {
                scaledBitmap = Bitmap.createBitmap(
                    actualWidth, actualHeight, Bitmap.Config.ARGB_8888
                )
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            val ratioX = actualWidth / options.outWidth as Float
            val ratioY = actualHeight / options.outHeight as Float
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
            val canvas = Canvas(scaledBitmap!!)
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(
                bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, Paint(
                    Paint.FILTER_BITMAP_FLAG
                )
            )

            // check the rotation of the image and display it properly
            val exif: ExifInterface
            try {
                exif = ExifInterface(imagepath)
                val orientation: Int = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0
                )
                LogUtils.Print("EXIF", "Exif: $orientation")
                val matrix = Matrix()
                if (orientation == 6) {
                    matrix.postRotate(90f)
                    LogUtils.Print("EXIF", "Exif: $orientation")
                } else if (orientation == 3) {
                    matrix.postRotate(180f)
                    LogUtils.Print("EXIF", "Exif: $orientation")
                } else if (orientation == 8) {
                    matrix.postRotate(270f)
                    LogUtils.Print("EXIF", "Exif: $orientation")
                }
                scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap!!,
                    0,
                    0,
                    scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(),
                    matrix,
                    true
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(imagepath)

                // write the compressed bitmap at the destination specified by
                // filename.
                scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return scaledBitmap!!
        }

        fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
        ): Int {
            val height: Int = options.outHeight
            val width: Int = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(
                    height.toFloat() / reqHeight.toFloat()
                )
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
            return inSampleSize
        }

        fun rotateImageAfterPick(sourcepath: String): Bitmap {
            var rotate = 0
            val mbitmap: Bitmap = compressImage(sourcepath)
            try {
                val imageFile = File(sourcepath)
                val exif = ExifInterface(imageFile.absolutePath)
                val orientation: Int = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val matrix = Matrix()
            matrix.postRotate(rotate.toFloat())
            return Bitmap.createBitmap(
                mbitmap, 0, 0, mbitmap.getWidth(), mbitmap.getHeight(), matrix, true
            )
        }

        fun rotate(ctx: Context, degree: Int, drawable: Drawable): Drawable {
            val iconBitmap: Bitmap = (drawable as BitmapDrawable).getBitmap()
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            val targetBitmap: Bitmap = Bitmap.createBitmap(
                iconBitmap, 0, 0, iconBitmap.getWidth(), iconBitmap.getHeight(), matrix, true
            )
            return BitmapDrawable(ctx.resources, targetBitmap)
        }

        // ---------------------------------------------------//
        // -------------- get Screen height ----------------//
        // -------------------------------------------------//
        fun getScreenWidth(context: Activity): Int {
            val display: Display = context.getWindowManager().getDefaultDisplay()
            val size = Point()
            display.getSize(size)
            return size.x
        }/*public static void makeNativeCall(Activity activity, String mobileNo) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNo));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activity.startActivity(intent);
    }*/

        /**
         * ***************************************************************************
         * **************************** PERMISSION UTILS ***************************
         * ***************************************************************************
         */
        fun checkPermission(context: Context?, permission: String?): Boolean {
            return ActivityCompat.checkSelfPermission(
                context!!, permission!!
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun showPermissionsDialog(
            activity: Activity?, permissions: Array<String?>?, REQUEST_CODE: Int
        ) {
            ActivityCompat.requestPermissions(activity!!, permissions!!, REQUEST_CODE)
        }

        fun shouldShowRequestPermissionRationale(
            activity: Activity?, permission: String?
        ): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission!!)
        }

        /**
         * ***************************************************************************
         * ************************* OTHER UTILS **************************
         * ***************************************************************************
         */
        fun fromHtml(html: String?): Spanned {
            val result: Spanned
            result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
            return result
        }

        fun locateView(v: View?): Rect? {
            val loc_int = IntArray(2)
            if (v == null) return null
            try {
                v.getLocationOnScreen(loc_int)
            } catch (npe: NullPointerException) {
                npe.printStackTrace()
                LogUtils.Print(TAG, "Happens when the view doesn't exist on screen anymore.")
                return null
            }
            val location = Rect()
            location.left = loc_int[0]
            location.top = loc_int[1]
            location.right = location.left + v.width
            location.bottom = location.top + v.height
            return location
        }

        fun getToolBarHeight(context: Context): Int {
            val attrs = intArrayOf(R.attr.actionBarSize)
            val ta: TypedArray = context.obtainStyledAttributes(attrs)
            val toolBarHeight: Int = ta.getDimensionPixelSize(0, -1)
            ta.recycle()
            return toolBarHeight
        }

        /**
         * return device width
         *
         * @param context
         */
        fun getDeviceWidth(context: Context): Int {
            return try {
                val displayMetrics = DisplayMetrics()
                (context as Activity).getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics)
                val height: Int = displayMetrics.heightPixels
                displayMetrics.widthPixels
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }

        fun getstringfromArray(array: Array<String>): String {
            val strBuilder = StringBuilder()
            for (i in array.indices) {
                strBuilder.append(array[i] + ",")
            }
            return strBuilder.toString()
        }

        fun getTimeFromMinutes(strMinutes: String): String {
            return try {
                val t = strMinutes.toInt()
                val hours = t / 60 //since both are ints, you get an int
                val minutes = t % 60
                System.out.printf("%d:%02d", hours, minutes)
                "$hours:$minutes"
            } catch (e: Exception) {
                "-"
            }
        }

        fun isFileLessThan2MB(strFile: String?): Boolean {
            val file = File(strFile)
            // Get length of file in bytes
            val fileSizeInBytes = file.length()
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            val fileSizeInKB = fileSizeInBytes / 1024
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            val fileSizeInMB = fileSizeInKB / 1024
            LogUtils.Print(TAG, "fileSizeInMB-> $fileSizeInMB")
            return fileSizeInMB < 2
        }

        //phone number validation
        fun isValidPhoneNumber(phone: String): Boolean {
            val check: Boolean
            val p: Pattern
            val m: Matcher
            // String PHONE_STRING = "^[+]?[0-9]{10,13}$";
            val PHONE_STRING = "^\\+(?:[0-9]●?){6,14}[0-9]$"
            p = Pattern.compile(PHONE_STRING)
            m = p.matcher(phone)
            check = m.matches()
            return phone.length == 8
            //return check;
        }

        fun setViewVisibility(view: View?, visibility: Int) {
            if (view != null) {
                view.visibility = visibility
            }
        }

        // -------------------------------------------------//
        // --------set color to swipe refresh layout-------//
        // -------------------------------------------------//
        fun setColorToSwipeRefreshLayout(srl: SwipeRefreshLayout) {
            try {
                srl.setColorSchemeResources(R.color.black)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * get ratting from string to float
         *
         * @param ratting avg ratting
         * @return float value of ratting
         */
        fun getRatting(ratting: String?): Float {
            var rate = 0f
            try {
                rate = java.lang.Float.valueOf(ratting)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return rate
        }

        fun getCurrentDate(format: String?): String {
            val date = Calendar.getInstance().time
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            return sdf.format(date)
        }

        fun downloadFile(activity: Context, url: String) {
            val name = url.substring(url.lastIndexOf("/") + 1)
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            //allow type of network to download file(s) by default both are allowed
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(name)
            request.setDescription("The file is downloading...")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + name)

            //get download service, and enqueue file
            val manager: DownloadManager =
                activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            if (manager != null) {
                manager.enqueue(request)
            }
        }

        fun changeStatusBarColor(context: Activity, color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = context.getWindow()
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color
            }
        }

        /* public static String getWeekDayName(CalendarDay date) {
        String name = "";
        switch (date.getDate().getDayOfWeek()) {
            case SUNDAY:
                name = "Sun";
                break;
            case MONDAY:
                name = "Mon";
                break;
            case TUESDAY:
                name = "Tue";
                break;
            case WEDNESDAY:
                name = "Wed";
                break;
            case THURSDAY:
                name = "Thu";
                break;
            case FRIDAY:
                name = "Fri";
                break;
            case SATURDAY:
                name = "Sat";
                break;
        }
        return name;
    }*/
        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }

        // Method for converting DP/DIP value to pixels
//        fun getPixelsFromDPs(activity: Context, dps: Int): Int {
//            val r = activity.resources
//            return TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics
//            )
//        }

        fun makeCall(context: Context, phoneNumber: String?) {
            if (phoneNumber != null && phoneNumber != "") {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                if (ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                context.startActivity(intent)
            }
        }

        /**
         * ***************************************************************************
         * ***************************** APP INFO UTILS ******************************
         * ***************************************************************************
         */
        fun GetDeviceID(ctx: Context): String {
            return Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun playAssetSound(activity: Activity, fileName: String?) {
            try {
                val mediaPlayer = MediaPlayer()
                val descriptor: AssetFileDescriptor = activity.getAssets().openFd(fileName!!)
                mediaPlayer.setDataSource(
                    descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(),
                    descriptor.getLength()
                )
                descriptor.close()
                mediaPlayer.prepare()
                mediaPlayer.setVolume(1f, 1f)
                mediaPlayer.setLooping(false)
                mediaPlayer.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Returns the consumer friendly device name
         */
        val deviceName: String
            get() {
                val manufacturer: String = Build.MANUFACTURER
                val model: String = Build.MODEL
                return if (model.startsWith(manufacturer)) {
                    capitalize(model)
                } else capitalize(manufacturer) + " " + model
            }

        public fun capitalize(str: String): String {
            if (TextUtils.isEmpty(str)) {
                return str
            }
            val arr = str.toCharArray()
            var capitalizeNext = true
            val phrase = StringBuilder()
            for (c in arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase.append(Character.toUpperCase(c))
                    capitalizeNext = false
                    continue
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true
                }
                phrase.append(c)
            }
            return phrase.toString()
        }

        fun addMonthInDate(timestamp: Long): Long {
            val c = Calendar.getInstance()
            c.timeInMillis = timestamp
            c.add(Calendar.MONTH, 1)
            return c.timeInMillis
        }

        fun addYearInDate(timestamp: Long): Long {
            val c = Calendar.getInstance()
            c.timeInMillis = timestamp
            c.add(Calendar.YEAR, 1)
            return c.timeInMillis
        }

        fun logEvent(context: Context, eventTitle: String, eventValues: HashMap<String, Any>) {
            AppsFlyerLib.getInstance()
                .logEvent(context, eventTitle, eventValues, object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        LogUtils.Print("AppsFlyerLib", "Event sent successfully")
                    }

                    override fun onError(errorCode: Int, errorDesc: String) {
                        LogUtils.Print(
                            "AppsFlyerLib",
                            "Event failed to be sent:\n" + "Error code: " + errorCode + "\n" + "Error description: " + errorDesc
                        )
                    }
                })
        }

        fun getAdSize(activity: Activity): AdSize {
            val outMetrics = activity.resources.displayMetrics
            val widthPixels = outMetrics.widthPixels.toFloat()
            val density = outMetrics.density
            val adWidth = (widthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        }

        fun getImageUri(context: Context, img: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver, img, context.getString(R.string.title), null
            )
            return Uri.parse(path)
        }

        fun generatePdf(context: Context, text: String) {
            lateinit var file: File
            val doc = Document()
            try {
                val path =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path + Constants.AiChat
                val dir = File(path)
                if (!dir.exists()) dir.mkdirs()
                file = File(dir, UUID.randomUUID().toString() + Constants.extensionPDF)
                val fOut = FileOutputStream(file)
                PdfWriter.getInstance(doc, fOut)
                //open the document
                doc.open()
                val p1 = Paragraph(text)
                val paraFont = Font(Font.FontFamily.COURIER)
                p1.alignment = Paragraph.ALIGN_CENTER
                p1.font = paraFont
                //add paragraph to document
                doc.add(p1)
            } catch (de: DocumentException) {
                Log.e("PDFCreator", "DocumentException:$de")
            } catch (e: IOException) {
                Log.e("PDFCreator", "ioException:$e")
            } finally {
                doc.close()
            }
            sharePDF(context, file)
        }

        fun sharePDF(context: Context, file: File) {
            val uri =
                FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(
                Intent.createChooser(
                    intent, context.getString(R.string.share_pdf)
                )
            )
        }

        fun termsPrivacy(context: Context, webViewFlag: Int) {
            val intent = Intent(context, PrivacyPolicyActivity::class.java)
            intent.putExtra(Constants.web_view, webViewFlag)
            context.startActivity(intent)
        }

        fun shareApp(context: Context) {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareMessage = preferences?.getRemoteConfig()?.androidShareText
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                context.startActivity(
                    Intent.createChooser(
                        shareIntent, context.getString(R.string.choose_any_one)
                    )
                )
            } catch (e: Exception) {
                LogUtils.Print("TAGS", e.message.toString())
            }
        }

        fun rateUs(context: Context) {
            try {
                val marketUri: Uri = Uri.parse("market://details?id=" + context.packageName)
                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                context.startActivity(marketIntent)
            } catch (e: ActivityNotFoundException) {
                val marketUri: Uri =
                    Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                context.startActivity(marketIntent)
            }
        }

        fun base64Decoded(encoded: String): String {
            val dataDec = Base64.decode(encoded, Base64.DEFAULT)
            var decodedString = ""
            try {
                decodedString = String(dataDec, charset("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } finally {
                return decodedString
            }
        }
        fun premiumScreenIntent(context: Context){
            val intent = Intent(context, PremiumActivity::class.java)
            context.startActivity(intent)
        }
        fun firebaseDatabase(context: Context){
            var android_id  = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID)
            FirebaseDatabase.getInstance().reference
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val responseClass = snapshot.getValue(all_service::class.java)!!
                        val usageCount = snapshot.child("Users").child(android_id).child("all_service").child("usageCount").getValue(Long::class.java)
                        val generateImageService = snapshot.child("Users").child(android_id).child("generate_image_service").child("throttle").getValue(Long::class.java)
                        val firstTimeUsedTimestamp = snapshot.child("Users").child(android_id).child("generate_image_service").child("firstTimeUsedTimestamp").getValue(Long::class.java)
                        preferences.putInt(Constants.allService, usageCount!!.toInt())
                        preferences.putInt(Constants.generateImageService, generateImageService!!.toInt())
                        preferences.putLong(Constants.firstTimeUsedTimestamp, firstTimeUsedTimestamp!!)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Utils.makeToast(
                            context, getString(R.string.server_error)
                        )
                    }
                })
        }
        fun setFirebaseData(context: Context, usageCount: Int,isPro:Boolean,isImageProGenerate:Boolean){
            var database: DatabaseReference = Firebase.database.reference
            val allService = all_service()
            if (preferences.isProVersion())
                allService.usageCount = usageCount
            else
                allService.usageCount = usageCount + 1
            val generateImageService = GenerateImageService()
            if (isImageProGenerate) {
                if (preferences.getInt("cont") == 1) {
                    generateImageService.throttle =
                        preferences.getInt(Constants.generateImageService) + 1
                    generateImageService.firstTimeUsedTimestamp =
                        preferences.getLong(Constants.firstTimeUsedTimestamp).toDouble()
                } else if (preferences.getInt("cont") == 2) {
                    val timestamp = Date().time
                    generateImageService.throttle =
                        preferences.getInt(Constants.generateImageService) + 1
                    generateImageService.firstTimeUsedTimestamp = timestamp.toDouble()
                }
            }
            if (isPro){
                val timestamp = Date().time
                generateImageService.throttle = 0
                generateImageService.firstTimeUsedTimestamp = timestamp.toDouble()
            }
            val map = HashMap<String, Any>()
            map["all_service"] = allService
            map["generate_image_service"] = generateImageService
            database
                .child("Users")
                .child(Secure.getString(
                    context.getContentResolver(),
                    Secure.ANDROID_ID))
                .setValue(map)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        LogUtils.Print("TAG", "isSuccessful - " + task.toString())
                        firebaseDatabase(context)
                    } else {
                        LogUtils.Print("TAG", "Failure - " + task.exception)
                    }
                }
        }
    }
}