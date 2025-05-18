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

    @Multipart
    @POST("tambah_barang.php")
    fun tambahBarang(
        @Part kode_kategori: MultipartBody.Part,
        @Part kode_barang: MultipartBody.Part,
        @Part jenis_barang: MultipartBody.Part,
        @Part nama_barang: MultipartBody.Part,
        @Part pegawai: MultipartBody.Part,
        @Part jabatan: MultipartBody.Part,
        @Part divisi: MultipartBody.Part,
        @Part status: MultipartBody.Part,
        @Part merk: MultipartBody.Part,
        @Part type: MultipartBody.Part,
        @Part kondisi: MultipartBody.Part,
        @Part catatan_tambahan: MultipartBody.Part,
        @Part gambar: MultipartBody.Part
    ): Call<ApiResponse<Barang>>

    @Multipart
    @POST("update_barang.php")
    fun updateBarang(
        @Part id_barang: MultipartBody.Part,
        @Part jenis_barang: MultipartBody.Part,
        @Part nama_barang: MultipartBody.Part,
        @Part pegawai: MultipartBody.Part,
        @Part jabatan: MultipartBody.Part,
        @Part divisi: MultipartBody.Part,
        @Part status: MultipartBody.Part,
        @Part merk: MultipartBody.Part,
        @Part type: MultipartBody.Part,
        @Part kondisi: MultipartBody.Part,
        @Part catatan_tambahan: MultipartBody.Part,
        @Part gambar_barang: MultipartBody.Part,
        @Part kode_kategori: MultipartBody.Part
    ): Call<ApiResponse<Barang>>

    @POST("delete_barang.php")
    fun deleteBarang(@Body body: RequestBody): Call<ResponseBody>

    @GET("get_all_kategori.php")
    fun getKategori(): Call<ApiResponse<List<Kategori>>>
}
