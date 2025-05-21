package com.example.indonesiapower.ui.kategori

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
import com.example.indonesiapower.model.Kategori
import com.example.indonesiapower.ui.kategori.RiwayatKategoriActivity
import com.example.indonesiapower.ui.kategori.RiwayatKategoriAdapter
import com.example.indonesiapower.ui.kategori.tambah.TambahKategoriActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatKategoriActivity : AppCompatActivity() {

    private var idUser: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatKategoriAdapter

    // ActivityResultLauncher untuk menangkap hasil dari aktivitas lain
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_kategori)
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
            val intent = Intent(this@RiwayatKategoriActivity, TambahKategoriActivity::class.java)
            startActivity(intent)
        }

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewRiwayatKategori)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi Adapter
        adapter = RiwayatKategoriAdapter(emptyList(), startForResult) {
            refreshData()
        }

        recyclerView.adapter = adapter

        // Ambil id_user dari SharedPreferences
        idUser = getUserIdFromSharedPreferences()
        fetchKategori()
    }

    private fun fetchKategori() {
        RetrofitClient.instance.riwayatKategori().enqueue(object : Callback<ApiResponse<List<Kategori>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Kategori>>>,
                response: Response<ApiResponse<List<Kategori>>>
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

            override fun onFailure(call: Call<ApiResponse<List<Kategori>>>, t: Throwable) {
                Log.e("RiwayatMedia", "Gagal menghubungi server: ${t.localizedMessage}", t)
            }
        })
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("id_user", -1)
    }

    private fun refreshData() {
        fetchKategori()
    }
}