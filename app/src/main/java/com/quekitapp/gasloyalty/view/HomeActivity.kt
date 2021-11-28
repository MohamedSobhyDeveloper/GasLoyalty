package com.quekitapp.gasloyalty.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.retrofitconfig.HandelCalls
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.quekitapp.gasloyalty.utlitites.HelpMe
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.ScanModel
import com.quekitapp.gasloyalty.models.VerifyPlate
import com.quekitapp.gasloyalty.utlitites.SetupLanguage
import com.tbruyelle.rxpermissions2.RxPermissions
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File


class HomeActivity : BaseActivity(),HandleRetrofitResp {
    var dialog: BottomSheetDialog? = null
    private val CAMERA_REQUEST = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        setupBottomSheet()
        click()
    }

    @SuppressLint("CheckResult")
    private fun checkPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.setLogging(true)

        rxPermissions
                .request(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                        Log.e("m", "permission")
                    } else {
                        Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
                        finish()
                        // Oups permission denied
                    }
                }
    }

    private fun setupBottomSheet() {
        @SuppressLint("InflateParams") val modalbottomsheet: View = layoutInflater.inflate(
                R.layout.language_layout,
                null
        )

        dialog = BottomSheetDialog(this)
        dialog!!.setContentView(modalbottomsheet)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setCancelable(true)

       val english = modalbottomsheet.findViewById<TextView>(R.id.english_txv)
        val arabic = modalbottomsheet.findViewById<TextView>(R.id.arabic_txv)
        val cancel = modalbottomsheet.findViewById<TextView>(R.id.cancel_dialog)
        cancel.setOnClickListener {dialog!!.hide() }

        english.setOnClickListener {
            dialog?.hide()
            if (PrefsUtil.with(this).get("language", "ar").equals("ar")){

                PrefsUtil.with(this).add("language", "en").apply()
                SetupLanguage.checkLanguage("en", this)
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        arabic.setOnClickListener {
            dialog?.hide()
            if (PrefsUtil.with(this).get("language", "ar").equals("en")){
                PrefsUtil.with(this).add("language", "ar").apply()
                SetupLanguage.checkLanguage("ar", this)
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun click() {
        readQrcodeBtn.setOnClickListener {
            startActivity(Intent(this, ScanQrActivity::class.java))

        }

        plateNumberBtn.setOnClickListener {
//            startActivity(Intent(this, PlateNumberActivity::class.java))
            EasyImage.openCamera(this@HomeActivity, 0)
        }

        languageBtn.setOnClickListener {

            dialog?.show()

        }

        logout.setOnClickListener {
            val alert = Builder(this)
            alert.setMessage(getString(R.string.want_logout_from_app))
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        PrefsUtil.with(this).add("id", "-1").apply()
                        val intent =
                                Intent(this, SplashActivity::class.java)
                        startActivity(intent)
                        finish()

                    }.setNegativeButton(getString(R.string.no), null)

            val alert1: AlertDialog = alert.create()
            alert1.show()
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

                val compressedImage = Compressor(this@HomeActivity).compressToFile(file)
                val compressfile_size = (compressedImage.length() / 1024).toString().toInt().toDouble()
                val compressfileSizeMB = compressfile_size / 1024
                Log.e("size_after", compressfileSizeMB.toString()+" mb ")

                val imageStream = contentResolver.openInputStream(Uri.fromFile(compressedImage))
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val encodedImage: String? = encodeImage(selectedImage)
                val verifybody = VerifyPlate(encodedImage!!)

                Log.e("image", encodedImage)
                Log.e("image", verifybody.snapshot!!)
                Log.e("json", verifybody.toString()!!)

                HandelCalls.getInstance(this@HomeActivity)?.callMultiPart(DataEnum.plateno.name, verifybody, true, this@HomeActivity)

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

    override fun onResponseSuccess(flag: String?, o: Any?) {
        val plateNumberModel: ScanModel = o as ScanModel

        if (plateNumberModel.pk.equals("-1")){
            HelpMe.getInstance(this)?.verifyPlateDialog(plateNumberModel,false,object :
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

                }

            })

        }else{

            val intent = Intent(this@HomeActivity, UpdatePlateActivity::class.java)
            intent.putExtra("balance", plateNumberModel.balance)
            intent.putExtra("mobile", plateNumberModel.mobile)
            intent.putExtra("name", plateNumberModel.name)
            intent.putExtra("pk", plateNumberModel.pk)
            intent.putExtra("plate_no", plateNumberModel.plate_no)
            intent.putExtra("ssn", plateNumberModel.ssn)
            intent.putExtra("tag_id", plateNumberModel.tag_id)
            intent.putExtra("valid", plateNumberModel.valid)
            intent.putExtra("maintenance_date", plateNumberModel.maintenance_date)
            intent.putExtra("EventID", plateNumberModel.EventID)
            intent.putExtra("fromHome", "true")
            startActivity(intent)
//            HelpMe.getInstance(this)?.infoDialog(plateNumberModel, true, true, object : HelpMe.ViewListenerInterface {
//                override fun clickView() {
//                    val intent = Intent(this@HomeActivity, ChargeActivity::class.java)
//                    intent.putExtra("mobile", plateNumberModel.mobile)
//                    startActivity(intent)
//                }
//
//                override fun verifyclickView() {
////                    EasyImage.openCamera(this@HomeActivity, 0)
//
//                }
//
//            })

        }

    }

    override fun onResponseFailure(flag: String?, o: String?) {
    }

    override fun onNoContent(flag: String?, code: Int) {
    }

    override fun onBadRequest(flag: String?, o: Any?) {
    }
}