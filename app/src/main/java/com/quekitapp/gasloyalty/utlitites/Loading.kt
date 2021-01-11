package com.quekitapp.gasloyalty.utlitites

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.quekitapp.gasloyalty.R
import com.wang.avi.AVLoadingIndicatorView

/**
 * Created by ksi on 27-Mar-18.
 */
class Loading(context: Context?) : AppCompatDialog(context) {
    private fun setup() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER)
        setContentView(R.layout.layout_loading)
        setCancelable(false)
        val ivLoading = findViewById<AVLoadingIndicatorView>(R.id.avi)
    }

    init {
        setup()
    }
}