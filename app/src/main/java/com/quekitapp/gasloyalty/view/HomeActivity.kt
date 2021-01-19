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
import com.interactive.ksi.propertyturkeybooking.utlitites.HelpMe
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.PlateNumberModel
import com.quekitapp.gasloyalty.utlitites.SetupLanguage
import com.tbruyelle.rxpermissions2.RxPermissions
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
                val intent =
                        Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        arabic.setOnClickListener {
            dialog?.hide()
            if (PrefsUtil.with(this).get("language", "ar").equals("en")){
                PrefsUtil.with(this).add("language", "ar").apply()
                SetupLanguage.checkLanguage("ar", this)
                val intent =
                        Intent(this, SplashActivity::class.java)
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
                val imageStream = contentResolver.openInputStream(uri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val encodedImage: String? = encodeImage(selectedImage)

                Log.e("image", encodedImage!!)

                HandelCalls.getInstance(this@HomeActivity)?.callMultiPart(DataEnum.plateno.name, encodedImage, true, this@HomeActivity)

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
        val plateNumberModel: PlateNumberModel = o as PlateNumberModel
          HelpMe.getInstance(this)?.verifyPlateDialog(plateNumberModel)

    }

    override fun onResponseFailure(flag: String?, o: String?) {
    }

    override fun onNoContent(flag: String?, code: Int) {
    }

    override fun onBadRequest(flag: String?, o: Any?) {
    }
}