package com.quekitapp.gasloyalty

import android.content.Intent
import android.os.Bundle
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil
import com.quekitapp.gasloyalty.utlitites.SetupLanguage
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        click()
    }

    private fun click() {
        readQrcodeBtn.setOnClickListener {
            startActivity(Intent(this, ScanQrActivity::class.java))

        }

        plateNumberBtn.setOnClickListener {
            startActivity(Intent(this, PlateNumberActivity::class.java))

        }

        languageBtn.setOnClickListener {
            PrefsUtil.with(this).add("language", "ar").apply()
//            RestRetrofit.inititobj()
//            HandelCalls.inititobjHandcalls()
            SetupLanguage.checkLanguage("ar", this)
            val intent =
                Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}