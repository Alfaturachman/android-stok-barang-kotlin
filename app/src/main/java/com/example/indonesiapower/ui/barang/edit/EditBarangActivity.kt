package com.example.indonesiapower.ui.barang.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.indonesiapower.R
import com.example.indonesiapower.api.ApiResponse
import com.example.indonesiapower.api.RetrofitClient
import com.example.indonesiapower.model.Kategori
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ImageView
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.indonesiapower.model.Barang
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream

class EditBarangActivity : AppCompatActivity() {

    // Deklarasi View
    private lateinit var etKodeBarang: TextInputEditText
    private lateinit var spinnerJenisBarang: Spinner
    private lateinit var etNamaBarang: TextInputEditText
    private lateinit var etPegawai: TextInputEditText
    private lateinit var etJabatan: TextInputEditText
    private lateinit var etDivisi: TextInputEditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var etMerk: TextInputEditText
    private lateinit var etType: TextInputEditText
    private lateinit var spinnerKondisi: Spinner
    private lateinit var etCatatanTambahan: TextInputEditText
    private lateinit var tvNamaFileGambar: TextView
    private lateinit var imageView: ImageView
    private lateinit var btnSimpan: Button
    private lateinit var btnKembali: ImageButton

    // Data dari intent
    private var idBarang: Int = 0
    private var kodeBarang: Int = 0
    private var jenisBarang: String = ""
    private var namaBarang: String = ""
    private var pegawai: String = ""
    private var jabatan: String = ""
    private var divisi: String = ""
    private var status: String = ""
    private var merk: String = ""
    private var type: String = ""
    private var kondisi: String = ""
    private var catatanTambahan: String = ""
    private var gambarBarang: String? = null

    private var selectedKodeKategori: Int? = null
    private var selectedGambarUri: Uri? = null
    private lateinit var selectGambarLauncher: ActivityResultLauncher<Intent>
    private var kategoriList: List<Kategori> = listOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_barang)
        supportActionBar?.hide()

        // Set status bar
        window.statusBarColor = resources.getColor(R.color.white, theme)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Button Kembali
        val btnKembali: ImageButton = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }

        // Ambil data dari intent
        idBarang = intent.getIntExtra("id_barang", 0)
        kodeBarang = intent.getIntExtra("kode_barang", 0)
        jenisBarang = intent.getStringExtra("jenis_barang") ?: ""
        namaBarang = intent.getStringExtra("nama_barang") ?: ""
        pegawai = intent.getStringExtra("pegawai") ?: ""
        jabatan = intent.getStringExtra("jabatan") ?: ""
        divisi = intent.getStringExtra("divisi") ?: ""
        status = intent.getStringExtra("status") ?: ""
        merk = intent.getStringExtra("merk") ?: ""
        type = intent.getStringExtra("type") ?: ""
        kondisi = intent.getStringExtra("kondisi") ?: ""
        catatanTambahan = intent.getStringExtra("catatan_tambahan") ?: ""
        gambarBarang = intent.getStringExtra("gambar_barang")

        // Inisialisasi View
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

        // Isi data ke form
        etKodeBarang.setText(kodeBarang.toString())
        etNamaBarang.setText(namaBarang)
        etPegawai.setText(pegawai)
        etJabatan.setText(jabatan)
        etDivisi.setText(divisi)
        etMerk.setText(merk)
        etType.setText(type)
        etCatatanTambahan.setText(catatanTambahan)

        // Load gambar (gunakan Glide atau Picasso)
        val imageUrl = "${RetrofitClient.BASE_URL_UPLOADS}${gambarBarang}"
        Log.d("IMAGE_LOAD", "Loading image from: $imageUrl")
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)

        // Setup ActivityResult launcher
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

                    Log.d("GAMBAR_UPLOAD", "File URI: $uri")
                    Log.d("GAMBAR_UPLOAD", "File Name: $fileName")
                }
            } else {
                Log.d("GAMBAR_UPLOAD", "File selection canceled")
            }
        }

        // Aksi tombol kembali
        btnKembali.setOnClickListener {
            finish()
        }

        // Isi spinner Status & Kondisi
        val statusList = arrayOf("sewa", "milik dinas")
        val kondisiList = arrayOf("baik", "rusak")

        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = statusAdapter

        // Set selected status
        val statusPosition = statusList.indexOf(status)
        if (statusPosition >= 0) {
            spinnerStatus.setSelection(statusPosition)
        }

        val kondisiAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kondisiList)
        kondisiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKondisi.adapter = kondisiAdapter

        // Set selected kondisi
        val kondisiPosition = kondisiList.indexOf(kondisi)
        if (kondisiPosition >= 0) {
            spinnerKondisi.setSelection(kondisiPosition)
        }

        // Panggil loadKategori() untuk isi spinnerJenisBarang dari API
        loadKategori()

        // Set click listener untuk tombol simpan
        btnSimpan.setOnClickListener {
            updateBarang()
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

                    val kategoriNamaList = kategoriList.map { it.nama_kategori ?: "-" }

                    val adapter = ArrayAdapter(
                        this@EditBarangActivity,
                        android.R.layout.simple_spinner_item,
                        kategoriNamaList
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerJenisBarang.adapter = adapter

                    // Set selected jenis barang setelah data dimuat
                    val jenisBarangPosition = kategoriNamaList.indexOf(jenisBarang)
                    if (jenisBarangPosition >= 0) {
                        spinnerJenisBarang.setSelection(jenisBarangPosition)
                    }

                    // Selected Spinner
                    spinnerJenisBarang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            val selectedKategori = kategoriList[position]
                            val kodeKategori = selectedKategori.kode_kategori
                            val namaKategori = selectedKategori.nama_kategori
                            selectedKodeKategori = selectedKategori.kode_kategori

                            Log.d("EditBarangActivity", "Dipilih: $namaKategori (Kode: $kodeKategori)")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            Log.d("EditBarangActivity", "Tidak ada kategori yang dipilih.")
                        }
                    }
                } else {
                    Toast.makeText(
                        this@EditBarangActivity,
                        "Gagal memuat kategori",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Kategori>>>, t: Throwable) {
                Toast.makeText(
                    this@EditBarangActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateBarang() {
        // Ambil nilai dari form
        val updatedJenisBarang = spinnerJenisBarang.selectedItem.toString()
        val updatedNamaBarang = etNamaBarang.text.toString()
        val updatedPegawai = etPegawai.text.toString()
        val updatedJabatan = etJabatan.text.toString()
        val updatedDivisi = etDivisi.text.toString()
        val updatedStatus = spinnerStatus.selectedItem.toString()
        val updatedMerk = etMerk.text.toString()
        val updatedType = etType.text.toString()
        val updatedKondisi = spinnerKondisi.selectedItem.toString()
        val updatedCatatan = etCatatanTambahan.text.toString()
        val kodeKategori = selectedKodeKategori

        // Validasi form
        if (updatedNamaBarang.isEmpty()) {
            etNamaBarang.error = "Nama barang tidak boleh kosong"
            return
        }

        // ✅ Log semua data sebelum dikirim
        Log.d("UpdateBarang", "ID Barang: $idBarang")
        Log.d("UpdateBarang", "Jenis Barang: $updatedJenisBarang")
        Log.d("UpdateBarang", "Nama Barang: $updatedNamaBarang")
        Log.d("UpdateBarang", "Pegawai: $updatedPegawai")
        Log.d("UpdateBarang", "Jabatan: $updatedJabatan")
        Log.d("UpdateBarang", "Divisi: $updatedDivisi")
        Log.d("UpdateBarang", "Status: $updatedStatus")
        Log.d("UpdateBarang", "Merk: $updatedMerk")
        Log.d("UpdateBarang", "Type: $updatedType")
        Log.d("UpdateBarang", "Kondisi: $updatedKondisi")
        Log.d("UpdateBarang", "Catatan: $updatedCatatan")
        Log.d("UpdateBarang", "Kode Kategori: ${kodeKategori ?: "null"}")
        Log.d("UpdateBarang", "Gambar URI: ${selectedGambarUri?.toString() ?: "Tidak ada"}")

        // Tampilkan loading
        Toast.makeText(this, "Menyimpan perubahan...", Toast.LENGTH_SHORT).show()

        // Konversi data ke MultipartBody.Part
        val idBarangPart = MultipartBody.Part.createFormData("id_barang", (idBarang ?: 0).toString())
        val jenisBarangPart = MultipartBody.Part.createFormData("jenis_barang", updatedJenisBarang)
        val namaBarangPart = MultipartBody.Part.createFormData("nama_barang", updatedNamaBarang)
        val pegawaiPart = MultipartBody.Part.createFormData("pegawai", updatedPegawai)
        val jabatanPart = MultipartBody.Part.createFormData("jabatan", updatedJabatan)
        val divisiPart = MultipartBody.Part.createFormData("divisi", updatedDivisi)
        val statusPart = MultipartBody.Part.createFormData("status", updatedStatus)
        val merkPart = MultipartBody.Part.createFormData("merk", updatedMerk)
        val typePart = MultipartBody.Part.createFormData("type", updatedType)
        val kondisiPart = MultipartBody.Part.createFormData("kondisi", updatedKondisi)
        val catatanPart = MultipartBody.Part.createFormData("catatan", updatedCatatan)
        val kodeKategoriPart = MultipartBody.Part.createFormData("kode_kategori", (selectedKodeKategori ?: 0).toString())

        // Handle gambar (opsional - hanya jika ada perubahan gambar)
        val gambarPart = if (selectedGambarUri != null) {
            getFilePart(selectedGambarUri!!) ?: run {
                Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            // Jika tidak ada gambar baru yang dipilih, kirim null atau kosong
            MultipartBody.Part.createFormData("gambar", "")
        }

        // Panggil API untuk update
        RetrofitClient.instance.updateBarang(
            idBarangPart,
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
            gambarPart,
            kodeKategoriPart
        ).enqueue(object : Callback<ApiResponse<Barang>> {
            override fun onResponse(call: Call<ApiResponse<Barang>>, response: Response<ApiResponse<Barang>>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.status) {
                        // Log data yang diupdate
                        Log.d("UpdateBarang", "Nama Barang: $updatedNamaBarang")
                        Log.d("UpdateBarang", "Jenis Barang: $updatedJenisBarang")
                        Log.d("UpdateBarang", "Status: $updatedStatus")

                        Toast.makeText(this@EditBarangActivity, "Berhasil memperbarui barang", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UpdateBarang", "Gagal update barang. Error: $errorBody")
                        Toast.makeText(this@EditBarangActivity, "Gagal memperbarui barang", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UpdateBarang", "Response error: ${response.code()} - ${response.message()}. Error body: $errorBody")
                    Toast.makeText(this@EditBarangActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Barang>>, t: Throwable) {
                Log.e("UpdateBarang", "Gagal menghubungi server: ${t.message}", t)
                Toast.makeText(this@EditBarangActivity, "Gagal menghubungi server!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi yang dipanggil saat LinearLayout diklik (sesuai dengan android:onClick di XML)
    fun selectGambar(view: View) {
        // Untuk Android 13+ kita tidak perlu permission untuk galeri
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

            // Tentukan tipe MIME berdasarkan ekstensi file
            val mimeType = contentResolver.getType(uri) ?: "image/*"
            val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("gambar", file.name, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}