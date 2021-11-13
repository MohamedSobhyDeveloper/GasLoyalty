package com.quekitapp.gasloyalty.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.zxing.Result
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.retrofitconfig.HandelCalls
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.interactive.ksi.propertyturkeybooking.utlitites.HelpMe
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.ScanModel
import com.quekitapp.gasloyalty.models.UpdatePlateBody
import com.quekitapp.gasloyalty.models.VerifyPlate
import com.sdsmdg.tastytoast.TastyToast
import id.zelory.compressor.Compressor
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
    lateinit var scanModel: ScanModel
    lateinit var plateNumberModel: ScanModel
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

         scanModel = o as ScanModel

        if (scanModel.pk.equals("-1")){
            TastyToast.makeText(this,getString(R.string.no_data_found_for_this_tank), TastyToast.LENGTH_SHORT, TastyToast.ERROR)

        }else{
          showInfoDialog(scanModel,false)
        }
        }else if (flag==DataEnum.updatePlate.name){

        }else{
            plateNumberModel = o as ScanModel

            if (scanModel.plate_no.equals(plateNumberModel.plate_no)) {
                TastyToast.makeText(this,getString(R.string.verifed_plate_num), TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                showInfoDialog(scanModel,true)

            } else {
                HelpMe.getInstance(this)?.verifyPlateDialog(plateNumberModel,true,object :
                    HelpMe.ViewListenerInterface {
                    override fun clickView() {
                        HelpMe.getInstance(this@ScanQrActivity)?.updatePlate(object :
                            HelpMe.ViewListenerUpdatePlateInterface {
                            override fun clickView(
                                chr1: String,
                                chr2: String,
                                chr3: String,
                                num1: String,
                                num2: String,
                                num3: String,
                                num4: String
                            ) {
                                val item=UpdatePlateBody("",chr1,chr2,chr3,num1,num2,num3,num4)
                                updatePlate(item)

                            }

                        })
                    }

                    override fun verifyclickView() {

                    }

                })
            }
        }


    }

    private fun updatePlate(item: UpdatePlateBody) {
        HandelCalls.getInstance(this)?.call(DataEnum.updatePlate.name, null, null,item,true, this)

    }

    private fun showInfoDialog(scanModel: ScanModel,verifyPlateNum:Boolean) {
        HelpMe.getInstance(this)?.infoDialog(scanModel,verifyPlateNum,false,object : HelpMe.ViewListenerInterface{
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
            HandelCalls.getInstance(this)?.call(DataEnum.scan.name, meMap, null,null,true, this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {
                val uri = Uri.fromFile(imageFile)

                val filePath: String = uri.getPath()!!

                val file = File(filePath)
                val file_size = (file.length() / 1024).toString().toInt().toDouble()
                val fileSizeMB = file_size / 1024
                Log.e("size_before", fileSizeMB.toString()+" mb ")

                val compressedImage = Compressor(this@ScanQrActivity).compressToFile(file)
                val compressfile_size = (compressedImage.length() / 1024).toString().toInt().toDouble()
                val compressfileSizeMB = compressfile_size / 1024
                Log.e("size_after", compressfileSizeMB.toString()+" mb ")

                val imageStream = contentResolver.openInputStream(Uri.fromFile(compressedImage))
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val encodedImage: String? = encodeImage(selectedImage)
                val verifybody = VerifyPlate(encodedImage!!)

                Log.e("image", verifybody.snapshot!!)
                Log.e("json", verifybody.toString()!!)



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