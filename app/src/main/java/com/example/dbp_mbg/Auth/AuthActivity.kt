package com.example.dbp_mbg.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dbp_mbg.MainActivity
import com.example.dbp_mbg.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        btnLogin?.setOnClickListener {
            val username = etUsername?.text?.toString()?.trim() ?: ""
            val password = etPassword?.text?.toString() ?: ""

            if (username.isNotEmpty() && username == password) {

                sharedPref.edit()
                    .putBoolean("isLogin", true)
                    .putString("username", username)
                    .apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {

                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Silahkan coba lagi")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}
