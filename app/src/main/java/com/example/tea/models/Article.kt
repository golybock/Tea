package com.example.tea.models

import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import java.util.*

class Article(
    val id: Int,
    val title: String,
    val description : String,
    val firstName: String = "firstName",
    val lastName: String = "lastName",
    val middleName: String = "middleName",
    val login: String = "login",
    val email: String = "email",
    val dateOfPublication: Date = Date(2001, 12, 1),
    val photo: String = "photo",
    val gender: String = "gender"
)