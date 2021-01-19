package com.interactive.ksi.propertyturkeybooking.retrofitconfig

import com.quekitapp.gasloyalty.models.*
import retrofit2.Call
import retrofit2.http.*


interface ApiCall {
    @GET("User/login.php")
    fun login(@Query("username") username: String?, @Query("password") password: String?): Call<LogiModel?>?

    @GET("Member/ScanTank.php")
    fun scan(@Query("tank_id") tank_id: String?): Call<ScanModel?>?

    @POST("Member/RecognizePlate.php")
    fun uploadPlateNo(@Body requestBody: VerifyPlate?): Call<PlateNumberModel?>?

    @POST("Member/ChargeGas.php")
    fun charge(@Body requestBody:VerifyBody?): Call<ChargeModel?>?

    @POST("User/VerifyOTP.php")
    fun verify(@Body requestBody:VerifyBody?): Call<VerifyModel?>?


}