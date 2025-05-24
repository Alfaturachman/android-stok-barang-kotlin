package com.example.indonesiapower.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.indonesiapower.databinding.FragmentDashboardBinding
import com.example.indonesiapower.ui.admin.RiwayatAdminActivity
import com.example.indonesiapower.ui.barang.RiwayatBarangActivity
import com.example.indonesiapower.ui.kategori.RiwayatKategoriActivity
import com.example.indonesiapower.ui.pemeliharaan.RiwayatPemeliharaanActivity
import com.example.indonesiapower.ui.penerimaan.RiwayatPenerimaanActivity
import com.example.indonesiapower.ui.petugas.RiwayatPetugasActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Barang
        binding.cardViewBarang.setOnClickListener {
            val intent = Intent(requireContext(), RiwayatBarangActivity::class.java)
            startActivity(intent)
        }

        // Pemeliharaan
        binding.cardViewPemeliharaan.setOnClickListener {
            val intent = Intent(requireContext(), RiwayatPemeliharaanActivity::class.java)
            startActivity(intent)
        }

        // Kategori
        binding.cardViewKategori.setOnClickListener {
            val intent = Intent(requireContext(), RiwayatKategoriActivity::class.java)
            startActivity(intent)
        }

        // Penerimaan
        binding.cardViewPenerimaan.setOnClickListener {
            val intent = Intent(requireContext(), RiwayatPenerimaanActivity::class.java)
            startActivity(intent)
        }

        // Admin
        binding.cardViewManajemenAdmin.setOnClickListener {
            val intent = Intent(requireContext(), RiwayatAdminActivity::class.java)
            startActivity(intent)
        }

        // Petugas
        binding.cardViewDataPetugas.setOnClickListener {
            val intent = Intent(requireContext(), RiwayatPetugasActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}