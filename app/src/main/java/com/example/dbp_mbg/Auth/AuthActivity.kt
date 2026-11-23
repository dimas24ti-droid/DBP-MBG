package com.example.dbp_mbg.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dbp_mbg.MainActivity
import com.example.dbp_mbg.R
import com.example.dbp_mbg.data.SupabaseClient
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        btnRegister?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin?.setOnClickListener {
            val username = etUsername?.text?.toString()?.trim() ?: ""
            val password = etPassword?.text?.toString() ?: ""

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Attempt login via Supabase API
            lifecycleScope.launch {
                try {
                    val response = SupabaseClient.api.getUserByUsername(usernameQuery = "eq.$username")
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        val user = response.body()!!.first()
                        if (user.password == password) {
                            sharedPref.edit()
                                .putBoolean("isLogin", true)
                                .putString("username", user.username)
                                .putString("nama_lengkap", user.namaLengkap)
                                .apply()

                            Toast.makeText(this@AuthActivity, "Selamat datang, ${user.namaLengkap}!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@AuthActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            return@launch
                        } else {
                            AlertDialog.Builder(this@AuthActivity)
                                .setTitle("Login Gagal")
                                .setMessage("Password yang kamu masukkan salah!")
                                .setPositiveButton("OK", null)
                                .show()
                            return@launch
                        }
                    } else if (username == password) { // Fallback demo condition
                        sharedPref.edit()
                            .putBoolean("isLogin", true)
                            .putString("username", username)
                            .apply()

                        val intent = Intent(this@AuthActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@launch
                    } else {
                        AlertDialog.Builder(this@AuthActivity)
                            .setTitle("Login Gagal")
                            .setMessage("User tidak ditemukan di Supabase!")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                } catch (e: Exception) {
                    // Fallback to demo login if Supabase not configured yet
                    if (username == password) {
                        sharedPref.edit()
                            .putBoolean("isLogin", true)
                            .putString("username", username)
                            .apply()

                        val intent = Intent(this@AuthActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        AlertDialog.Builder(this@AuthActivity)
                            .setTitle("Login Gagal")
                            .setMessage("Gagal terhubung ke Supabase. Pastikan URL & API Key sudah sesuai.\n\nDetail: ${e.localizedMessage}")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }
        }
    }
}
