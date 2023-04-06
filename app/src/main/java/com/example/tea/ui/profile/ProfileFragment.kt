package com.example.tea.ui.profile

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.tea.EditProfileActivity
import com.example.tea.R
import com.example.tea.api.Api
import com.example.tea.auth.LoginActivity
import com.example.tea.databinding.FragmentProfileBinding
import com.example.tea.dialogs.LogoutFragment
import com.example.tea.models.User
import org.w3c.dom.Text
import kotlin.math.log

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

        val editButton: LinearLayout = binding.editProfileButton
        val exitButton : LinearLayout = binding.logoutProfileButton
        val pdfButton : LinearLayout = binding.printProfileButton

        val maleButton : Button = binding.profileManGender
        val femaleButton : Button = binding.profileFemaleGender

        if (user != null) {
            firstName.text = user.firstName
            lastName.text = user.lastName
            middleName.text = user.middleName
            dateOfBirth.text = user.dateOfBirth
            login.text = user.login
            email.text = user.email

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
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        exitButton.setOnClickListener{

            val myDialogFragment = LogoutFragment()
            val manager = activity?.supportFragmentManager
            val transaction: FragmentTransaction = manager!!.beginTransaction()
            myDialogFragment.show(transaction, "dialog")

        }

        return root
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
}