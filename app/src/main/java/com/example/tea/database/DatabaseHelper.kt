package com.example.tea.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tea.models.article.Article
import com.example.tea.models.article.ArticleDomain
import com.example.tea.models.user.User

class DatabaseHelper(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, 4) {

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = ("CREATE TABLE IF NOT EXISTS profile(id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT);")
        val query2 = ("CREATE TABLE IF NOT EXISTS article(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, photo TEXT);")
        val query3 = ("CREATE TABLE IF NOT EXISTS guest(id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT);")

        p0?.execSQL(query)
        p0?.execSQL(query2)
        p0?.execSQL(query3)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val query = ("CREATE TABLE IF NOT EXISTS profile(id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT);")
        val query2 = ("CREATE TABLE IF NOT EXISTS article(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, photo TEXT);")
        val query3 = ("CREATE TABLE IF NOT EXISTS guest(id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT);")

        p0?.execSQL(query)
        p0?.execSQL(query2)
        p0?.execSQL(query3)
    }

    fun addProfile(login : String, password : String ){

        val values = ContentValues()

        values.put("login", login)
        values.put("password", password)

        val db = this.writableDatabase

        // all values are inserted into database
        db.insert("profile", null, values)

        db.close()
    }

    fun addGuest(login : String, password : String ){

        val values = ContentValues()

        values.put("login", login)
        values.put("password", password)

        val db = this.writableDatabase

        // all values are inserted into database
        db.insert("guest", null, values)

        db.close()
    }

    fun addArticle(article : ArticleDomain){

        val values = ContentValues()

        values.put("title", article.title)
        values.put("description", article.description)
        values.put("photo", article.photo)

        val db = this.writableDatabase

        // all values are inserted into database
        db.insert("article", null, values)

        db.close()
    }

    @SuppressLint("Recycle")
    fun getArticles(): ArrayList<Article> {

        val articles = ArrayList<Article>()

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM article", null)

        while(cursor.moveToNext()){

            var article = Article()

            article.id = cursor.getInt(0)
            article.title = cursor.getString(1)
            article.description = cursor.getString(2)
            article.photo = cursor.getString(3)
            articles.add(article)
        }

        cursor.close()

        return articles
    }

    @SuppressLint("Recycle")
    fun getArticle(id: String): ArticleDomain {

        val article = ArticleDomain()

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM article WHERE id = $id", null)

        if(cursor.moveToFirst()){
            article.id = cursor.getInt(0)
            article.title = cursor.getString(1)
            article.description = cursor.getString(2)
            article.photo = cursor.getString(3)
        }

        cursor.close()

        return article
    }

    @SuppressLint("Recycle")
    fun getProfile(): User {

        val user = User()

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM profile ORDER BY id DESC LIMIT 1", null)

        if(cursor.moveToFirst()){
            user.login = cursor.getString(1)
            user.password = cursor.getString(2)
        }

        cursor.close()

        return user
    }

    @SuppressLint("Recycle")
    fun getGuest(): User {

        val user = User()

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM guest ORDER BY id DESC LIMIT 1", null)

        if(cursor.moveToFirst()){
            user.id = cursor.getInt(0)
            user.login = cursor.getString(1)
            user.password = cursor.getString(2)
        }

        cursor.close()

        return user
    }

    fun deleteProfile() {
        val db = this.writableDatabase

        db.execSQL("DELETE from profile")
    }

    fun deleteGuest() {
        val db = this.writableDatabase

        db.execSQL("DELETE from guest")
    }


    fun deleteDraft(id : Int) {
        val db = this.writableDatabase

        db.execSQL("DELETE from article where id = $id")
    }

    companion object{
        private val DATABASE_NAME = "TEA_DB"

    }
}