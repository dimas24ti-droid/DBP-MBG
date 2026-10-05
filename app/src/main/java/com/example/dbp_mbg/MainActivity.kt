package com.example.dbp_mbg

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dbp_mbg.Auth.AuthActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnStok)?.setOnClickListener {
            val intent = Intent(this, StokActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnProduksi)?.setOnClickListener {
            val intent = Intent(this, ProduksiActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnDistribusi)?.setOnClickListener {
            val intent = Intent(this, DistribusiActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnProfile)?.setOnClickListener {
            val intent = Intent(this, DeveloperActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linkWebsite)?.setOnClickListener {
            val url = "https://www.bgn.go.id/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnLogout)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah kamu yakin ingin logout?")
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { dialog, _ ->

                    getSharedPreferences(
                        "user_pref",
                        MODE_PRIVATE
                    ).edit()
                        .clear()
                        .apply()

                    dialog.dismiss()

                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .show()
        }
    }
}
