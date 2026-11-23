package com.example.dbp_mbg.data

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("nama_lengkap") val namaLengkap: String,
    @SerializedName("role") val role: String = "User",
    @SerializedName("created_at") val createdAt: String? = null
)
