package com.example.indonesiapower.api

import com.example.indonesiapower.model.Barang
import com.example.indonesiapower.model.Kategori
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

    @POST("tambah_barang.php")
    fun tambahBarang(@Body body: RequestBody): Call<ResponseBody>

    @Multipart
    @POST("edit_barang.php")
    fun editBarang(
        @Part id_user: Int,
        @Part id_kategori: MultipartBody.Part,
        @Part nama: MultipartBody.Part,
        @Part judul: MultipartBody.Part,
        @Part url: MultipartBody.Part,
        @Part gambar: MultipartBody.Part,
        @Part deskripsi: MultipartBody.Part
    ): Call<ApiResponse<Barang>>

    @POST("delete_barang.php")
    fun deleteBarang(@Body body: RequestBody): Call<ResponseBody>

    @GET("get_all_kategori.php")
    fun getKategori(): Call<ApiResponse<List<Kategori>>>
}
