
package com.interactive.ksi.propertyturkeybooking.retrofitconfig

import android.content.Context
import android.content.pm.PackageManager
import com.google.gson.GsonBuilder
import com.interactive.ksi.propertyturkeybooking.utlitites.Constant
import com.interactive.ksi.propertyturkeybooking.utlitites.DataEnum
import com.interactive.ksi.propertyturkeybooking.utlitites.PrefsUtil.with
import com.quekitapp.gasloyalty.retrofit.ApiCall
import okhttp3.OkHttpClient
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

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val httpClient = builder.build()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
        clientService = retrofit.create(ApiCall::class.java)
    }

    fun getClientService(): ApiCall {
        return clientService
    }
}