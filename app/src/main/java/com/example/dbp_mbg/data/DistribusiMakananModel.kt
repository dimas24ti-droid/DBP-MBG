package com.example.dbp_mbg.data

import com.google.gson.annotations.SerializedName

data class DistribusiMakananModel(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("nama_lokasi") val namaLokasi: String,
    @SerializedName("jumlah_porsi") val jumlahPorsi: Int,
    @SerializedName("penerima") val penerima: String,
    @SerializedName("status_distribusi") val statusDistribusi: String,
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("created_at") val createdAt: String? = null
)
