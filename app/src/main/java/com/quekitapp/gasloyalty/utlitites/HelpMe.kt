package com.interactive.ksi.propertyturkeybooking.utlitites

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.ScanModel
import com.sdsmdg.tastytoast.TastyToast
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
//        if (t is ConnectException) {
//            TastyToast.makeText(context, t.message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
//        } else {
            TastyToast.makeText(context, context!!.getString(R.string.check_connections), TastyToast.LENGTH_LONG, TastyToast.ERROR)
            Log.e("errrr", t.message!!)
//        }
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


    @SuppressLint("SetTextI18n")
    fun infoDialog(scanmodel:ScanModel,fromHome:Boolean, viewListenerInterface:ViewListenerInterface) {
        val dialogView = Dialog(context!!)
        dialogView.setContentView(R.layout.customer_layout_info_dialog)
        dialogView.setCanceledOnTouchOutside(false)
        dialogView.setCancelable(false)
        dialogView.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val charge_btn = dialogView.findViewById<Button>(R.id.chargebtn)
        val verify_btn = dialogView.findViewById<Button>(R.id.verifyplatebtn)
        val mobile_tv = dialogView.findViewById<TextView>(R.id.mobile_tv)
        val name_tv = dialogView.findViewById<TextView>(R.id.name_tv)
        val plate_tv = dialogView.findViewById<TextView>(R.id.plate_tv)
        val tank_tv = dialogView.findViewById<TextView>(R.id.tank_tv)
        val blance_tv = dialogView.findViewById<TextView>(R.id.blance_tv)
        val maintenance_tv = dialogView.findViewById<TextView>(R.id.maintenance_tv)
        val maintenance_info = dialogView.findViewById<TextView>(R.id.maintenance_info)

        val closeBtn = dialogView.findViewById<TextView>(R.id.closeBtn)
        closeBtn.setOnClickListener { view: View? -> dialogView.dismiss() }
        mobile_tv.text=scanmodel.mobile
        name_tv.text=scanmodel.name
        plate_tv.text=scanmodel.plate_no
        tank_tv.text=scanmodel.tag_id
        blance_tv.text=scanmodel.balance
        maintenance_tv.text=scanmodel.maintenance_date

        if (scanmodel.valid.equals("-1")){
            maintenance_info.visibility=View.VISIBLE
            charge_btn.visibility=View.GONE
        }else{
            maintenance_info.visibility=View.GONE
            charge_btn.visibility=View.VISIBLE
        }

        if (fromHome){
            verify_btn.visibility=View.GONE
        }

        charge_btn.setOnClickListener {
            dialogView.dismiss()
            viewListenerInterface.clickView()
        }

        verify_btn.setOnClickListener {
            viewListenerInterface.verifyclickView()
        }


        dialogView.show()
    }


    @SuppressLint("SetTextI18n")
    fun verifyPlateDialog(platenumber: ScanModel,notshow:Boolean) {
        val dialogView = Dialog(context!!)
        dialogView.setContentView(R.layout.verify_plate_dialog)
        dialogView.setCanceledOnTouchOutside(true)
        dialogView.setCancelable(true)
        dialogView.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val plate_tv = dialogView.findViewById<TextView>(R.id.plate_tv)
        val plate_info = dialogView.findViewById<TextView>(R.id.no_info)

        val closeBtn = dialogView.findViewById<TextView>(R.id.closeBtn)
        closeBtn.setOnClickListener { view: View? -> dialogView.dismiss() }

        if (!platenumber.plate_no.equals("-1")){
           plate_tv.text=platenumber.plate_no
            plate_info.visibility=View.VISIBLE
        }else{
           plate_tv.text= context!!.getString(R.string.cnt_verify_plate_num)
            plate_info.visibility=View.GONE

        }
        if (notshow){
            plate_info.visibility=View.GONE

        }

        dialogView.show()
    }


    @SuppressLint("SetTextI18n")
    fun verifycodeDialog(viewListenerInterface:ViewListenerVerifyInterface) {
        val dialogView = Dialog(context!!)
        dialogView.setContentView(R.layout.verify_code_dialog)
        dialogView.setCanceledOnTouchOutside(true)
        dialogView.setCancelable(true)
        dialogView.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val verifyCode = dialogView.findViewById<EditText>(R.id.verifyCode)
        val submit = dialogView.findViewById<Button>(R.id.submitbtn)

        val closeBtn = dialogView.findViewById<TextView>(R.id.closeBtn)
        closeBtn.setOnClickListener { view: View? -> dialogView.dismiss() }

        submit.setOnClickListener {
            if (verifyCode.text.isEmpty()){
                verifyCode.error=context!!.getString(R.string.enter_verification_code)
            }else{
                  viewListenerInterface.clickView(verifyCode.text.toString())
            }
        }






        dialogView.show()
    }





    interface ViewListenerInterface {
        fun clickView()
        fun verifyclickView()

    }

    interface ViewListenerVerifyInterface {
        fun clickView(code:String)
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