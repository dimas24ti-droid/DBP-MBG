package com.example.dbp_mbg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dbp_mbg.data.DistribusiMakananModel

class DistribusiAdapter(
    private var list: MutableList<DistribusiMakananModel>,
    private val onEdit: (DistribusiMakananModel) -> Unit,
    private val onDelete: (DistribusiMakananModel) -> Unit
) : RecyclerView.Adapter<DistribusiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaLokasi: TextView = view.findViewById(R.id.tvNamaLokasi)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvJumlahPorsi: TextView = view.findViewById(R.id.tvJumlahPorsi)
        val tvPenerima: TextView = view.findViewById(R.id.tvPenerima)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_distribusi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNamaLokasi.text = item.namaLokasi
        holder.tvStatus.text = item.statusDistribusi
        holder.tvJumlahPorsi.text = "${item.jumlahPorsi} Porsi"
        holder.tvPenerima.text = "Penerima: ${item.penerima}"
        holder.tvTanggal.text = "Tanggal: ${item.tanggal}"

        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<DistribusiMakananModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
