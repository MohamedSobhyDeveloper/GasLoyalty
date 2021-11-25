package com.quekitapp.gasloyalty.view

import android.os.Bundle
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.retrofitconfig.HandelCalls
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.quekitapp.gasloyalty.utlitites.HelpMe
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.ChargeModel
import com.quekitapp.gasloyalty.models.VerifyBody
import com.quekitapp.gasloyalty.models.VerifyModel
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_charge_layout.*
import java.util.*

class ChargeActivity : BaseActivity(), HandleRetrofitResp {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charge_layout)

        click()

    }

    private fun click() {
        backBtn.setOnClickListener {
            finish()
        }

        submitbtn.setOnClickListener {
            if (meters.text.isEmpty() && amount.text.isEmpty()) {
                meters.error = getString(R.string.enter_metrs)
                amount.error = getString(R.string.enter_amount_please)
            } else if (meters.text.isEmpty()) {
                meters.error = getString(R.string.enter_metrs)
            } else if (amount.text.isEmpty()) {
                amount.error = getString(R.string.enter_amount_please)
            } else {
                val meMap = HashMap<String, String?>()
                meMap["mobile_no"] = intent.getStringExtra("mobile")
                meMap["payment_amount"] = amount.text.toString()
                meMap["quantity"] = meters.text.toString()
                meMap["EventID"] = intent.getStringExtra("eventId")

                val chargebody = VerifyBody(
                    intent.getStringExtra("mobile")!!,
                    amount.text.toString(),
                    meters.text.toString(),
                    intent.getStringExtra("eventId")!!,
                    ""
                )

                HandelCalls.getInstance(this)
                    ?.call(DataEnum.charge.name, meMap, chargebody, null, true, this)

            }


        }
    }

    override fun onResponseSuccess(flag: String?, o: Any?) {
        if (flag == DataEnum.charge.name) {
            val chargeModel: ChargeModel = o as ChargeModel
            if (!chargeModel?.message?.isEmpty()) {
                TastyToast.makeText(
                    this,
                    getString(R.string.otp_send),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.SUCCESS
                )
                HelpMe.getInstance(this)
                    ?.verifycodeDialog(object : HelpMe.ViewListenerVerifyInterface {
                        override fun clickView(code: String) {
                            val meMap = HashMap<String, String?>()
                            meMap["mobile_no"] = intent.getStringExtra("mobile")
                            meMap["recovery_code"] = code
                            meMap["payment_amount"] = amount.text.toString()
                            meMap["quantity"] = meters.text.toString()
                            val verifybody = VerifyBody(
                                intent.getStringExtra("mobile")!!,
                                amount.text.toString(),
                                meters.text.toString(),
                                intent.getStringExtra("eventId")!!,
                                code
                            )
                            HandelCalls.getInstance(this@ChargeActivity)?.call(
                                DataEnum.verify.name,
                                meMap,
                                verifybody,
                                null,
                                true,
                                this@ChargeActivity
                            )

                        }

                    })
            }
        } else {
            val verifyModel: VerifyModel = o as VerifyModel

            if (verifyModel.status.equals("-1")) {
                TastyToast.makeText(
                    this,
                    getString(R.string.error_verify_code),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR
                )

            } else {
                TastyToast.makeText(
                    this,
                    getString(R.string.charged_successfully),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.SUCCESS
                )
//                PrefsUtil.with(this).add("verified",true).apply()
                finish()

            }

        }

    }

    override fun onResponseFailure(flag: String?, o: String?) {

    }

    override fun onNoContent(flag: String?, code: Int) {

    }

    override fun onBadRequest(flag: String?, o: Any?) {

    }
}