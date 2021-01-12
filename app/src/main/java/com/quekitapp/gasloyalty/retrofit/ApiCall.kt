package com.interactive.ksi.propertyturkeybooking.retrofitconfig

import com.quekitapp.gasloyalty.models.LogiModel
import com.quekitapp.gasloyalty.models.PlateNumberModel
import com.quekitapp.gasloyalty.models.ScanModel
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiCall {
    @GET("User/login.php")
    fun login(@Query("username") username: String?, @Query("password") password: String?): Call<LogiModel?>?

    @GET("Member/ScanTank.php")
    fun scan(@Query("tank_id") tank_id: String?): Call<ScanModel?>?

    @Multipart
    @POST("Member/RecognizePlate.php")
    fun uploadPlateNo(
        @Part snapshot: MultipartBody.Part?,
    ): Call<PlateNumberModel?>?

}