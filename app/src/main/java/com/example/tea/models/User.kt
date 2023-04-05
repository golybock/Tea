package com.example.tea.models

import android.provider.ContactsContract.CommonDataKinds.Email
import java.util.Date

class User(
    val id : Int,
    val firstName: String,
    val lastName: String,
    val middleName : String,
    val login : String,
    val email: Email,
    val dateOfBirth : Date,
    val photo : String,
    val role : String,
    val gender : String
)