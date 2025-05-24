package com.example.indonesiapower.ui.penerimaan

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.indonesiapower.R
import com.example.indonesiapower.model.Penerimaan

class RiwayatPenerimaanAdapter(
    private var penerimaanList: List<Penerimaan>,
) : RecyclerView.Adapter<RiwayatPenerimaanAdapter.PenerimaanViewHolder>() {

    class PenerimaanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val tvNoTerima: TextView = itemView.findViewById(R.id.tvNoTerima)
        val tvJenisBarang: TextView = itemView.findViewById(R.id.tvJenisBarang)
        val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        val tvTglTerima: TextView = itemView.findViewById(R.id.tvTglTerima)
        val tvJamTerima: TextView = itemView.findViewById(R.id.tvJamTerima)
        val tvSupplier: TextView = itemView.findViewById(R.id.tvSupplier)
        val tvCatatanTambahan: TextView = itemView.findViewById(R.id.tvCatatanTambahan)
    }

    fun updateData(newList: List<Penerimaan>) {
        penerimaanList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenerimaanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daftar_penerimaan, parent, false)
        return PenerimaanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PenerimaanViewHolder, position: Int) {
        val data = penerimaanList[position]

        holder.tvNamaBarang.text = data.nama_barang ?: "Tanpa Nama"
        holder.tvNoTerima.text = "No Terima: ${data.no_terima ?: "-"}"
        holder.tvJenisBarang.text = "Jenis: ${data.jenis_barang ?: "-"}"
        holder.tvJumlah.text = "Jumlah: ${data.jumlah ?: 0}"
        holder.tvTglTerima.text = "Tanggal Terima: ${data.tgl_terima ?: "-"}"
        holder.tvJamTerima.text = "Jam Terima: ${data.jam_terima ?: "-"}"
        holder.tvSupplier.text = "Supplier: ${data.supplier ?: "-"}"
        holder.tvCatatanTambahan.text = "Catatan: ${data.catatan_tambahan ?: "-"}"

        // Tambahkan onClickListener jika ingin membuka detail
        holder.itemView.setOnClickListener {
            // val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            // intent.putExtra("data", data)
            // startForResult.launch(intent)
        }
    }

    override fun getItemCount(): Int = penerimaanList.size
}