package com.example.dbp_mbg.Auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dbp_mbg.R
import com.example.dbp_mbg.data.SupabaseClient
import com.example.dbp_mbg.data.UserModel
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNamaLengkap: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSubmitRegister: Button
    private lateinit var tvToLogin: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNamaLengkap = findViewById(R.id.etNamaLengkap)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister)
        tvToLogin = findViewById(R.id.tvToLogin)
        progressBar = findViewById(R.id.progressBar)

        btnSubmitRegister.setOnClickListener {
            performRegister()
        }

        tvToLogin.setOnClickListener {
            finish()
        }
    }

    private fun performRegister() {
        val namaLengkap = etNamaLengkap.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (namaLengkap.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnSubmitRegister.isEnabled = false

        val newUser = UserModel(
            username = username,
            email = email,
            password = password,
            namaLengkap = namaLengkap
        )

        lifecycleScope.launch {
            try {
                val response = SupabaseClient.api.registerUser(user = newUser)
                progressBar.visibility = View.GONE
                btnSubmitRegister.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi Berhasil! Silakan Login.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Terjadi kesalahan"
                    AlertDialog.Builder(this@RegisterActivity)
                        .setTitle("Registrasi Gagal")
                        .setMessage("Error from Supabase: $errorMsg")
                        .setPositiveButton("OK", null)
                        .show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                btnSubmitRegister.isEnabled = true
                AlertDialog.Builder(this@RegisterActivity)
                    .setTitle("Koneksi / Config Error")
                    .setMessage("Gagal terhubung ke Supabase. Pastikan SUPABASE_URL dan SUPABASE_KEY di SupabaseClient.kt sudah diisi dengan benar.\n\nDetail: ${e.localizedMessage}")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}
