package com.example.indonesiapower.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.indonesiapower.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize with dummy data
        setupDummyStatistics()
    }

    private fun setupDummyStatistics() {
        // User statistics
        binding.tvJumlahAdmin.text = "15"
        binding.tvJumlahPetugas.text = "42"
        binding.tvJumlahDivisi.text = "8"
        binding.tvJumlahPengelola.text = "23"

        // Item statistics
        binding.tvJumlahBarang.text = "187"
        binding.tvJumlahJenisPerangkat.text = "15"
        binding.tvJumlahStatusPerbaikanBarang.text = "32"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}