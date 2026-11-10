package com.example.dbp_mbg

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dbp_mbg.Auth.AuthActivity
import com.example.dbp_mbg.pertemuan_10.TenthActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewLogs)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        val logList = listOf(
            ItemLog("Distribusi Sesi 1", "Pengiriman ke SDN 01 Pagi (250 Porsi)", "07.30 WIB", "Selesai"),
            ItemLog("Masak Nasi & Lauk", "Selesai memasak porsi harian utama", "06.00 WIB", "Selesai"),
            ItemLog("Penerimaan Stok Beras", "Masuk 500 kg beras dari Bulog", "Kemarin", "Diterima"),
            ItemLog("Pengecekan Kualitas", "Uji higienitas dan gizi makanan oleh ahli gizi", "05.15 WIB", "Lolos")
        )
        recyclerView.adapter = ItemLogAdapter(logList)

        view.findViewById<Button>(R.id.btnPertemuan10)?.setOnClickListener {
            val intent = Intent(requireContext(), TenthActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.btnStok)?.setOnClickListener {
            val intent = Intent(requireContext(), StokActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnProduksi)?.setOnClickListener {
            val intent = Intent(requireContext(), ProduksiActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnDistribusi)?.setOnClickListener {
            val intent = Intent(requireContext(), DistribusiActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnDistribusiProfile)?.setOnClickListener {
            val intent = Intent(requireContext(), DistribusiProfileActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnProfile)?.setOnClickListener {
            val intent = Intent(requireContext(), DeveloperActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.linkWebsite)?.setOnClickListener {
            val url = "https://www.bgn.go.id/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnLogout)?.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah kamu yakin ingin logout?")
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { dialog, _ ->
                    requireActivity().getSharedPreferences(
                        "user_pref",
                        Context.MODE_PRIVATE
                    ).edit()
                        .clear()
                        .apply()

                    dialog.dismiss()

                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .show()
        }

        return view
    }
}
