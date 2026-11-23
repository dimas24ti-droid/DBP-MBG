package com.example.dbp_mbg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dbp_mbg.data.DistribusiMakananModel
import com.example.dbp_mbg.data.SupabaseClient
import kotlinx.coroutines.launch

class DistribusiActivity : AppCompatActivity() {

    private lateinit var recyclerViewDistribusi: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRefresh: Button
    private lateinit var btnTambahDistribusi: Button
    private lateinit var adapter: DistribusiAdapter
    private val listDistribusi = mutableListOf<DistribusiMakananModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distribusi)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerViewDistribusi = findViewById(R.id.recyclerViewDistribusi)
        progressBar = findViewById(R.id.progressBar)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnTambahDistribusi = findViewById(R.id.btnTambahDistribusi)

        adapter = DistribusiAdapter(
            listDistribusi,
            onEdit = { item -> showFormDialog(item) },
            onDelete = { item -> confirmDelete(item) }
        )
        recyclerViewDistribusi.layoutManager = LinearLayoutManager(this)
        recyclerViewDistribusi.adapter = adapter

        btnRefresh.setOnClickListener { fetchDistribusiFromSupabase() }
        btnTambahDistribusi.setOnClickListener { showFormDialog(null) }

        fetchDistribusiFromSupabase()
    }

    // READ (Fetch data from Supabase)
    private fun fetchDistribusiFromSupabase() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.getAllDistribusi()
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    listDistribusi.clear()
                    listDistribusi.addAll(data)
                    adapter.notifyDataSetChanged()
                } else {
                    showDemoDataIfError("Supabase Response Error: ${response.code()}")
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                showDemoDataIfError(e.localizedMessage ?: "Gagal terhubung ke Supabase")
            }
        }
    }

    private fun showDemoDataIfError(reason: String) {
        if (listDistribusi.isEmpty()) {
            listDistribusi.clear()
            listDistribusi.add(DistribusiMakananModel(1, "SD Negeri 01 Pagi", 300, "Siswa Kelas 1-6", "Sudah Terkirim", "2026-11-23"))
            listDistribusi.add(DistribusiMakananModel(2, "SD Negeri 02 Pagi", 350, "Siswa Kelas 1-6", "Dalam Pengiriman", "2026-11-23"))
            listDistribusi.add(DistribusiMakananModel(3, "SD Negeri 03 Petang", 300, "Siswa Kelas 1-6", "Menunggu", "2026-11-23"))
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Menampilkan data lokal/demo ($reason)", Toast.LENGTH_SHORT).show()
        }
    }

    // CREATE & UPDATE Dialog
    private fun showFormDialog(item: DistribusiMakananModel?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_distribusi_form, null)
        val tvTitle = view.findViewById<TextView>(R.id.tvDialogTitle)
        val etNamaLokasi = view.findViewById<EditText>(R.id.etNamaLokasi)
        val etJumlahPorsi = view.findViewById<EditText>(R.id.etJumlahPorsi)
        val etPenerima = view.findViewById<EditText>(R.id.etPenerima)
        val etStatus = view.findViewById<EditText>(R.id.etStatus)
        val etTanggal = view.findViewById<EditText>(R.id.etTanggal)

        val isEdit = item != null
        if (isEdit) {
            tvTitle.text = "Edit Data Distribusi"
            etNamaLokasi.setText(item!!.namaLokasi)
            etJumlahPorsi.setText(item.jumlahPorsi.toString())
            etPenerima.setText(item.penerima)
            etStatus.setText(item.statusDistribusi)
            etTanggal.setText(item.tanggal)
        } else {
            tvTitle.text = "Tambah Distribusi Makanan Baru"
            etTanggal.setText("2026-11-23")
        }

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton(if (isEdit) "UPDATE" else "SIMPAN") { dialog, _ ->
                val lokasi = etNamaLokasi.text.toString().trim()
                val porsiStr = etJumlahPorsi.text.toString().trim()
                val penerima = etPenerima.text.toString().trim()
                val status = etStatus.text.toString().trim()
                val tanggal = etTanggal.text.toString().trim()

                if (lokasi.isEmpty() || porsiStr.isEmpty()) {
                    Toast.makeText(this, "Nama Lokasi dan Jumlah Porsi wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val porsi = porsiStr.toIntOrNull() ?: 0

                if (isEdit) {
                    val updatedItem = item!!.copy(
                        namaLokasi = lokasi,
                        jumlahPorsi = porsi,
                        penerima = penerima.ifEmpty { "Penerima Manfaat" },
                        statusDistribusi = status.ifEmpty { "Menunggu" },
                        tanggal = tanggal.ifEmpty { "2026-11-23" }
                    )
                    updateDistribusiInSupabase(updatedItem)
                } else {
                    val newItem = DistribusiMakananModel(
                        namaLokasi = lokasi,
                        jumlahPorsi = porsi,
                        penerima = penerima.ifEmpty { "Penerima Manfaat" },
                        statusDistribusi = status.ifEmpty { "Menunggu" },
                        tanggal = tanggal.ifEmpty { "2026-11-23" }
                    )
                    createDistribusiInSupabase(newItem)
                }
                dialog.dismiss()
            }
            .setNegativeButton("BATAL", null)
            .show()
    }

    // CREATE
    private fun createDistribusiInSupabase(item: DistribusiMakananModel) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.createDistribusi(item = item)
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@DistribusiActivity, "Berhasil menambah jadwal distribusi!", Toast.LENGTH_SHORT).show()
                    fetchDistribusiFromSupabase()
                } else {
                    listDistribusi.add(0, item.copy(id = System.currentTimeMillis()))
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@DistribusiActivity, "Ditambahkan ke tampilan lokal (${response.message()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                listDistribusi.add(0, item.copy(id = System.currentTimeMillis()))
                adapter.notifyDataSetChanged()
                Toast.makeText(this@DistribusiActivity, "Ditambahkan ke tampilan lokal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // UPDATE
    private fun updateDistribusiInSupabase(item: DistribusiMakananModel) {
        if (item.id == null) return
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.updateDistribusi(idQuery = "eq.${item.id}", item = item)
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@DistribusiActivity, "Berhasil update data distribusi!", Toast.LENGTH_SHORT).show()
                    fetchDistribusiFromSupabase()
                } else {
                    val index = listDistribusi.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        listDistribusi[index] = item
                        adapter.notifyDataSetChanged()
                    }
                    Toast.makeText(this@DistribusiActivity, "Diupdate pada tampilan lokal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                val index = listDistribusi.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    listDistribusi[index] = item
                    adapter.notifyDataSetChanged()
                }
                Toast.makeText(this@DistribusiActivity, "Diupdate pada tampilan lokal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // DELETE Confirmation & Action
    private fun confirmDelete(item: DistribusiMakananModel) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Data Distribusi")
            .setMessage("Apakah Anda yakin ingin menghapus pengiriman ke '${item.namaLokasi}'?")
            .setPositiveButton("HAPUS") { _, _ -> deleteDistribusiFromSupabase(item) }
            .setNegativeButton("BATAL", null)
            .show()
    }

    private fun deleteDistribusiFromSupabase(item: DistribusiMakananModel) {
        if (item.id == null) return
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.deleteDistribusi(idQuery = "eq.${item.id}")
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@DistribusiActivity, "Berhasil menghapus data distribusi!", Toast.LENGTH_SHORT).show()
                    fetchDistribusiFromSupabase()
                } else {
                    listDistribusi.removeAll { it.id == item.id }
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@DistribusiActivity, "Dihapus dari tampilan lokal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                listDistribusi.removeAll { it.id == item.id }
                adapter.notifyDataSetChanged()
                Toast.makeText(this@DistribusiActivity, "Dihapus dari tampilan lokal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
