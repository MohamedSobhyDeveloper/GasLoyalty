package com.quekitapp.gasloyalty.view

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil
import com.quekitapp.gasloyalty.R
import java.util.*

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        Log.e("CurrentScreen", this.javaClass.simpleName)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setuplanguages()

    }

    private fun setuplanguages() {
        val language: String = PrefsUtil.with(this).get("language", "")!!
        Log.e("mmmm", language)
        val locale: Locale
        locale = if (language == "" || language == "ar") {
            Locale("ar")
        } else {
            Locale(language)
        }
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
    }
}