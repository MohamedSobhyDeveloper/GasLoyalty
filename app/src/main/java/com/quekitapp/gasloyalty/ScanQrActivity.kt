package com.quekitapp.gasloyalty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScanQrActivity : BaseActivity(), ZBarScannerView.ResultHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
    }

    override fun handleResult(p0: Result?) {

    }
}