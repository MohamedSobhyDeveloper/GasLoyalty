package com.interactive.ksi.propertyturkeybooking.utlitites

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatImageView
import com.quekitapp.gasloyalty.R
import com.race604.drawable.wave.WaveDrawable

/**
 * Created by ksi on 27-Mar-18.
 */
class LoadingDialog(context: Context?) : AppCompatDialog(context) {
    private fun setup() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER)
        setContentView(R.layout.layout_loading_indicator)
        setCancelable(false)

        val ivLoading = findViewById<AppCompatImageView>(R.id.ivLoading)
        val drawable = WaveDrawable(context, R.drawable.ic_launcher_background)
        ivLoading!!.setImageDrawable(drawable)
        drawable.isIndeterminate = true
        drawable.setWaveAmplitude(30)
        drawable.setWaveLength(70)
        drawable.setWaveSpeed(65)
    }

    init {
        setup()
    }
}