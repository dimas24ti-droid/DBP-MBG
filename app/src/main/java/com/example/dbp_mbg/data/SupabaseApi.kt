package com.example.dbp_mbg.data

import retrofit2.Response
import retrofit2.http.*

interface SupabaseApi {

    // ==========================================
    // 1. USERS TABLE (Register & Auth)
    // ==========================================
    @GET("rest/v1/users")
    suspend fun getUserByUsername(
        @Query("username") usernameQuery: String,
        @Query("select") select: String = "*"
    ): Response<List<UserModel>>

    @POST("rest/v1/users")
    suspend fun registerUser(
        @Header("Prefer") prefer: String = "return=representation",
        @Body user: UserModel
    ): Response<List<UserModel>>


    // ==========================================
    // 2. STOK BAHAN TABLE (CRUD Topik 1)
    // ==========================================
    @GET("rest/v1/stok_bahan")
    suspend fun getAllStok(
        @Query("select") select: String = "*",
        @Query("order") order: String = "id.desc"
    ): Response<List<StokBahanModel>>

    @POST("rest/v1/stok_bahan")
    suspend fun createStok(
        @Header("Prefer") prefer: String = "return=representation",
        @Body item: StokBahanModel
    ): Response<List<StokBahanModel>>

    @PATCH("rest/v1/stok_bahan")
    suspend fun updateStok(
        @Query("id") idQuery: String, // e.g. "eq.1"
        @Header("Prefer") prefer: String = "return=representation",
        @Body item: StokBahanModel
    ): Response<List<StokBahanModel>>

    @DELETE("rest/v1/stok_bahan")
    suspend fun deleteStok(
        @Query("id") idQuery: String // e.g. "eq.1"
    ): Response<Unit>


    // ==========================================
    // 3. DISTRIBUSI MAKANAN TABLE (CRUD Topik 2)
    // ==========================================
    @GET("rest/v1/distribusi_makanan")
    suspend fun getAllDistribusi(
        @Query("select") select: String = "*",
        @Query("order") order: String = "id.desc"
    ): Response<List<DistribusiMakananModel>>

    @POST("rest/v1/distribusi_makanan")
    suspend fun createDistribusi(
        @Header("Prefer") prefer: String = "return=representation",
        @Body item: DistribusiMakananModel
    ): Response<List<DistribusiMakananModel>>

    @PATCH("rest/v1/distribusi_makanan")
    suspend fun updateDistribusi(
        @Query("id") idQuery: String, // e.g. "eq.1"
        @Header("Prefer") prefer: String = "return=representation",
        @Body item: DistribusiMakananModel
    ): Response<List<DistribusiMakananModel>>

    @DELETE("rest/v1/distribusi_makanan")
    suspend fun deleteDistribusi(
        @Query("id") idQuery: String // e.g. "eq.1"
    ): Response<Unit>
}
