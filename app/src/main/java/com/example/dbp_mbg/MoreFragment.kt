package com.example.dbp_mbg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class MoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        view.findViewById<Button>(R.id.btnOpenKalkulator)?.setOnClickListener {
            val intent = Intent(requireContext(), KalkulatorActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnOpenJadwalMenu)?.setOnClickListener {
            val intent = Intent(requireContext(), JadwalMenuActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnShareReport)?.setOnClickListener {
            val report = "=== LAPORAN OPERASIONAL HARIAN DBP-MBG ===\n" +
                    "Tanggal: Hari Ini\n" +
                    "Total Produksi: 1.250 Porsi\n" +
                    "Jumlah Penerima: 1.200 Anak\n" +
                    "Stok Bahan Tersedia: 12 Jenis\n" +
                    "Status Distribusi: Aman & Lancar\n" +
                    "Dikelola oleh Sistem DBP-MBG Nasional."

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, report)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "Ekspor & Bagikan Laporan via"))
        }

        return view
    }
}
