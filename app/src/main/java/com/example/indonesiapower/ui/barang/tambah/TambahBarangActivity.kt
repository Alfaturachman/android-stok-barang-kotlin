package com.example.indonesiapower.ui.barang.tambah

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.indonesiapower.R
import com.example.indonesiapower.api.ApiResponse
import com.example.indonesiapower.api.RetrofitClient
import com.example.indonesiapower.model.Barang
import com.example.indonesiapower.model.Kategori
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream

class TambahBarangActivity : AppCompatActivity() {

    // Deklarasi View
    private lateinit var etKodeBarang: EditText
    private lateinit var spinnerJenisBarang: Spinner
    private lateinit var etNamaBarang: EditText
    private lateinit var etPegawai: EditText
    private lateinit var etJabatan: EditText
    private lateinit var etDivisi: EditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var etMerk: EditText
    private lateinit var etType: EditText
    private lateinit var spinnerKondisi: Spinner
    private lateinit var etCatatanTambahan: EditText
    private lateinit var tvNamaFileGambar: TextView
    private lateinit var imageView: ImageView
    private lateinit var btnSimpan: Button

    private var selectedKodeKategori: Int? = null
    private var selectedGambarUri: Uri? = null
    private lateinit var selectGambarLauncher: ActivityResultLauncher<Intent>
    private var kategoriList: List<Kategori> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_barang)
        supportActionBar?.hide()

        // Set status bar
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Inisialisasi View
        initViews()
        loadKodeBarangOtomatis()
        setupImagePicker()
        loadKategori()
        setupSpinners()
        setupButtonListeners()
    }

    private fun initViews() {
        etKodeBarang = findViewById(R.id.etKodeBarang)
        spinnerJenisBarang = findViewById(R.id.spinnerJenisBarang)
        etNamaBarang = findViewById(R.id.etNamaBarang)
        etPegawai = findViewById(R.id.etPegawai)
        etJabatan = findViewById(R.id.etJabatan)
        etDivisi = findViewById(R.id.etDivisi)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        etMerk = findViewById(R.id.etMerk)
        etType = findViewById(R.id.etType)
        spinnerKondisi = findViewById(R.id.spinnerKondisi)
        etCatatanTambahan = findViewById(R.id.etCatatanTambahan)
        tvNamaFileGambar = findViewById(R.id.tvNamaFileGambar)
        imageView = findViewById(R.id.imageView)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Button Kembali
        findViewById<ImageButton>(R.id.btnKembali).setOnClickListener {
            finish()
        }
    }

    private fun setupImagePicker() {
        selectGambarLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    selectedGambarUri = it
                    val fileName = getFileName(it)
                    tvNamaFileGambar.text = fileName

                    // Tampilkan gambar yang dipilih di ImageView
                    try {
                        val inputStream = contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imageView.setImageBitmap(bitmap)
                        inputStream?.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupButtonListeners() {
        btnSimpan.setOnClickListener {
            simpanBarang()
        }
    }

    private fun loadKategori() {
        RetrofitClient.instance.getKategori().enqueue(object : Callback<ApiResponse<List<Kategori>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Kategori>>>,
                response: Response<ApiResponse<List<Kategori>>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    kategoriList = response.body()?.data ?: listOf()
                    setupKategoriSpinner()
                } else {
                    Toast.makeText(this@TambahBarangActivity, "Gagal memuat kategori", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Kategori>>>, t: Throwable) {
                Toast.makeText(this@TambahBarangActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupKategoriSpinner() {
        val kategoriNamaList = kategoriList.map { it.nama_kategori ?: "-" }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kategoriNamaList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenisBarang.adapter = adapter

        spinnerJenisBarang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedKodeKategori = kategoriList[position].kode_kategori
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Tidak ada aksi
            }
        }
    }

    private fun setupSpinners() {
        val statusList = arrayOf("sewa", "milik dinas")
        val kondisiList = arrayOf("baik", "rusak")

        // Setup spinner status
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStatus.adapter = adapter
        }

        // Setup spinner kondisi
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kondisiList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKondisi.adapter = adapter
        }
    }

    fun selectGambar(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        selectGambarLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String {
        var result = "Unknown"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = cursor.getString(index)
                }
            }
        }
        return result
    }

    private fun getFilePart(uri: Uri): MultipartBody.Part? {
        return try {
            val fileDescriptor = contentResolver.openFileDescriptor(uri, "r") ?: return null
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val file = File(cacheDir, getFileName(uri))
            file.outputStream().use { output -> inputStream.copyTo(output) }

            val mimeType = contentResolver.getType(uri) ?: "image/*"
            val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("gambar", file.name, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadKodeBarangOtomatis() {
        RetrofitClient.instance.kodeBarangOtomatis().enqueue(object : Callback<ApiResponse<Barang>> {
            override fun onResponse(call: Call<ApiResponse<Barang>>, response: Response<ApiResponse<Barang>>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val kodeBarang = response.body()?.data?.kode_barang ?: ""
                    etKodeBarang.setText(kodeBarang.toString())
                } else {
                    Toast.makeText(this@TambahBarangActivity, "Gagal mendapatkan kode barang", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Barang>>, t: Throwable) {
                Toast.makeText(this@TambahBarangActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun simpanBarang() {
        val kodeBarang = etKodeBarang.text.toString()
        val jenisBarang = spinnerJenisBarang.selectedItem.toString()
        val namaBarang = etNamaBarang.text.toString()
        val pegawai = etPegawai.text.toString()
        val jabatan = etJabatan.text.toString()
        val divisi = etDivisi.text.toString()
        val status = spinnerStatus.selectedItem.toString()
        val merk = etMerk.text.toString()
        val type = etType.text.toString()
        val kondisi = spinnerKondisi.selectedItem.toString()
        val catatan = etCatatanTambahan.text.toString()

        // Validasi
        if (namaBarang.isEmpty()) {
            etNamaBarang.error = "Nama barang tidak boleh kosong"
            return
        }

        if (selectedKodeKategori == null) {
            Toast.makeText(this, "Pilih kategori barang", Toast.LENGTH_SHORT).show()
            return
        }

        val gambarPart = selectedGambarUri?.let { getFilePart(it) } ?: run {
            Toast.makeText(this, "Pilih gambar barang", Toast.LENGTH_SHORT).show()
            return
        }

        // Konversi data ke MultipartBody.Part
        val kodeKategoriPart = MultipartBody.Part.createFormData("kode_kategori", selectedKodeKategori.toString())
        val kodeBarangPart = MultipartBody.Part.createFormData("kode_barang", kodeBarang)
        val jenisBarangPart = MultipartBody.Part.createFormData("jenis_barang", jenisBarang)
        val namaBarangPart = MultipartBody.Part.createFormData("nama_barang", namaBarang)
        val pegawaiPart = MultipartBody.Part.createFormData("pegawai", pegawai)
        val jabatanPart = MultipartBody.Part.createFormData("jabatan", jabatan)
        val divisiPart = MultipartBody.Part.createFormData("divisi", divisi)
        val statusPart = MultipartBody.Part.createFormData("status", status)
        val merkPart = MultipartBody.Part.createFormData("merk", merk)
        val typePart = MultipartBody.Part.createFormData("type", type)
        val kondisiPart = MultipartBody.Part.createFormData("kondisi", kondisi)
        val catatanPart = MultipartBody.Part.createFormData("catatan_tambahan", catatan)

        // Panggil API
        RetrofitClient.instance.tambahBarang(
            kodeKategoriPart,
            kodeBarangPart,
            jenisBarangPart,
            namaBarangPart,
            pegawaiPart,
            jabatanPart,
            divisiPart,
            statusPart,
            merkPart,
            typePart,
            kondisiPart,
            catatanPart,
            gambarPart
        ).enqueue(object : Callback<ApiResponse<Barang>> {
            override fun onResponse(call: Call<ApiResponse<Barang>>, response: Response<ApiResponse<Barang>>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(this@TambahBarangActivity, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Log.e("TambahBarang", "Response error: code=${response.code()}, body=${response.errorBody()?.string()}")
                    Toast.makeText(this@TambahBarangActivity, "Gagal menambahkan barang", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Barang>>, t: Throwable) {
                Log.e("TambahBarang", "Request failed", t)
                Toast.makeText(this@TambahBarangActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}