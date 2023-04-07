package com.example.tea.ui.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.tea.EditProfileActivity
import com.example.tea.R
import com.example.tea.api.Api
import com.example.tea.databinding.FragmentProfileBinding
import com.example.tea.dialogs.LogoutFragment
import com.example.tea.models.user.User

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = getUser()

        val firstName: TextView = binding.profileFirstName
        val lastName : TextView = binding.profileLastName
        val middleName : TextView = binding.profileMiddleName
        val dateOfBirth : TextView = binding.profileBirthDate
        val login : TextView = binding.profileLogin
        val email : TextView = binding.profileEmail
        val image : ImageView = binding.profileImage

        val editButton: LinearLayout = binding.editProfileButton
        val exitButton : LinearLayout = binding.logoutProfileButton
        val pdfButton : LinearLayout = binding.printProfileButton

        val maleButton : Button = binding.profileManGender
        val femaleButton : Button = binding.profileFemaleGender

        if (user != null) {
            firstName.text = "Имя: " + user.firstName.toString()
            lastName.text = "Фамилия: " + user.lastName.toString()
            middleName.text = "Отчество: " + user.middleName.toString()
            dateOfBirth.text = "День рождения: " + user.dateOfBirth.toString()
            login.text = "Логин: " + user.login.toString()
            email.text = "Почта: " + user.email.toString()

            if(user.photo.length > 100){
                image.setImageBitmap(convert(user.photo))
            }

            if(user.gender == "Male"){
                maleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.crimson)));
                femaleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
            }
            if(user.gender == "Female"){
                maleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
                femaleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.crimson)));
            }
        }

        editButton.setOnClickListener {

            if (user != null) {
                val intent = Intent(activity, EditProfileActivity::class.java)
                intent.putExtra("id", user.id)
                startActivity(intent)
            }

        }

        exitButton.setOnClickListener{

            val myDialogFragment = LogoutFragment()
            val manager = activity?.supportFragmentManager
            val transaction: FragmentTransaction = manager!!.beginTransaction()
            myDialogFragment.show(transaction, "dialog")

        }

        return root
    }

    override fun onResume() {
        super.onResume()

        val user = getUser()

        val firstName: TextView = binding.profileFirstName
        val lastName : TextView = binding.profileLastName
        val middleName : TextView = binding.profileMiddleName
        val dateOfBirth : TextView = binding.profileBirthDate
        val login : TextView = binding.profileLogin
        val email : TextView = binding.profileEmail
        val image : ImageView = binding.profileImage

        val editButton: LinearLayout = binding.editProfileButton
        val exitButton : LinearLayout = binding.logoutProfileButton
        val pdfButton : LinearLayout = binding.printProfileButton

        val maleButton : Button = binding.profileManGender
        val femaleButton : Button = binding.profileFemaleGender

        if (user != null) {
            firstName.text = "Имя: " + user.firstName.toString()
            lastName.text = "Фамилия: " + user.lastName.toString()
            middleName.text = "Отчество: " + user.middleName.toString()
            dateOfBirth.text = "День рождения: " + user.dateOfBirth.toString()
            login.text = "Логин: " + user.login.toString()
            email.text = "Почта: " + user.email.toString()

            if(user.photo.length > 100){
                image.setImageBitmap(convert(user.photo))
            }

            if(user.gender == "Male"){
                maleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.crimson)));
                femaleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
            }
            if(user.gender == "Female"){
                maleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
                femaleButton.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.crimson)));
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUser() : User? {
        val api = Api(activity)
        val user = api.getUser()
        return user
    }

    @Throws(IllegalArgumentException::class)
    fun convert(base64Str: String): Bitmap? {
        val decodedBytes: ByteArray = android.util.Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            android.util.Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

}

