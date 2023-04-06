package com.example.tea.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tea.models.User

class DatabaseHelper(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, 1) {

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = ("CREATE TABLE IF NOT EXISTS profile(id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT);" +
                "CREATE TABLE IF NOT EXISTS article(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, photo TEXT)")

        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

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

    fun deleteProfile() {
        val db = this.writableDatabase

        db.execSQL("DELETE from profile")
    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "TEA"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val PROFILE_TABLE = "profile"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val LOGIN_COL = "login"
        val PASSWORD_COL = "password"

        val TITLE_COL = "title"
        val DESCRIPTION_COL = "description"
        val PHOTO_COL = "photo"

    }
}