package com.example.indonesiapower.ui.barang

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.indonesiapower.R
import com.example.indonesiapower.api.RetrofitClient
import com.example.indonesiapower.model.Barang
import com.example.indonesiapower.ui.barang.detail.DetailBarangActivity

class RiwayatBarangAdapter(
    private var barangList: List<Barang>,
    private val startForResult: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<RiwayatBarangAdapter.BarangViewHolder>() {

    class BarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudul)
        val tvKategori: TextView = itemView.findViewById(R.id.tvKategori)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val ivMedia: ImageView = itemView.findViewById(R.id.ivMedia)
        val cardView: View = itemView.findViewById(R.id.cardView)
    }

    fun updateData(newList: List<Barang>) {
        barangList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daftar_barang, parent, false)
        return BarangViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val barang = barangList[position]
        val context = holder.itemView.context

        holder.tvJudul.text = barang.nama_barang
        holder.tvKategori.text = "Kategori: ${barang.nama_kategori ?: "Tidak Diketahui"}"
        holder.tvStatus.text = "Status: ${barang.status}"

        val statusColor = when (barang.status) {
            "Baik" -> R.color.badge_success
            "Rusak" -> R.color.badge_warning
            else -> R.color.badge_secondary
        }
        holder.tvStatus.backgroundTintList = ContextCompat.getColorStateList(context, statusColor)

        Glide.with(context)
            .load("${RetrofitClient.BASE_URL_UPLOADS}${barang.gambar_barang}")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.ivMedia)

        holder.cardView.setOnClickListener {
            val intent = Intent(context, DetailBarangActivity::class.java).apply {
                putExtra("id_barang", barang.id)
            }
            startForResult.launch(intent)
        }
    }

    override fun getItemCount(): Int = barangList.size
}
