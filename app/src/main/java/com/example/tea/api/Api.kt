package com.example.tea.api

import android.content.Context
import android.content.SharedPreferences
import android.os.Looper
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.fragment.app.FragmentActivity
import com.example.tea.database.DatabaseHelper
import com.example.tea.models.Article
import com.example.tea.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Handler
import kotlin.math.log


class Api(val context: FragmentActivity?) {

    private val ENDPOINT = "http://188.164.136.18:8888"  // Im using json-server running on my localhost and emulator
    private val ARTICLES = "/api/Article/getArticles"
    private val ARTICLE = "/api/Article/getArticleById"
    private val LOGIN = "/api/User/login"
    private val GETCLIENT = "/api/User/getUser"
    private val client = OkHttpClient()

    @WorkerThread
    fun getArticles() : List<Article>? {

        val httpUrlConnection = URL(ENDPOINT + ARTICLES).openConnection() as HttpURLConnection
        httpUrlConnection.apply {
            connectTimeout = 10000 // 10 seconds
            requestMethod = "GET"
            doInput = true
        }
        if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
            // show error toast
            return null
        }
        val streamReader = InputStreamReader(httpUrlConnection.inputStream)
        var text: String = ""
        streamReader.use {
            text = it.readText()
        }

        val type = object : TypeToken<List<Article>>(){}.type

        val articles = Gson().fromJson<List<Article>>(text, type)

        httpUrlConnection.disconnect()

        return articles
    }

    @WorkerThread
    fun login(login: String, password : String, save : Boolean) : Boolean {
        val httpUrlConnection = URL(ENDPOINT + LOGIN + "?Login=$login&Password=$password").openConnection() as HttpURLConnection
        val body = JSONObject().apply {
            put("", "")
        }
        httpUrlConnection.apply {
            connectTimeout = 10000 // 10 seconds
            requestMethod = "POST"
            doOutput = true
        }

        if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
            // show error toast
            return false
        }

        val streamReader = InputStreamReader(httpUrlConnection.inputStream)
        var token: String = ""
        streamReader.use {
            token = it.readText()
            if(save){
                val db = DatabaseHelper(context,null)
                db.addProfile(login, password)
            }
        }

        httpUrlConnection.disconnect()

        return true
    }

    @WorkerThread
    fun getToken() : String? {

        val db = DatabaseHelper(context,null)
        val user = db.getProfile()

        val httpUrlConnection = URL(ENDPOINT + LOGIN + "?Login=${user.login}&Password=${user.password}").openConnection() as HttpURLConnection
        val body = JSONObject().apply {
            put("", "")
        }
        httpUrlConnection.apply {
            connectTimeout = 10000 // 10 seconds
            requestMethod = "POST"
            doOutput = true
        }

        if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
            // show error toast
            return null
        }

        val streamReader = InputStreamReader(httpUrlConnection.inputStream)
        var token: String = ""
        streamReader.use {
            token = it.readText()
        }

        httpUrlConnection.disconnect()

        return token
    }

    @WorkerThread
    fun getArticle(id : String) : Article? {

        val httpUrlConnection = URL(ENDPOINT + ARTICLE + id).openConnection() as HttpURLConnection
        httpUrlConnection.apply {
            connectTimeout = 10000 // 10 seconds
            requestMethod = "GET"
            doInput = true
        }
        if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
            // show error toast
            return null
        }
        val streamReader = InputStreamReader(httpUrlConnection.inputStream)
        var text: String = ""
        streamReader.use {
            text = it.readText()
        }

        val type = object : TypeToken<List<Article>>(){}.type

        val articles = Gson().fromJson<List<Article>>(text, type)

        httpUrlConnection.disconnect()

        return articles[0]
    }

    @WorkerThread
    fun getUser() : User? {

        val token = getToken()

        val httpUrlConnection = URL(ENDPOINT + GETCLIENT + token).openConnection() as HttpURLConnection

        httpUrlConnection.apply {
            connectTimeout = 10000 // 10 seconds
            requestMethod = "GET"
            doInput = true

        }
        if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
            // show error toast
            return null
        }
        val streamReader = InputStreamReader(httpUrlConnection.inputStream)
        var text: String = ""
        streamReader.use {
            text = it.readText()
        }

        val type = object : TypeToken<User>(){}.type

        val user = Gson().fromJson<User>(text, type)

        httpUrlConnection.disconnect()

        return user
    }

}
