package com.quekitapp.gasloyalty.view

import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.retrofitconfig.HandelCalls
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.models.LogiModel
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : BaseActivity() ,HandleRetrofitResp{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        click()
    }

    private fun click() {
        loginbtn.setOnClickListener {

            if (username.text.isEmpty()||password.text.isEmpty()){
                TastyToast.makeText(this, getString(R.string.enter_name_password), TastyToast.LENGTH_SHORT, TastyToast.ERROR)

            }else{
                val meMap = HashMap<String, String?>()
                meMap["username"] = username.text.toString()
                meMap["password"] = password.text.toString()

                HandelCalls.getInstance(this)?.call(DataEnum.login.name, meMap, true, this)

            }


        }

        backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onResponseSuccess(flag: String?, o: Any?) {
        if (flag == DataEnum.login.name) {
            LoginAction(o)
        }

    }

    private fun LoginAction(o: Any?) {
        val modelLogin: LogiModel = o as LogiModel
        if (!modelLogin.pk.equals("-1")){
            PrefsUtil.with(this).add("id",modelLogin.pk).apply()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }else{
            TastyToast.makeText(this,getString(R.string.correct_username_password), TastyToast.LENGTH_SHORT, TastyToast.ERROR)

        }
    }


    override fun onResponseFailure(flag: String?, o: String?) {
        val gson = GsonBuilder().create()

    }

    override fun onNoContent(flag: String?, code: Int) {
    }

    override fun onBadRequest(flag: String?, o: Any?) {
    }
}