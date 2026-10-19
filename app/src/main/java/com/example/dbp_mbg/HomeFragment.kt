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
import com.example.dbp_mbg.Auth.AuthActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

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
