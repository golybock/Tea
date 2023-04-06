package com.example.tea.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.tv.material3.NavigationDrawer
import com.example.tea.NavigationActivity
import com.example.tea.R
import com.example.tea.api.Api
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(tryLogin()){
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }

        val login = findViewById<EditText>(R.id.editText_login)
        val password = findViewById<EditText>(R.id.editText_password)

        val loginBtn = findViewById<Button>(R.id.login_button);
        val saveCheckBox = findViewById<CheckBox>(R.id.remind_checkbox)
        val registrationBtn = findViewById<Button>(R.id.registration_button);

        loginBtn.setOnClickListener {

            val res = login(login.text.toString(), password.text.toString(), saveCheckBox.isChecked)

            if(res){
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }

        registrationBtn.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun tryLogin() : Boolean{
        val api = Api(this)
        var res : String? = api.getToken()

        return res != null
    }

    private fun login(login: String, password : String, save : Boolean) : Boolean{
        val api = Api(this)
        val res = api.login(login, password, save)
        return res
    }
}