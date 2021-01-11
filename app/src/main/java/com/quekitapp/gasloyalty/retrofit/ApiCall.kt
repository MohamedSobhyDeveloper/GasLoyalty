package com.interactive.ksi.propertyturkeybooking.retrofitconfig

import com.quekitapp.gasloyalty.models.LogiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiCall {
    @GET("User/login.php")
    fun login(@Query("username")username:String?,@Query("password")password:String?): Call<LogiModel?>?
}