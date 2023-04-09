package com.example.tea.auth

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tea.NavigationActivity
import com.example.tea.R
import com.example.tea.database.DatabaseHelper
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {

    lateinit var login : EditText
    lateinit var password : EditText
    lateinit var loginBtn : Button
    lateinit var saveCheckBox : CheckBox
    lateinit var registrationBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        tryLogin()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = findViewById<EditText>(R.id.editText_login)
        password = findViewById<EditText>(R.id.editText_password)

        loginBtn = findViewById<Button>(R.id.login_button);
        saveCheckBox = findViewById<CheckBox>(R.id.remind_checkbox)
        registrationBtn = findViewById<Button>(R.id.registration_button);

        loginBtn.setOnClickListener {
            loginAsync()
        }

        registrationBtn.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun tryLogin(){
        val thread = Thread{
            val db = DatabaseHelper(this,null)
            var user = db.getGuest()
            if(user.id == 0){
                user = db.getProfile()
            }

            val httpUrlConnection = URL("http://188.164.136.18:8888" + "/api/User/login" + "?Login=${user.login}&Password=${user.password}").openConnection() as HttpURLConnection

            httpUrlConnection.apply {
                connectTimeout = 10000 // 10 seconds
                requestMethod = "POST"
                doOutput = true
            }

            if (httpUrlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                httpUrlConnection.disconnect()
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        thread.start()
            thread.join()
    }


    private fun loginAsync(){
        Thread{
            val res = login(login.text.toString(), password.text.toString(), saveCheckBox.isChecked)

            if(res){
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                this.runOnUiThread(Runnable {
                    Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                })

            }
        }.start()
    }

    fun login(login: String, password : String, save : Boolean) : Boolean {
        val httpUrlConnection = URL("http://188.164.136.18:8888" + "/api/User/login" + "?Login=$login&Password=$password").openConnection() as HttpURLConnection
        httpUrlConnection.apply {
            connectTimeout = 10000 // 10 seconds
            requestMethod = "POST"
            doOutput = true
        }

        if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
            return false
        }

        val streamReader = InputStreamReader(httpUrlConnection.inputStream)
        var token: String = ""
        streamReader.use {
            token = it.readText()
            if(save){
                val db = DatabaseHelper(this,null)
                db.addProfile(login, password)
            }
            else{
                val db = DatabaseHelper(this,null)
                db.addGuest(login, password)
            }
        }

        httpUrlConnection.disconnect()

        return true
    }
}