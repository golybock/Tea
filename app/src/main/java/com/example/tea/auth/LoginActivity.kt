package com.example.tea.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.tv.material3.NavigationDrawer
import com.example.tea.NavigationActivity
import com.example.tea.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.login_button);
        val registrationBtn = findViewById<Button>(R.id.registration_button);

        loginBtn.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }

        registrationBtn.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }
}