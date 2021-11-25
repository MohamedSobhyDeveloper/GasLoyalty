package com.quekitapp.gasloyalty.retrofit

import com.quekitapp.gasloyalty.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiCall {

    @GET("User/login.php")
    fun login(@Query("username") username: String?, @Query("password") password: String?): Call<LogiModel?>?

    @GET("Member/ScanTank.php")
    fun scan(@Query("tank_id") tank_id: String?): Call<ScanModel?>?

    @GET("Member/OpenTank.php")
    fun openTank(): Call<OpenTankModel?>?


    @POST("Member/RecognizePlate.php")
    fun uploadPlateNo(@Body requestBody: VerifyPlate?): Call<ScanModel?>?

    @POST("Member/ChargeGas.php")
    fun charge(@Body requestBody: VerifyBody?): Call<ChargeModel?>?


    @POST("Member/UpdateRecognizedRecord.php")
    fun updatePlate(@Body requestBody: UpdatePlateBody?): Call<UpdatePlateModel?>?


    @POST("User/VerifyOTP.php")
    fun verify(@Body requestBody: VerifyBody?): Call<VerifyModel?>?
}