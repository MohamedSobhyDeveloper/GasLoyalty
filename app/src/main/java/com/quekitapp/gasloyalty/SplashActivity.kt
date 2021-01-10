package com.quekitapp.gasloyalty

import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()

        }, 2000)
    }
}