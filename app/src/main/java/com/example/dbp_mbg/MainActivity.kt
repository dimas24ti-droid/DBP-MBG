package com.example.dbp_mbg

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStok = findViewById<View>(R.id.btnStok)
        val btnProduksi = findViewById<Button>(R.id.btnProduksi)
        val btnDistribusi = findViewById<Button>(R.id.btnDistribusi)
        val linkWebsite = findViewById<Button>(R.id.linkWebsite)

        btnStok.setOnClickListener {
            val intent = Intent(this, StokActivity::class.java)
            startActivity(intent)
        }

        btnProduksi.setOnClickListener {
            val intent = Intent(this, ProduksiActivity::class.java)
            startActivity(intent)
        }

        btnDistribusi.setOnClickListener {
            val intent = Intent(this, DistribusiActivity::class.java)
            startActivity(intent)
        }

        linkWebsite.setOnClickListener {
            val url = "https://www.bgn.go.id/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
