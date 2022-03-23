package com.isu.covidvolunteer.ui.user

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.util.UserDetails

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    lateinit var userViewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository())
        )[UserViewModel::class.java]

        // TODO: get from userDetails
        userViewModel.getUser(UserDetails.id!!)
        userViewModel.user.observe(viewLifecycleOwner, Observer {
            view.findViewById<TextView>(R.id.firstNameTextView).text = "Имя: ${it.firstName}"
            view.findViewById<TextView>(R.id.lastNameTextView).text = "Фамилия: ${it.lastName}"
            view.findViewById<TextView>(R.id.phoneTextView).text = "Телефон: ${it.phone}"
            view.findViewById<TextView>(R.id.roleTextView).text = "Роль: ${it.roles[0].toString()}"
        })
    }
}