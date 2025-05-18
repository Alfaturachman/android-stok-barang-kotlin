package com.example.indonesiapower.ui.pemeliharaan.tambah

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.indonesiapower.R
import com.google.android.material.textfield.TextInputEditText

class TambahPemeliharaanActivity : AppCompatActivity() {

    private lateinit var spinnerKodeBarang: Spinner
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

    // Data class untuk detail barang
    data class DataDetail(
        val namaBarang: String,
        val pegawai: String,
        val jabatan: String,
        val divisi: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pemeliharaan)
        supportActionBar?.hide()

        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        spinnerKodeBarang = findViewById(R.id.spinnerKodeBarang)
        etNamaBarang = findViewById(R.id.etNamaBarang)
        etPegawai = findViewById(R.id.etPegawai)
        etJabatan = findViewById(R.id.etJabatan)
        etDivisi = findViewById(R.id.etDivisi)
        spinnerKondisi = findViewById(R.id.spinnerKondisi)
        spinnerNamaPetugas = findViewById(R.id.spinnerNamaPetugas)
        etTanggalBarangMasuk = findViewById(R.id.etTanggalBarangMasuk)
        etCatatanTambahan = findViewById(R.id.etCatatanTambahan)
        etTanggalPemeliharaanSelanjutnya = findViewById(R.id.etTanggalPemeliharaanSelanjutnya)
        btnSimpan = findViewById(R.id.btnSimpan)

        btnKembali.setOnClickListener {
            finish()
        }

        // Data dummy
        val kodeBarangList = listOf("KB001", "KB002", "KB003")
        val kondisiList = listOf("Baik", "Rusak Ringan", "Rusak Berat")
        val namaPetugasList = listOf("Andi", "Budi", "Citra")

        val dataDetailMap = mapOf(
            "KB001" to DataDetail(
                namaBarang = "Mesin Pompa",
                pegawai = "Rudi",
                jabatan = "Teknisi",
                divisi = "Pemeliharaan"
            ),
            "KB002" to DataDetail(
                namaBarang = "Generator",
                pegawai = "Sari",
                jabatan = "Supervisor",
                divisi = "Operasi"
            ),
            "KB003" to DataDetail(
                namaBarang = "Kabel Listrik",
                pegawai = "Dewi",
                jabatan = "Teknisi",
                divisi = "Distribusi"
            )
        )

        // Adapter untuk spinnerKodeBarang
        val kodeBarangAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kodeBarangList
        )
        kodeBarangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKodeBarang.adapter = kodeBarangAdapter

        // Adapter untuk spinnerKondisi
        val kondisiAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kondisiList
        )
        kondisiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKondisi.adapter = kondisiAdapter

        // Adapter untuk spinnerNamaPetugas
        val namaPetugasAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            namaPetugasList
        )
        namaPetugasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNamaPetugas.adapter = namaPetugasAdapter

        // Listener saat pilih kode barang
        spinnerKodeBarang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedKode = kodeBarangList[position]
                val detail = dataDetailMap[selectedKode]

                if (detail != null) {
                    etNamaBarang.setText(detail.namaBarang)
                    etPegawai.setText(detail.pegawai)
                    etJabatan.setText(detail.jabatan)
                    etDivisi.setText(detail.divisi)
                } else {
                    // Jika data tidak ditemukan, kosongkan field
                    etNamaBarang.setText("")
                    etPegawai.setText("")
                    etJabatan.setText("")
                    etDivisi.setText("")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Kosongkan jika tidak ada pilihan
                etNamaBarang.setText("")
                etPegawai.setText("")
                etJabatan.setText("")
                etDivisi.setText("")
            }
        }

        btnSimpan.setOnClickListener {
            val kodeBarang = spinnerKodeBarang.selectedItem?.toString() ?: ""
            val namaBarang = etNamaBarang.text.toString()
            val pegawai = etPegawai.text.toString()
            val jabatan = etJabatan.text.toString()
            val divisi = etDivisi.text.toString()
            val kondisi = spinnerKondisi.selectedItem?.toString() ?: ""
            val namaPetugas = spinnerNamaPetugas.selectedItem?.toString() ?: ""
            val tanggalBarangMasuk = etTanggalBarangMasuk.text.toString()
            val catatanTambahan = etCatatanTambahan.text.toString()
            val tanggalPemeliharaanSelanjutnya = etTanggalPemeliharaanSelanjutnya.text.toString()

            Log.d("TambahPemeliharaan", "Kode Barang: $kodeBarang")
            Log.d("TambahPemeliharaan", "Nama Barang: $namaBarang")
            Log.d("TambahPemeliharaan", "Pegawai: $pegawai")
            Log.d("TambahPemeliharaan", "Jabatan: $jabatan")
            Log.d("TambahPemeliharaan", "Divisi: $divisi")
            Log.d("TambahPemeliharaan", "Kondisi: $kondisi")
            Log.d("TambahPemeliharaan", "Nama Petugas: $namaPetugas")
            Log.d("TambahPemeliharaan", "Tanggal Barang Masuk: $tanggalBarangMasuk")
            Log.d("TambahPemeliharaan", "Catatan Tambahan: $catatanTambahan")
            Log.d("TambahPemeliharaan", "Tanggal Pemeliharaan Selanjutnya: $tanggalPemeliharaanSelanjutnya")
        }
    }
}
