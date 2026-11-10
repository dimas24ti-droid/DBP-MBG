package com.example.dbp_mbg.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.dbp_mbg.Auth.AuthActivity
import com.example.dbp_mbg.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("user_pref", MODE_PRIVATE)
        val isOnboarded = prefs.getBoolean("is_onboarded", false)
        if (isOnboarded) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_onboarding)

        findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
            prefs.edit().putBoolean("is_onboarded", true).apply()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
