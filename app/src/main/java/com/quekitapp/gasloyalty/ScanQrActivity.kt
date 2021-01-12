package com.quekitapp.gasloyalty

import android.os.Bundle
import com.google.zxing.Result
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.retrofitconfig.HandelCalls
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.interactive.ksi.propertyturkeybooking.utlitites.HelpMe
import com.quekitapp.gasloyalty.models.ScanModel
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_scan_qr.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

import java.util.HashMap

class ScanQrActivity : BaseActivity(), ZXingScannerView.ResultHandler,HandleRetrofitResp {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

      click()

    }

    private fun click() {
        scanbtn.setOnClickListener {
            scannerview.resumeCameraPreview(this)

        }
    }


    override fun onResume() {
        super.onResume()
        scannerview.setResultHandler(this@ScanQrActivity)
        scannerview.startCamera()

    }

    override fun onPause() {
        super.onPause()
        scannerview.stopCamera()
    }


    override fun onResponseSuccess(flag: String?, o: Any?) {

        val scanModel: ScanModel = o as ScanModel

        if (scanModel.pk.equals("-1")){
            TastyToast.makeText(this,getString(R.string.no_data_found_for_this_tank), TastyToast.LENGTH_SHORT, TastyToast.ERROR)

        }else{
            HelpMe.getInstance(this)?.infoDialog(scanModel,object : HelpMe.ViewListenerInterface{
                override fun clickView() {

                }

            })
        }


    }

    override fun onResponseFailure(flag: String?, o: String?) {

    }

    override fun onNoContent(flag: String?, code: Int) {

    }

    override fun onBadRequest(flag: String?, o: Any?) {

    }

    override fun handleResult(result: Result?) {
       val tank_id = result!!.text

        if (!tank_id.isEmpty()){
            val meMap = HashMap<String, String?>()
            meMap["tankid"] = tank_id
            HandelCalls.getInstance(this)?.call(DataEnum.scan.name, meMap, true, this)

        }
    }
}