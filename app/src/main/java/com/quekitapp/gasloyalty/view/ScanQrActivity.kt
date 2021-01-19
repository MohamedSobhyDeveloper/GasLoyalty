package com.quekitapp.gasloyalty.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import com.google.zxing.Result
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.retrofitconfig.HandelCalls
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.interactive.ksi.propertyturkeybooking.utlitites.HelpMe
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.PlateNumberModel
import com.quekitapp.gasloyalty.models.ScanModel
import com.quekitapp.gasloyalty.models.VerifyBody
import com.quekitapp.gasloyalty.models.VerifyPlate
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_charge_layout.*
import kotlinx.android.synthetic.main.activity_scan_qr.*
import kotlinx.android.synthetic.main.activity_scan_qr.backBtn
import me.dm7.barcodescanner.zxing.ZXingScannerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File

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
        backBtn.setOnClickListener {
            finish()
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


        if (flag==DataEnum.scan.name){

        val scanModel: ScanModel = o as ScanModel

        if (scanModel.pk.equals("-1")){
            TastyToast.makeText(this,getString(R.string.no_data_found_for_this_tank), TastyToast.LENGTH_SHORT, TastyToast.ERROR)

        }else{
            HelpMe.getInstance(this)?.infoDialog(scanModel,object : HelpMe.ViewListenerInterface{
                override fun clickView() {
                    val intent = Intent(this@ScanQrActivity, ChargeActivity::class.java)
                    intent.putExtra("mobile", scanModel.mobile)
                    startActivity(intent)
                }

                override fun verifyclickView() {
                    EasyImage.openCamera(this@ScanQrActivity, 0)

                }

            })
        }
        }else{
            val plateNumberModel: PlateNumberModel = o as PlateNumberModel
            HelpMe.getInstance(this)?.verifyPlateDialog(plateNumberModel)
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
            HandelCalls.getInstance(this)?.call(DataEnum.scan.name, meMap, null,true, this)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {
                val uri= Uri.fromFile(imageFile)
                val imageStream = contentResolver.openInputStream(uri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val encodedImage: String? = encodeImage(selectedImage)
                val verifybody= VerifyPlate(encodedImage!!)



                HandelCalls.getInstance(this@ScanQrActivity)?.callMultiPart(DataEnum.plateno.name, verifybody, true, this@ScanQrActivity)

            }
        })
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    private fun prepareFilePart(fileUri: String?): MultipartBody.Part {
        val file = File(fileUri)
        // create RequestBody instance from file
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("snapshot", file.name, requestFile)
    }
}