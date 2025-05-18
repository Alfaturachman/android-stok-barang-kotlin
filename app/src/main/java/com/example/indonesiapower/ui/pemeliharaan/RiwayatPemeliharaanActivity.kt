package com.example.indonesiapower.ui.pemeliharaan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.indonesiapower.R
import com.example.indonesiapower.ui.barang.RiwayatBarangActivity
import com.example.indonesiapower.ui.barang.tambah.TambahBarangActivity
import com.example.indonesiapower.ui.pemeliharaan.tambah.TambahPemeliharaanActivity

class RiwayatPemeliharaanActivity : AppCompatActivity() {
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
    }
}