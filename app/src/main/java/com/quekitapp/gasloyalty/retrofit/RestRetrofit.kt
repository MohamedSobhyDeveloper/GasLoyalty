package com.interactive.ksi.propertyturkeybooking.retrofitconfig

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.interactive.ksi.propertyturkeybooking.utlitites.Constant
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil.with
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// start comment
//import okhttp3.logging.HttpLoggingInterceptor;
// end comment
class RestRetrofit private constructor() {
    // public String apiValue = "9c4a06e4dddceb70722de4f3fda4f2c7";
    var apiKey = "api-key"
    var Authorization = "authorization-token"
    private val deviceKey = "device"

    // private String deviceValue = "9584215687459532154865";
    private var deviceValue: String? = ""

    companion object {
        private val TAG = RestRetrofit::class.java.simpleName
        private var instance: RestRetrofit? = null
        lateinit var clientService: ApiCall


        //public final String BASE_URL = "http://192.168.1.222/";
        private var mcontext: Context? = null
        @JvmStatic
        fun getInstance(context: Context?): RestRetrofit? {
            mcontext = context
            if (instance == null) {
                instance = RestRetrofit()
            }
            return instance
        }

        fun getVestionCode(c: Context?): String {
            /*
        p40sdmkkmgjb1ggyadqz
        e4bbe5b7a4c1eb55652965aee885dd59bd2ee7f4
         */
            var v = ""
            try {
                v += c!!.packageManager
                        .getPackageInfo(c.packageName, 0).versionName
                with(c).add(DataEnum.shversionName.name, v)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            // Log.e("log",v);
            return v
        }
    }

    init {
        val builder = OkHttpClient.Builder()
                .readTimeout(6, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)


        val interceptor =  HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor)
        //
//          httpClient.addInterceptor(interceptor).build();
//        builder.addInterceptor { chain ->
//            val request = chain.request()
//            val newRequest: Request
//            val apiValue = Constant.apiValue
//            val token = with(mcontext!!)[DataEnum.AuthToken.name, ""]
//            Log.d(TAG, "intercept() called with: token = [$token]")
//            deviceValue = with(mcontext!!)[DataEnum.shFirebaseToken.name, "utiututtt7t7g7tyyyrr"]
//            Log.e(TAG, "intercept:firebasetoken $deviceValue")
//            if (!token!!.isEmpty() && !deviceValue!!.isEmpty()) {
//                Log.d(TAG, """
//     intercept() first called with: chain  = [$chain]
//     apiValue$apiValue
//     authorization$token
//     firebase $deviceValue
//     ${getVestionCode(mcontext)}
//     """.trimIndent())
//                newRequest = request.newBuilder()
//                        .header(apiKey, apiValue)
//                        .header(Authorization, token)
//                        .header(deviceKey, deviceValue)
//                        .header("version", getVestionCode(mcontext))
//                        .method(request.method(), request.body())
//                        .build()
//                chain.proceed(newRequest)
//            } else if (!token.isEmpty()) {
//                Log.d(TAG, "intercept() second called with: chain = [" + chain + "]" + apiValue + token + deviceValue + getVestionCode(mcontext))
//                newRequest = request.newBuilder()
//                        .header(apiKey, apiValue)
//                        .header(Authorization, token) // todo remove this line when youssef make it possible to login wihout the device
//                        .header(deviceKey, deviceValue)
//                        .header("version", getVestionCode(mcontext))
//                        .method(request.method(), request.body())
//                        .build()
//                chain.proceed(newRequest)
//            } else if (!deviceValue!!.isEmpty()) {
//                Log.d(TAG, "intercept() third called with: chain = [" + chain + "]" + apiValue + token + deviceValue + getVestionCode(mcontext))
//                newRequest = request.newBuilder()
//                        .header(apiKey, apiValue)
//                        .header(deviceKey, deviceValue)
//                        .header("version", getVestionCode(mcontext))
//                        .method(request.method(), request.body())
//                        .build()
//                chain.proceed(newRequest)
//            } else {
//                Log.d(TAG, "intercept() fifth called with: chain = [" + chain + "]" + apiValue + token + deviceValue + getVestionCode(mcontext))
//                newRequest = request.newBuilder()
//                        .header(apiKey, apiValue) // todo remove this line when youssef make it possible to login wihout the device
//                        .header(deviceKey, deviceValue)
//                        .header("version", getVestionCode(mcontext))
//                        .method(request.method(), request.body())
//                        .build()
//                chain.proceed(newRequest)
//            }
//        }
        val httpClient = builder.build()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        clientService = retrofit.create(ApiCall::class.java)
    }

    fun getClientService(): ApiCall {
         return clientService
    }
}