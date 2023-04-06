package com.example.tea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val saveBtn : Button = findViewById<Button>(R.id.edit_profile_button)
        val cancelBtn : Button = findViewById<Button>(R.id.cancel_edit_profile_button)

        cancelBtn.setOnClickListener {
            finish()
        }

        saveBtn.setOnClickListener {

        }

    }
}