package com.quekitapp.gasloyalty

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.AlertDialog.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil
import com.quekitapp.gasloyalty.utlitites.SetupLanguage
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : BaseActivity() {
    var dialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       setupBottomSheet()
        click()
    }

    private fun setupBottomSheet() {
        @SuppressLint("InflateParams") val modalbottomsheet: View = layoutInflater.inflate(R.layout.language_layout, null)

        dialog = BottomSheetDialog(this)
        dialog!!.setContentView(modalbottomsheet)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setCancelable(true)

       val english = modalbottomsheet.findViewById<TextView>(R.id.english_txv)
        val arabic = modalbottomsheet.findViewById<TextView>(R.id.arabic_txv)
        val cancel = modalbottomsheet.findViewById<TextView>(R.id.cancel_dialog)
        cancel.setOnClickListener {dialog!!.hide() }

        english.setOnClickListener {
            dialog?.hide()
            if (PrefsUtil.with(this).get("language", "ar").equals("ar")){

                PrefsUtil.with(this).add("language", "en").apply()
                SetupLanguage.checkLanguage("en", this)
                val intent =
                        Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        arabic.setOnClickListener {
            dialog?.hide()
            if (PrefsUtil.with(this).get("language", "ar").equals("en")){
                PrefsUtil.with(this).add("language", "ar").apply()
                SetupLanguage.checkLanguage("ar", this)
                val intent =
                        Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun click() {
        readQrcodeBtn.setOnClickListener {
            startActivity(Intent(this, ScanQrActivity::class.java))

        }

        plateNumberBtn.setOnClickListener {
            startActivity(Intent(this, PlateNumberActivity::class.java))

        }

        languageBtn.setOnClickListener {

            dialog?.show()


        }

        logout.setOnClickListener {
            val alert: Builder = Builder(this)
            alert.setMessage(getString(R.string.want_logout_from_app))
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        PrefsUtil.with(this).add("id", "-1").apply()
                        val intent =
                                Intent(this, SplashActivity::class.java)
                        startActivity(intent)
                        finish()

                    }.setNegativeButton(getString(R.string.no), null)

            val alert1: AlertDialog = alert.create()
            alert1.show()
        }


    }
}