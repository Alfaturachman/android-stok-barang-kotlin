package com.example.indonesiapower.ui.penerimaan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indonesiapower.R
import com.example.indonesiapower.api.ApiResponse
import com.example.indonesiapower.api.RetrofitClient
import com.example.indonesiapower.model.Penerimaan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatPenerimaanActivity : AppCompatActivity() {

    private var idUser: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatPenerimaanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riwayat_penerimaan)
        supportActionBar?.hide()

        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewRiwayatPenerimaan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RiwayatPenerimaanAdapter(emptyList())

        recyclerView.adapter = adapter

        idUser = getUserIdFromSharedPreferences()
        if (idUser != -1) {
            fetchPenerimaan()
        } else {
            Toast.makeText(this, "ID User tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPenerimaan() {
        RetrofitClient.instance.riwayatPenerimaan().enqueue(object : Callback<ApiResponse<List<Penerimaan>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Penerimaan>>>,
                response: Response<ApiResponse<List<Penerimaan>>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    response.body()?.data?.let {
                        adapter.updateData(it)
                    }
                } else {
                    Toast.makeText(this@RiwayatPenerimaanActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Penerimaan>>>, t: Throwable) {
                Toast.makeText(this@RiwayatPenerimaanActivity, "Kesalahan jaringan: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("id_user", -1)
    }
}
