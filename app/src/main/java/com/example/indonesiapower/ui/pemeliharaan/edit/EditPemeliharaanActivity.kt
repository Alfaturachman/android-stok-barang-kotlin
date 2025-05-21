package com.example.indonesiapower.ui.pemeliharaan.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.indonesiapower.R
import com.google.android.material.textfield.TextInputEditText

class EditPemeliharaanActivity : AppCompatActivity() {

    // View declarations
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
    private lateinit var btnKembali: ImageButton

    // Data dummy di dalam activity
    private val barangData = listOf(
        BarangDummy(101, "Laptop Asus", "Andi", "Manager", "IT"),
        BarangDummy(102, "Printer Canon", "Budi", "Staff", "Admin"),
        BarangDummy(103, "Proyektor Epson", "Citra", "Supervisor", "Marketing")
    )

    data class BarangDummy(
        val kodeBarang: Int,
        val namaBarang: String,
        val pegawai: String,
        val jabatan: String,
        val divisi: String
    )

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

        // Tombol kembali
        btnKembali.setOnClickListener {
            finish()
        }

        // Ambil data dari intent (jika ada)
        val idPemeliharaan = intent.getStringExtra("id_pemeliharaan")
        val kodeBarangIntent = intent.getStringExtra("kode_barang")
        val tglBarangMasuk = intent.getStringExtra("tgl_barang_masuk")
        val kondisi = intent.getStringExtra("kondisi")
        val catatanTambahan = intent.getStringExtra("catatan_tambahan")
        val tglPemeliharaanSelanjutnya = intent.getStringExtra("tgl_pemeliharaan_selanjutnya")

        // 1. Set data dummy ke Spinner Kode Barang
        val kodeBarangList = barangData.map { it.kodeBarang.toString() }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kodeBarangList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKodeBarang.adapter = adapter

        // 2. Jika data intent kode barang ada, set spinner ke posisi kode barang tersebut
        if (kodeBarangIntent != null) {
            val index = kodeBarangList.indexOf(kodeBarangIntent)
            if (index >= 0) {
                spinnerKodeBarang.setSelection(index)
            }
        }

        // 3. Spinner listener untuk update EditText sesuai kode barang terpilih
        spinnerKodeBarang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedKode = kodeBarangList[position].toInt()
                val selectedBarang = barangData.find { it.kodeBarang == selectedKode }

                if (selectedBarang != null) {
                    etNamaBarang.setText(selectedBarang.namaBarang)
                    etPegawai.setText(selectedBarang.pegawai)
                    etJabatan.setText(selectedBarang.jabatan)
                    etDivisi.setText(selectedBarang.divisi)
                } else {
                    etNamaBarang.setText("")
                    etPegawai.setText("")
                    etJabatan.setText("")
                    etDivisi.setText("")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                etNamaBarang.setText("")
                etPegawai.setText("")
                etJabatan.setText("")
                etDivisi.setText("")
            }
        }

        // 4. Set data lain dari intent ke EditText (jika ada)
        etTanggalBarangMasuk.setText(tglBarangMasuk)
        etCatatanTambahan.setText(catatanTambahan)
        etTanggalPemeliharaanSelanjutnya.setText(tglPemeliharaanSelanjutnya)

        // Jika ingin langsung set data dari intent juga (bisa di-comment jika ingin pakai data dummy sepenuhnya)
        /*
        etNamaBarang.setText(namaBarangIntent)
        etPegawai.setText(pegawaiIntent)
        etJabatan.setText(jabatanIntent)
        etDivisi.setText(divisiIntent)
        */
    }
}
