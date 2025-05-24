package com.example.indonesiapower.ui.petugas

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indonesiapower.R
import com.example.indonesiapower.api.ApiResponse
import com.example.indonesiapower.api.RetrofitClient
import com.example.indonesiapower.model.Petugas
import com.example.indonesiapower.ui.petugas.tambah.TambahPetugasActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatPetugasActivity : AppCompatActivity() {

    private var idUser: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatPetugasAdapter

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshData()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_petugas)
        supportActionBar?.hide()

        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        val buttonTambah: Button = findViewById(R.id.buttonTambah)
        buttonTambah.setOnClickListener {
            val intent = Intent(this@RiwayatPetugasActivity, TambahPetugasActivity::class.java)
            startForResult.launch(intent)
        }

        recyclerView = findViewById(R.id.recyclerViewRiwayatPetugas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RiwayatPetugasAdapter(emptyList(), startForResult) {
            refreshData()
        }

        recyclerView.adapter = adapter

        idUser = getUserIdFromSharedPreferences()
        fetchPetugas()
    }

    private fun fetchPetugas() {
        RetrofitClient.instance.riwayatPetugas().enqueue(object : Callback<ApiResponse<List<Petugas>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Petugas>>>,
                response: Response<ApiResponse<List<Petugas>>>
            ) {
                Log.d("RiwayatPetugas", "Response diterima dengan kode: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == true) {
                        Log.d("RiwayatPetugas", "Data berhasil diterima: ${responseBody.data}")
                        responseBody.data?.let {
                            adapter.updateData(it)
                        }
                    } else {
                        Log.e("RiwayatPetugas", "Gagal mendapatkan data: ${responseBody?.message}")
                    }
                } else {
                    Log.e(
                        "RiwayatPetugas",
                        "Request gagal dengan kode: ${response.code()}, pesan: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Petugas>>>, t: Throwable) {
                Log.e("RiwayatPetugas", "Gagal menghubungi server: ${t.localizedMessage}", t)
            }
        })
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("id_user", -1)
    }

    private fun refreshData() {
        fetchPetugas()
    }
}
