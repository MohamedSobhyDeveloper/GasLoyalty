package com.quekitapp.gasloyalty.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.retrofitconfig.HandelCalls
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.*
import com.sdsmdg.tastytoast.TastyToast
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_scan_qr.*
import kotlinx.android.synthetic.main.activity_update_plate.*
import kotlinx.android.synthetic.main.activity_update_plate.backBtn
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.ByteArrayOutputStream
import java.io.File

class UpdatePlateActivity : BaseActivity(), HandleRetrofitResp {
    private lateinit var plateNumberModel: ScanModel
    private lateinit var scanModel: ScanModel
   lateinit var dialog:BottomSheetUpdatedDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_plate)

        initialize()
        click()

    }

    private fun initialize() {
        scanModel = ScanModel(
            intent.getStringExtra("balance")!!,
            intent.getStringExtra("mobile")!!,
            intent.getStringExtra("name")!!,
            intent.getStringExtra("pk")!!,
            intent.getStringExtra("plate_no")!!,
            intent.getStringExtra("ssn")!!,
            intent.getStringExtra("tag_id")!!,
            intent.getStringExtra("valid")!!,
            intent.getStringExtra("maintenance_date")!!,
            intent.getStringExtra("EventID")
        )
        mobile_tv.text = scanModel.mobile
        name_tv.text = scanModel.name
        plate_tv.text = scanModel.plate_no
        tank_tv.text = scanModel.tag_id
        blance_tv.text = scanModel.balance
        maintenance_tv.text = scanModel.maintenance_date

        if (intent.getStringExtra("fromHome").equals("true")){
            success_info.visibility=View.VISIBLE
            success_info2.visibility=View.VISIBLE
            chargebtn.visibility = View.VISIBLE
            verifyplatebtn.visibility=View.GONE
        }
    }

    private fun click() {
        backBtn.setOnClickListener {
            finish()
        }

        verifyplatebtn.setOnClickListener {
            EasyImage.openCamera(this@UpdatePlateActivity, 0)

        }

        chargebtn.setOnClickListener {
            openTank()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onImagePicked(
                    imageFile: File,
                    source: EasyImage.ImageSource,
                    type: Int
                ) {
                    val uri = Uri.fromFile(imageFile)

                    val filePath: String = uri.getPath()!!

                    val file = File(filePath)
                    val file_size = (file.length() / 1024).toString().toInt().toDouble()
                    val fileSizeMB = file_size / 1024
                    Log.e("size_before", fileSizeMB.toString() + " mb ")

                    val compressedImage = Compressor(this@UpdatePlateActivity).compressToFile(file)
                    val compressfile_size =
                        (compressedImage.length() / 1024).toString().toInt().toDouble()
                    val compressfileSizeMB = compressfile_size / 1024
                    Log.e("size_after", "$compressfileSizeMB mb ")

                    val imageStream = contentResolver.openInputStream(Uri.fromFile(compressedImage))
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    val encodedImage: String? = encodeImage(selectedImage)
                    val verifybody = VerifyPlate(encodedImage!!)

                    Log.e("image", verifybody.snapshot)
                    Log.e("json", verifybody.toString())



                    HandelCalls.getInstance(this@UpdatePlateActivity)?.callMultiPart(
                        DataEnum.plateno.name,
                        verifybody,
                        true,
                        this@UpdatePlateActivity
                    )

                }
            })
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onResponseSuccess(flag: String?, o: Any?) {
        if (flag == DataEnum.updatePlate.name) {
            val data: UpdatePlateModel = o as UpdatePlateModel
            val plate=data.plate_no.trim()
            val oldplate=intent.getStringExtra("plate_no")?.trim() ?: ""
            if (data.plate_no.trim() == intent.getStringExtra("plate_no")?.trim() ?: "") {
                dialog.dismiss()
//                TastyToast.makeText(
//                    this,
//                    getString(R.string.verifed_plate_num),
//                    TastyToast.LENGTH_SHORT,
//                    TastyToast.SUCCESS
//                )
                success_info.visibility=View.VISIBLE
                success_info2.visibility=View.VISIBLE

                chargebtn.visibility = View.VISIBLE
                verifyplatebtn.visibility = View.GONE
            } else {
                TastyToast.makeText(
                    this,
                    getString(R.string.verify_plate),
                    TastyToast.LENGTH_LONG,
                    TastyToast.ERROR
                )

            }

        } else if (flag == DataEnum.openTank.name) {
            val data: OpenTankModel = o as OpenTankModel
            if (data.message == "1") {
                val intent = Intent(this@UpdatePlateActivity, ChargeActivity::class.java)
                intent.putExtra("mobile", scanModel.mobile)
                if (intent.getStringExtra("fromHome").equals("true")){
                    intent.putExtra("eventId", scanModel.EventID)

                }else{
                    intent.putExtra("eventId", plateNumberModel.EventID)

                }
                startActivity(intent)
                finish()
            } else {
                TastyToast.makeText(
                    this,
                    getString(R.string.check_data),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.DEFAULT
                )

            }

        } else {
            plateNumberModel = o as ScanModel

            if (scanModel.plate_no.trim() == plateNumberModel.plate_no.trim()) {
//                TastyToast.makeText(
//                    this,
//                    getString(R.string.verifed_plate_num),
//                    TastyToast.LENGTH_SHORT,
//                    TastyToast.SUCCESS
//                )
                success_info.visibility=View.VISIBLE
                success_info2.visibility=View.VISIBLE
                chargebtn.visibility = View.VISIBLE
                verifyplatebtn.visibility=View.GONE

            } else {

                dialog= BottomSheetUpdatedDialog(
                    plateNumberModel,
                    itemSelectedAction = {
                        updatePlate(it)
                    })

                dialog.show(supportFragmentManager,"dialog")
//                HelpMe.getInstance(this)?.verifyPlateDialog(plateNumberModel, true, object :
//                    HelpMe.ViewListenerUpdatePlateInterface {
//                    override fun clickView(
//                        chr1: String,
//                        chr2: String,
//                        chr3: String,
//                        num1: String,
//                        num2: String,
//                        num3: String,
//                        num4: String
//                    ) {
//                        val item =
//                            UpdatePlateBody(
//                                scanModel.EventID!!,
//                                chr1,
//                                chr2,
//                                chr3,
//                                num1,
//                                num2,
//                                num3,
//                                num4
//                            )
//                        updatePlate(item)
//                    }
//
//                })
            }
        }


    }

    override fun onResponseFailure(flag: String?, o: String?) {

    }

    override fun onNoContent(flag: String?, code: Int) {

    }

    override fun onBadRequest(flag: String?, o: Any?) {

    }

    private fun openTank() {
        HandelCalls.getInstance(this)?.call(DataEnum.openTank.name, null, null, null, true, this)

    }

    private fun updatePlate(item: UpdatePlateBody) {
        HandelCalls.getInstance(this)?.call(DataEnum.updatePlate.name, null, null, item, true, this)

    }

}