package com.example.dbp_mbg.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SupabaseClient {

    // Ganti dengan URL Supabase kamu (contoh: "https://xyzcompany.supabase.co/")
    // Pastikan diakhiri dengan tanda miring /
    var SUPABASE_URL = "https://your-project-id.supabase.co/"

    // Ganti dengan Anon / Public Key dari Dashboard Supabase -> Project Settings -> API
    var SUPABASE_KEY = "your-anon-key-here"

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("apikey", SUPABASE_KEY)
            .header("Authorization", "Bearer $SUPABASE_KEY")
            .header("Content-Type", "application/json")

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val api: SupabaseApi by lazy {
        Retrofit.Builder()
            .baseUrl(if (SUPABASE_URL.endsWith("/")) SUPABASE_URL else "$SUPABASE_URL/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApi::class.java)
    }
}
