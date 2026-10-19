package com.example.dbp_mbg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Fragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)
        view.findViewById<Button>(R.id.btnOpenProduksi)?.setOnClickListener {
            startActivity(Intent(requireContext(), ProduksiActivity::class.java))
        }
        return view
    }
}
