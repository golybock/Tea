package com.example.tea.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tea.R

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val cancelBtn = findViewById<Button>(R.id.cancel_registration_button);
        val registrationBtn = findViewById<Button>(R.id.registration_button);

        cancelBtn.setOnClickListener {
            finish()
        }

        registrationBtn.setOnClickListener {
            finish()
        }

    }
}