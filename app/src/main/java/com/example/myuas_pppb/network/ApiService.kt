package com.example.myuas_pppb.network

import com.example.myuas_pppb.model.Menu
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("menu")
    fun getAllMenu(): Call<List<Menu>>

    @DELETE("menu/{id}")
    suspend fun deleteMenu(@Path("id") id: Int): Response<Unit>

//    @POST("fruits")
//    fun createMenu(@Body request: FruitRequest): Call<GeneralResponse>
//
//    @POST("fruits/{id}")
//    fun updateMenu(@Path("id") id: String, @Body request: FruitRequest): Call<GeneralResponse>
//
//    @DELETE("fruits/{id}")
//    fun deleteMenu(@Path("id") id: String): Call<GeneralResponse>

}