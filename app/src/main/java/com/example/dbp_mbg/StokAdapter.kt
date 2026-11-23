package com.example.dbp_mbg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dbp_mbg.data.StokBahanModel

class StokAdapter(
    private var list: MutableList<StokBahanModel>,
    private val onEdit: (StokBahanModel) -> Unit,
    private val onDelete: (StokBahanModel) -> Unit
) : RecyclerView.Adapter<StokAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaBahan: TextView = view.findViewById(R.id.tvNamaBahan)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvJumlahSatuan: TextView = view.findViewById(R.id.tvJumlahSatuan)
        val tvKategori: TextView = view.findViewById(R.id.tvKategori)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stok, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNamaBahan.text = item.namaBahan
        holder.tvStatus.text = item.status
        holder.tvJumlahSatuan.text = "Jumlah: ${item.jumlah} ${item.satuan}"
        holder.tvKategori.text = "Kategori: ${item.kategori}"

        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<StokBahanModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
