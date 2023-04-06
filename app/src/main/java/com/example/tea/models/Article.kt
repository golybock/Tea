package com.example.tea.models

import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class Article(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Description")
    val description : String,
    @SerializedName("FirstName")
    val firstName: String = "firstName",
    @SerializedName("LastName")
    val lastName: String = "lastName",
    @SerializedName("MiddleName")
    val middleName: String = "middleName",
    @SerializedName("Login")
    val login: String = "login",
    @SerializedName("Email")
    val email: String = "email",
    @SerializedName("DateOfPublication")
    val dateOfPublication: String = SimpleDateFormat("yyyy-mm-dd").format(Calendar.getInstance().time),
    @SerializedName("Photo")
    val photo: String = "photo",
    @SerializedName("Gender")
    val gender: String = "gender"
)