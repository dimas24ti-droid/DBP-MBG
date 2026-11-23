package com.example.dbp_mbg.data

import com.google.gson.annotations.SerializedName

data class StokBahanModel(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("nama_bahan") val namaBahan: String,
    @SerializedName("jumlah") val jumlah: Double,
    @SerializedName("satuan") val satuan: String,
    @SerializedName("kategori") val kategori: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String? = null
)
