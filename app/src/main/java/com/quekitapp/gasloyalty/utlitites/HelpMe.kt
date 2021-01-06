package com.interactive.ksi.propertyturkeybooking.utlitites

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.quekitapp.gasloyalty.R
import com.sdsmdg.tastytoast.TastyToast
import java.net.ConnectException
import java.util.*

/**
 * Created by Mina Shaker on 27-Mar-18.
 */
class HelpMe {
    private var gson: Gson? = null
    val isTablet: Boolean
        get() = ((context!!.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)

    fun parseJsonToObject(response: String?, modelClass: Class<*>?): Any? {
        if (gson == null) {
            gson = GsonBuilder().serializeNulls().create()
        }
        return try {
            gson!!.fromJson(response, modelClass)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    /**
     * Serializes the specified object into its equivalent Json representation.
     *
     * @param object The object which Json will represent.
     * @return Json representation of src.
     */
    fun parseObjectToJson(`object`: Any?): String {
        if (gson == null) {
            gson = GsonBuilder().serializeNulls().create()
        }
        return gson!!.toJson(`object`)
    }

    fun isAppInstalled(packageName: String?): Boolean {
        return try {
            //boolean whatsappFound = AndroidHelper.isAppInstalled(context, "com.whatsapp");
            context!!.packageManager.getApplicationInfo(packageName!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun initLang(lang: String?) {

        //  String lang = SharedPrefUtil.getInstance(getApplicationContext()).read("settingLangName", "en");
        //  String lang = "ar";
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context!!.resources.updateConfiguration(config,
                context!!.resources.displayMetrics)
    }

    fun hideKeyBoard(act: Activity) {
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun spliteForLog(veryLongString: String) {
        val maxLogStringSize = 1000
        for (i in 0..veryLongString.length / maxLogStringSize) {
            val start = i * maxLogStringSize
            var end = (i + 1) * maxLogStringSize
            end = if (end > veryLongString.length) veryLongString.length else end
            Log.v("spletres", veryLongString.substring(start, end))
        }
    }

    //======================================================//
    fun hidekeyboard(act: Activity) {
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun scaleCenterCrop(source: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        val xScale = newWidth.toFloat() / sourceWidth
        val yScale = newHeight.toFloat() / sourceHeight
        val scale = Math.max(xScale, yScale)

        // Now get the size of the source bitmap when scaled
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        val dest = Bitmap.createBitmap(newWidth, newHeight, source.config)
        val canvas = Canvas(dest)
        canvas.drawBitmap(source, null, targetRect, null)
        return dest
    }

    fun retrofironFailure(t: Throwable) {
        if (t is ConnectException) {
            TastyToast.makeText(context, t.message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
        } else {
            TastyToast.makeText(context, context!!.getString(R.string.check_connections), TastyToast.LENGTH_LONG, TastyToast.ERROR)
            Log.e("errrr", t.message!!)
        }
    }

    fun retrofirOnNotTwoHundred(x: Int) {
        Log.e("codeis", x.toString() + "")
        if (x == 204) {
            // TastyToast.makeText(context, context.getString(R.string.no_content), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        } else if (x == 400) {
            //TastyToast.makeText(context, context.getString(R.string.no_data), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }

    /*================================================================================*/


    interface ViewListenerInterface {
        fun clickView(agentId: String?, agentName: String?)
    }

    companion object {
        // static Uri.Builder builder;
        private var context: Context? = null
        private var instance: HelpMe? = null

        //  public   Socket mSocket;
        @JvmStatic
        fun getInstance(context: Context?): HelpMe? {
            Companion.context = context
            if (instance == null) {
                instance = HelpMe()
            }
            return instance
        }
    }
}