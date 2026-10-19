package com.example.dbp_mbg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Fragment3 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_3, container, false)
        view.findViewById<Button>(R.id.btnOpenDistribusi)?.setOnClickListener {
            startActivity(Intent(requireContext(), DistribusiActivity::class.java))
        }
        view.findViewById<Button>(R.id.btnOpenDistribusiProfile)?.setOnClickListener {
            startActivity(Intent(requireContext(), DistribusiProfileActivity::class.java))
        }
        return view
    }
}
