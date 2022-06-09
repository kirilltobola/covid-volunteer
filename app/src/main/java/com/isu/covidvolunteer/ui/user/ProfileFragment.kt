package com.isu.covidvolunteer.ui.user

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.user.UserDto
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.util.UserDetails

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    lateinit var userViewModel: UserViewModel

    // Views
    private lateinit var firstNameField: TextView
    private lateinit var lastNameField: TextView
    private lateinit var rolesField: TextView
    private lateinit var logoutButton: ImageButton
    private lateinit var phoneNumber: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository())
        )[UserViewModel::class.java]

        firstNameField = view.findViewById(R.id.firstNameTextView)
        lastNameField = view.findViewById(R.id.lastNameTextView)
        rolesField = view.findViewById(R.id.roleTextView)

        // TODO: проверка на возможность повзонить
        phoneNumber = view.findViewById(R.id.phoneTextView)
        if (phoneNumber.text.isNotBlank()) {
            phoneNumber.setTextColor(Color.BLUE)
            phoneNumber.setOnClickListener {
                val phoneIntent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:${phoneNumber.text}")
                )
                if (phoneIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(phoneIntent)
                }
            }
        }

        logoutButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val sharedPrefs = requireActivity().getSharedPreferences(
                "VOLUNTEER_API", MODE_PRIVATE
            )
            val editor = sharedPrefs.edit()
            editor.remove("UserDetails")
            editor.apply()
            requireActivity().finish()
        }

        val user = arguments?.get("user") as UserDto?
        if (user == null) {
            userViewModel.getUser(UserDetails.id!!)
            userViewModel.user.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is CustomResponse.Success -> {
                        firstNameField.text = "${it.body.firstName}"
                        lastNameField.text = "${it.body.lastName}"
                        phoneNumber.text = "Телефон: ${it.body.phone}"
                        val role = it.body.roles[it.body.roles.size - 1]
                        rolesField.text = "Роль: $role"
                    }
                    is CustomResponse.NetworkError -> {
                        val msg = "Отсутсвтует интернет соединение"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                    is CustomResponse.ApiError -> {
                        val msg = it.body.message
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                    is CustomResponse.UnexpectedError -> {
                        val msg = "Неизвестная ошибка"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            firstNameField.text = "${user?.firstName}"
            lastNameField.text = "${user?.lastName}"
            phoneNumber.text = "Телефон: ${user?.phone}"
            rolesField.text = "Роль: ${user?.roles[user?.roles.size - 1]}"

            logoutButton.visibility = View.GONE
        }
    }
}