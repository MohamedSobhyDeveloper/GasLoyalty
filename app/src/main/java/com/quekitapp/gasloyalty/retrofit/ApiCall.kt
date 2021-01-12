package com.interactive.ksi.propertyturkeybooking.retrofitconfig

import com.quekitapp.gasloyalty.models.LogiModel
import com.quekitapp.gasloyalty.models.ScanModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiCall {
    @GET("User/login.php")
    fun login(@Query("username")username:String?,@Query("password")password:String?): Call<LogiModel?>?

    @GET("Member/ScanTank.php")
    fun scan(@Query("tank_id")tank_id :String?): Call<ScanModel?>?
}