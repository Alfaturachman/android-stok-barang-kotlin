package com.example.indonesiapower.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.indonesiapower.databinding.FragmentHomeBinding
import com.example.indonesiapower.model.TotalData
import com.example.indonesiapower.api.ApiResponse
import com.example.indonesiapower.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        fetchTotalData()
    }

    private fun fetchTotalData() {
        RetrofitClient.instance.totalData().enqueue(object : Callback<ApiResponse<TotalData>> {
            override fun onResponse(
                call: Call<ApiResponse<TotalData>>,
                response: Response<ApiResponse<TotalData>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val data = response.body()?.data
                    data?.let {
                        binding.tvJumlahAdmin.text = it.total_admin.toString()
                        binding.tvJumlahPetugas.text = it.total_petugas.toString()
                        binding.tvJumlahDivisi.text = it.total_divisi.toString()
                        binding.tvJumlahPengelola.text = it.total_pengelola.toString()
                        binding.tvJumlahBarang.text = it.total_barang.toString()
                        binding.tvJumlahPemeliharaan.text = it.total_pemeliharaan.toString()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<TotalData>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
