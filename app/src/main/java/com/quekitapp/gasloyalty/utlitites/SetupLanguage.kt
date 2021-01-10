package com.quekitapp.gasloyalty.utlitites

import android.content.Context
import android.content.res.Configuration
import java.util.*

object SetupLanguage {
    fun checkLanguage(Language: String?, context: Context) {
        val locale = Locale(Language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
}