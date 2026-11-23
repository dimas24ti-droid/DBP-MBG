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
import com.example.dbp_mbg.data.StokBahanModel
import com.example.dbp_mbg.data.SupabaseClient
import kotlinx.coroutines.launch

class StokActivity : AppCompatActivity() {

    private lateinit var recyclerViewStok: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRefresh: Button
    private lateinit var btnTambahStok: Button
    private lateinit var adapter: StokAdapter
    private val listStok = mutableListOf<StokBahanModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stok)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerViewStok = findViewById(R.id.recyclerViewStok)
        progressBar = findViewById(R.id.progressBar)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnTambahStok = findViewById(R.id.btnTambahStok)

        adapter = StokAdapter(
            listStok,
            onEdit = { item -> showFormDialog(item) },
            onDelete = { item -> confirmDelete(item) }
        )
        recyclerViewStok.layoutManager = LinearLayoutManager(this)
        recyclerViewStok.adapter = adapter

        btnRefresh.setOnClickListener { fetchStokFromSupabase() }
        btnTambahStok.setOnClickListener { showFormDialog(null) }

        fetchStokFromSupabase()
    }

    // READ (Fetch data from Supabase)
    private fun fetchStokFromSupabase() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.getAllStok()
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    listStok.clear()
                    listStok.addAll(data)
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
        if (listStok.isEmpty()) {
            listStok.clear()
            listStok.add(StokBahanModel(1, "Beras Premium", 500.0, "Kg", "Bahan Pokok", "Tersedia"))
            listStok.add(StokBahanModel(2, "Daging Ayam", 150.0, "Kg", "Lauk Pauk", "Tersedia"))
            listStok.add(StokBahanModel(3, "Minyak Goreng", 50.0, "Liter", "Minyak", "Hampir Habis"))
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Menampilkan data lokal/demo ($reason)", Toast.LENGTH_SHORT).show()
        }
    }

    // CREATE & UPDATE Dialog
    private fun showFormDialog(item: StokBahanModel?) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_stok_form, null)
        val tvTitle = view.findViewById<TextView>(R.id.tvDialogTitle)
        val etNamaBahan = view.findViewById<EditText>(R.id.etNamaBahan)
        val etJumlah = view.findViewById<EditText>(R.id.etJumlah)
        val etSatuan = view.findViewById<EditText>(R.id.etSatuan)
        val etKategori = view.findViewById<EditText>(R.id.etKategori)
        val etStatus = view.findViewById<EditText>(R.id.etStatus)

        val isEdit = item != null
        if (isEdit) {
            tvTitle.text = "Edit Stok Bahan"
            etNamaBahan.setText(item!!.namaBahan)
            etJumlah.setText(item.jumlah.toString())
            etSatuan.setText(item.satuan)
            etKategori.setText(item.kategori)
            etStatus.setText(item.status)
        } else {
            tvTitle.text = "Tambah Stok Bahan Baru"
        }

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton(if (isEdit) "UPDATE" else "SIMPAN") { dialog, _ ->
                val nama = etNamaBahan.text.toString().trim()
                val jumlahStr = etJumlah.text.toString().trim()
                val satuan = etSatuan.text.toString().trim()
                val kategori = etKategori.text.toString().trim()
                val status = etStatus.text.toString().trim()

                if (nama.isEmpty() || jumlahStr.isEmpty() || satuan.isEmpty()) {
                    Toast.makeText(this, "Nama, Jumlah, dan Satuan wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val jumlah = jumlahStr.toDoubleOrNull() ?: 0.0

                if (isEdit) {
                    val updatedItem = item!!.copy(
                        namaBahan = nama,
                        jumlah = jumlah,
                        satuan = satuan,
                        kategori = kategori.ifEmpty { "Umum" },
                        status = status.ifEmpty { "Tersedia" }
                    )
                    updateStokInSupabase(updatedItem)
                } else {
                    val newItem = StokBahanModel(
                        namaBahan = nama,
                        jumlah = jumlah,
                        satuan = satuan,
                        kategori = kategori.ifEmpty { "Umum" },
                        status = status.ifEmpty { "Tersedia" }
                    )
                    createStokInSupabase(newItem)
                }
                dialog.dismiss()
            }
            .setNegativeButton("BATAL", null)
            .show()
    }

    // CREATE
    private fun createStokInSupabase(item: StokBahanModel) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.createStok(item = item)
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@StokActivity, "Berhasil menambah stok!", Toast.LENGTH_SHORT).show()
                    fetchStokFromSupabase()
                } else {
                    // Fallback to local mutation
                    listStok.add(0, item.copy(id = System.currentTimeMillis()))
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@StokActivity, "Ditambahkan ke tampilan lokal (${response.message()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                listStok.add(0, item.copy(id = System.currentTimeMillis()))
                adapter.notifyDataSetChanged()
                Toast.makeText(this@StokActivity, "Ditambahkan ke tampilan lokal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // UPDATE
    private fun updateStokInSupabase(item: StokBahanModel) {
        if (item.id == null) return
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.updateStok(idQuery = "eq.${item.id}", item = item)
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@StokActivity, "Berhasil update stok!", Toast.LENGTH_SHORT).show()
                    fetchStokFromSupabase()
                } else {
                    val index = listStok.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        listStok[index] = item
                        adapter.notifyDataSetChanged()
                    }
                    Toast.makeText(this@StokActivity, "Diupdate pada tampilan lokal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                val index = listStok.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    listStok[index] = item
                    adapter.notifyDataSetChanged()
                }
                Toast.makeText(this@StokActivity, "Diupdate pada tampilan lokal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // DELETE Confirmation & Action
    private fun confirmDelete(item: StokBahanModel) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Stok Bahan")
            .setMessage("Apakah Anda yakin ingin menghapus '${item.namaBahan}'?")
            .setPositiveButton("HAPUS") { _, _ -> deleteStokFromSupabase(item) }
            .setNegativeButton("BATAL", null)
            .show()
    }

    private fun deleteStokFromSupabase(item: StokBahanModel) {
        if (item.id == null) return
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.deleteStok(idQuery = "eq.${item.id}")
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@StokActivity, "Berhasil menghapus stok!", Toast.LENGTH_SHORT).show()
                    fetchStokFromSupabase()
                } else {
                    listStok.removeAll { it.id == item.id }
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@StokActivity, "Dihapus dari tampilan lokal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                listStok.removeAll { it.id == item.id }
                adapter.notifyDataSetChanged()
                Toast.makeText(this@StokActivity, "Dihapus dari tampilan lokal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
