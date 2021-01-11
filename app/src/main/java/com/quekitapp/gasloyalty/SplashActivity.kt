package com.quekitapp.gasloyalty

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            if (PrefsUtil.with(this).get("id","-1").equals("-1")){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }


        }, 2000)
    }
}