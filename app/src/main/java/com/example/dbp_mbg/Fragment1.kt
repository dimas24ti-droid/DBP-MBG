package com.example.dbp_mbg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Fragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        view.findViewById<Button>(R.id.btnOpenStok)?.setOnClickListener {
            startActivity(Intent(requireContext(), StokActivity::class.java))
        }
        return view
    }
}
