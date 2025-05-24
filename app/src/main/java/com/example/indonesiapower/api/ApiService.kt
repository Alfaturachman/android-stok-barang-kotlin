package com.example.indonesiapower.api

import com.example.indonesiapower.model.Barang
import com.example.indonesiapower.model.Kategori
import com.example.indonesiapower.model.Pemeliharaan
import com.example.indonesiapower.model.Penerimaan
import com.example.indonesiapower.model.Petugas
import com.example.indonesiapower.model.TotalData
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
    //////////////////// Home ////////////////////
    @GET("get_total_data.php")
    fun totalData(): Call<ApiResponse<TotalData>>


    //////////////////// Login ////////////////////
    @POST("login.php")
    fun loginUser(@Body body: RequestBody): Call<ResponseBody>


    //////////////////// Barang CRUD ////////////////////
    @GET("get_all_barang.php")
    fun riwayatBarang(): Call<ApiResponse<List<Barang>>>

    @GET("get_otomatis_kode_barang.php")
    fun kodeBarangOtomatis(): Call<ApiResponse<Barang>>

    @Multipart
    @POST("create_barang.php")
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


    //////////////////// Pemeliharaan CRUD ////////////////////
    @GET("get_all_pemeliharaan.php")
    fun riwayatPemeliharaan(): Call<ApiResponse<List<Pemeliharaan>>>

    @Headers("Content-Type: application/json")
    @POST("create_pemeliharaan.php")
    fun tambahPemeliharaan(@Body requestBody: RequestBody): Call<ApiResponse<Pemeliharaan>>

    @POST("get_barang_by_kode.php")
    fun getBarangByKode(@Body body: RequestBody): Call<ApiResponse<List<Barang>>>

    @Headers("Content-Type: application/json")
    @POST("update_pemeliharaan.php")
    fun editPemeliharaan(@Body requestBody: RequestBody): Call<ApiResponse<Pemeliharaan>>

    @POST("delete_pemeliharaan.php")
    fun deletePemeliharaan(@Body body: RequestBody): Call<ResponseBody>


    //////////////////// Penerimaan CRUD ////////////////////
    @GET("get_all_penerimaan.php")
    fun riwayatPenerimaan(): Call<ApiResponse<List<Penerimaan>>>


    //////////////////// Kategori CRUD ////////////////////
    @GET("get_all_kategori.php")
    fun riwayatKategori(): Call<ApiResponse<List<Kategori>>>

    @GET("get_otomatis_kode_kategori.php")
    fun kodeKategoriOtomatis(): Call<ApiResponse<Kategori>>

    @Headers("Content-Type: application/json")
    @POST("create_kategori.php")
    fun tambahKategori(@Body requestBody: RequestBody): Call<ApiResponse<Kategori>>

    @Headers("Content-Type: application/json")
    @POST("update_kategori.php")
    fun editKategori(@Body requestBody: RequestBody): Call<ApiResponse<Kategori>>

    @POST("delete_kategori.php")
    fun deleteKategori(@Body body: RequestBody): Call<ResponseBody>

    //////////////////// Petugas CRUD ////////////////////
    @GET("get_all_petugas.php")
    fun riwayatPetugas(): Call<ApiResponse<List<Petugas>>>
}
