package com.example.indonesiapower.ui.pemeliharaan.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.indonesiapower.R
import com.example.indonesiapower.api.ApiResponse
import com.example.indonesiapower.api.RetrofitClient
import com.example.indonesiapower.model.Barang
import com.example.indonesiapower.model.Petugas
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPemeliharaanActivity : AppCompatActivity() {

    // View declarations
    private lateinit var etKodeBarang: TextInputEditText
    private lateinit var etNamaBarang: TextInputEditText
    private lateinit var etPegawai: TextInputEditText
    private lateinit var etJabatan: TextInputEditText
    private lateinit var etDivisi: TextInputEditText
    private lateinit var spinnerKondisi: Spinner
    private lateinit var spinnerNamaPetugas: Spinner
    private lateinit var etTanggalBarangMasuk: TextInputEditText
    private lateinit var etCatatanTambahan: TextInputEditText
    private lateinit var etTanggalPemeliharaanSelanjutnya: TextInputEditText
    private lateinit var btnSimpan: Button
    private lateinit var btnKembali: ImageButton

    private lateinit var petugasList: List<Petugas>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pemeliharaan)
        supportActionBar?.hide()

        // Set status bar light mode
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Inisialisasi Views
        btnKembali = findViewById(R.id.btnKembali)
        etNamaBarang = findViewById(R.id.etNamaBarang)
        etKodeBarang = findViewById(R.id.etKodeBarang)
        etPegawai = findViewById(R.id.etPegawai)
        etJabatan = findViewById(R.id.etJabatan)
        etDivisi = findViewById(R.id.etDivisi)
        spinnerKondisi = findViewById(R.id.spinnerKondisi)
        spinnerNamaPetugas = findViewById(R.id.spinnerNamaPetugas)
        etTanggalBarangMasuk = findViewById(R.id.etTanggalBarangMasuk)
        etCatatanTambahan = findViewById(R.id.etCatatanTambahan)
        etTanggalPemeliharaanSelanjutnya = findViewById(R.id.etTanggalPemeliharaanSelanjutnya)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Tombol kembali
        btnKembali.setOnClickListener {
            finish()
        }

        // Ambil data dari intent (jika ada)
        val idPemeliharaan = intent.getIntExtra("id_pemeliharaan", 0)
        val kodeBarang = intent.getIntExtra("kode_barang", 0)
        val tglBarangMasuk = intent.getStringExtra("tgl_barang_masuk")
        val kondisi = intent.getStringExtra("kondisi")
        val catatanTambahan = intent.getStringExtra("catatan_tambahan")
        val tglPemeliharaanSelanjutnya = intent.getStringExtra("tgl_pemeliharaan_selanjutnya")

        etKodeBarang.setText(kodeBarang.toString())
        etCatatanTambahan.setText(catatanTambahan)
        etTanggalBarangMasuk.setText(tglBarangMasuk)
        etTanggalPemeliharaanSelanjutnya.setText(tglPemeliharaanSelanjutnya)

        fetchBarangByKode(kodeBarang.toString())
        
        val kondisiList = listOf("Sudah Diperbaiki", "Rusak", "Butuh Perbaikan")
        val adapterKondisi = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kondisiList
        )
        adapterKondisi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKondisi.adapter = adapterKondisi

    }

    private fun fetchBarangByKode(selectedKode: String) {
        // Buat JSON body secara manual
        val jsonObject = JSONObject()
        jsonObject.put("kode_barang", selectedKode)
        val jsonBody = jsonObject.toString()
        val requestBody: RequestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())

        // Panggil API Retrofit dengan RequestBody yang sudah dibuat
        RetrofitClient.instance.getBarangByKode(requestBody)
            .enqueue(object : Callback<ApiResponse<List<Barang>>> {
                override fun onResponse(
                    call: Call<ApiResponse<List<Barang>>>,
                    response: Response<ApiResponse<List<Barang>>>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val dataBarang = response.body()?.data
                        if (!dataBarang.isNullOrEmpty()) {
                            val barang = dataBarang[0]  // ambil item pertama
                            etNamaBarang.setText(barang.nama_barang)
                            etPegawai.setText(barang.pegawai)
                            etJabatan.setText(barang.jabatan)
                            etDivisi.setText(barang.divisi)
                        } else {
                            clearEditTexts()
                        }
                    } else {
                        clearEditTexts()
                        Toast.makeText(this@EditPemeliharaanActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<List<Barang>>>, t: Throwable) {
                    clearEditTexts()
                    Toast.makeText(this@EditPemeliharaanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadNamaPetugas() {
        RetrofitClient.instance.riwayatPetugas().enqueue(object : Callback<ApiResponse<List<Petugas>>> {
            override fun onResponse(call: Call<ApiResponse<List<Petugas>>>, response: Response<ApiResponse<List<Petugas>>>) {
                if (response.isSuccessful && response.body() != null) {
                    petugasList = response.body()!!.data ?: emptyList()

                    val namaPetugasList = petugasList.map { it.nama.toString() }

                    val adapter = ArrayAdapter(
                        this@EditPemeliharaanActivity,
                        android.R.layout.simple_spinner_item,
                        namaPetugasList
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerNamaPetugas.adapter = adapter

                    spinnerNamaPetugas.setSelection(0, true)

                } else {
                    Toast.makeText(this@EditPemeliharaanActivity, "Gagal load data barang", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Petugas>>>, t: Throwable) {
                Toast.makeText(this@EditPemeliharaanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearEditTexts() {
        etNamaBarang.setText("")
        etPegawai.setText("")
        etJabatan.setText("")
        etDivisi.setText("")
    }
}
