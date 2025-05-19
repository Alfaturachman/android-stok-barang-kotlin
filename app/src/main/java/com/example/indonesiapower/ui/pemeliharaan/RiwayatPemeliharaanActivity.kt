package com.example.indonesiapower.ui.pemeliharaan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indonesiapower.R
import com.example.indonesiapower.api.ApiResponse
import com.example.indonesiapower.api.RetrofitClient
import com.example.indonesiapower.model.Barang
import com.example.indonesiapower.ui.barang.RiwayatBarangActivity
import com.example.indonesiapower.ui.barang.RiwayatBarangAdapter
import com.example.indonesiapower.ui.barang.tambah.TambahBarangActivity
import com.example.indonesiapower.ui.pemeliharaan.tambah.TambahPemeliharaanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatPemeliharaanActivity : AppCompatActivity() {

    private var idUser: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatBarangAdapter

    // ActivityResultLauncher untuk menangkap hasil dari aktivitas lain
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshData()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_pemeliharaan)
        supportActionBar?.hide()

        // Set status bar color dan mode light
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Button Kembali
        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        // Button Tambah
        val buttonTambah: Button = findViewById(R.id.buttonTambah)
        buttonTambah.setOnClickListener {
            val intent = Intent(this@RiwayatPemeliharaanActivity, TambahPemeliharaanActivity::class.java)
            startActivity(intent)
        }

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewRiwayatMedia)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi Adapter
        adapter = RiwayatBarangAdapter(emptyList(), startForResult) {
            refreshData()
        }

        recyclerView.adapter = adapter

        // Ambil id_user dari SharedPreferences
        idUser = getUserIdFromSharedPreferences()
        fetchPemeliharaan()
    }

    private fun fetchPemeliharaan() {
        RetrofitClient.instance.riwayatBarang().enqueue(object : Callback<ApiResponse<List<Barang>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Barang>>>,
                response: Response<ApiResponse<List<Barang>>>
            ) {
                Log.d("RiwayatMedia", "Response diterima dengan kode: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == true) {
                        Log.d("RiwayatMedia", "Data berhasil diterima: ${responseBody.data}")
                        responseBody.data?.let {
                            adapter.updateData(it)
                        }
                    } else {
                        Log.e("RiwayatMedia", "Gagal mendapatkan data: ${responseBody?.message}")
                    }
                } else {
                    Log.e(
                        "RiwayatMedia",
                        "Request gagal dengan kode: ${response.code()}, pesan: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Barang>>>, t: Throwable) {
                Log.e("RiwayatMedia", "Gagal menghubungi server: ${t.localizedMessage}", t)
            }
        })
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("id_user", -1)
    }

    private fun refreshData() {
        fetchPemeliharaan()
    }
}