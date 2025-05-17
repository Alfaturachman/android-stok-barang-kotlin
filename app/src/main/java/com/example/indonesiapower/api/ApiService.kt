package com.example.indonesiapower.api

import com.example.indonesiapower.model.Barang
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    // Login
    @POST("login.php")
    fun loginUser(@Body body: RequestBody): Call<ResponseBody>

    @GET("get_all_barang.php")
    fun riwayatBarang(): Call<ApiResponse<List<Barang>>>
}
