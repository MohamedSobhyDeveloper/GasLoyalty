package com.interactive.ksi.propertyturkeybooking.retrofitconfig

import android.content.Context
import android.util.Log
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitResp
import com.interactive.ksi.propertyturkeybooking.interfaces.HandleRetrofitRespAdapter
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.interactive.ksi.propertyturkeybooking.utlitites.HelpMe
import com.quekitapp.gasloyalty.utlitites.Loading
import com.sdsmdg.tastytoast.TastyToast
import okhttp3.MultipartBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.IOException
import java.util.*

/**
 * Created by lenovo on 1/3/2018.
 */
class HandelCalls {
    private var onRespnse: HandleRetrofitResp? = null
    private var onRespnseAdapter: HandleRetrofitRespAdapter? = null

    /**
     * @param onRespnseSucess
     */
    fun setonRespnseSucess(onRespnseSucess: HandleRetrofitResp?) {
        onRespnse = onRespnseSucess
    }

    fun setonRespnseSucessApapter(onRespnseAdapter: HandleRetrofitRespAdapter?) {
        this.onRespnseAdapter = onRespnseAdapter
    }

    fun call(flag: String, meMap: HashMap<String, String?>?, ShowLoadingDialog: Boolean,onRespnseSucess: HandleRetrofitResp) {
        onRespnse = onRespnseSucess

        if (flag==DataEnum.login.name){
            val username = meMap!!["username"]
            val password = meMap!!["password"]
            callRetrofit(restRetrofit!!.getClientService().login(username,password), flag, ShowLoadingDialog)
        }else if (flag==DataEnum.scan.name){
            val tank_id = meMap!!["tankid"]
            callRetrofit(restRetrofit!!.getClientService().scan(tank_id), flag, ShowLoadingDialog)
        }

    }

    fun callMultiPart(flag: String, meMap: HashMap<String?, String?>, @Part postAudio: MultipartBody.Part?, ShowLoadingDialog: Boolean) {
//        if (flag == DataEnum.postAudio.name) {
//            callRetrofit(restRetrofit!!.getClientService().postAudio(postAudio, meMap["id"]), flag, ShowLoadingDialog)
//        }
    }

    /*
    public void callURL(final String flag, String url) {

        if (flag.equals(DataEnum.call_get_Document_Url.name())) {
            Call<DocumentsModel> call = restRetrofit.getClientService().getDocuments(url);
            callRetrofit(call, flag);
        } else if (flag.equals(DataEnum.call_get_Document_Details.name())) {
            Call<DocumentDetailsModel> call = restRetrofit.getClientService().getDocumentDetails(url);
            callRetrofit(call, flag);
        }

    }



    public void callWithImage(final String flag, @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part postImage) {
        if (flag.equals(DataEnum.call_update_profile.name())) {

            Call<ModelUpdateProfile> call = restRetrofit.getClientService().updateProfile(partMap, postImage);
            callRetrofit(call, flag);
        }


    }
    */
    fun <T> callRetrofit(call: Call<T>?, flag: String?, ShowDialog: Boolean) {
        val progressDialog = Loading(context)
        if (ShowDialog) {
            progressDialog.show()
        }
        call!!.enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                Log.d("test", "onResponse() called with: call = [$call], response = [$response]response returned")
                if (ShowDialog) {
                    progressDialog.dismiss()
                }
                Log.e(TAG, "onResponse: $response")
                if (response.code() == 200) {
                    if (response.isSuccessful && response.body() != null) {
                        if (onRespnse != null) Log.d("testing", "onResponse() minma called with: call = [$call], response = [$response]")
                        onRespnse!!.onResponseSuccess(flag, response.body())
                    }
                    // TODO - 4 Add 400 to condition base on (Login Response)
                } else if (response.code() == 400 || response.code() == 401) {
                    Log.e("res1", "resp")
                    if (onRespnse != null) {
                        Log.e("res2", "resp")
                        try {
                            Log.e("res3", "resp")
                            // Log.e("resp",response.errorBody().string());
                            // onRespnse.onBadRequest(flag, response.errorBody().string());
                            // Log.e("resp",response.errorBody().string());
                            val o = JSONObject(response.errorBody()!!.string())
                            //{"status":{"status":0,"code":400,"message":"The selected email is invalid."}}
                            TastyToast.makeText(context, o.getJSONObject("status").getString("message"), TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                            onRespnse!!.onBadRequest(flag, response.errorBody()!!.string())
                            //{"status":{"status":0,"code":401,"message":"Warning this booking is overlapping with other bookings Dates."}}
                            //401
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                if (ShowDialog) progressDialog.dismiss()
                HelpMe.getInstance(context)!!.retrofironFailure(t)
            }
        })
    }

    companion object {
        /**
         * Created by lenovo on 6/28/2017.
         */
        val TAG = HandelCalls::class.java.simpleName
        private var context: Context? = null
        private var instance: HandelCalls? = null
        private var restRetrofit: RestRetrofit? = null
        //private HandleNoContent onNoContent;
        /**
         * @param context create ana object if it's not already created (singleton)
         * @return reference to that class
         */
        fun getInstance(context: Context?): HandelCalls? {
            Companion.context = context
            if (instance == null) {
                instance = HandelCalls()
                restRetrofit = RestRetrofit.getInstance(context)
            }
            return instance
        }
    }
}