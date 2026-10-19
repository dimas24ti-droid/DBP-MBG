package com.example.dbp_mbg

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class KalkulatorActivity : AppCompatActivity() {

    private lateinit var etPortions: EditText
    private lateinit var btnCalculate: Button
    private lateinit var layoutResult: LinearLayout
    private lateinit var tvBeras: TextView
    private lateinit var tvAyam: TextView
    private lateinit var tvSayur: TextView
    private lateinit var tvBuah: TextView
    private lateinit var btnShareResult: Button

    private var calculatedSummary = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalkulator)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        etPortions = findViewById(R.id.etPortions)
        btnCalculate = findViewById(R.id.btnCalculate)
        layoutResult = findViewById(R.id.layoutResult)
        tvBeras = findViewById(R.id.tvBeras)
        tvAyam = findViewById(R.id.tvAyam)
        tvSayur = findViewById(R.id.tvSayur)
        tvBuah = findViewById(R.id.tvBuah)
        btnShareResult = findViewById(R.id.btnShareResult)

        btnCalculate.setOnClickListener {
            val inputStr = etPortions.text.toString().trim()
            if (inputStr.isNotEmpty()) {
                val porsi = inputStr.toIntOrNull() ?: 0
                if (porsi > 0) {
                    val beras = porsi * 0.1
                    val ayam = porsi * 0.08
                    val sayur = porsi * 0.05
                    val buah = porsi

                    tvBeras.text = "• Beras: ${String.format("%.1f", beras)} kg"
                    tvAyam.text = "• Daging Ayam: ${String.format("%.1f", ayam)} kg"
                    tvSayur.text = "• Sayuran Segar: ${String.format("%.1f", sayur)} kg"
                    tvBuah.text = "• Buah Segar: $buah Buah"

                    calculatedSummary = "=== ESTIMASI KEBUTUHAN BAHAN MBG ===\nJumlah Porsi: $porsi Anak\n- Beras: ${String.format("%.1f", beras)} kg\n- Daging Ayam: ${String.format("%.1f", ayam)} kg\n- Sayuran: ${String.format("%.1f", sayur)} kg\n- Buah: $buah Buah"

                    layoutResult.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "Masukkan jumlah porsi yang valid", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Jumlah porsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        btnShareResult.setOnClickListener {
            if (calculatedSummary.isNotEmpty()) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, calculatedSummary)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, "Bagikan Estimasi via"))
            }
        }
    }
}
