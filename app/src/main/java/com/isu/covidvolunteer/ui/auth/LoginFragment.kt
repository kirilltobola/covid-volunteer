package com.isu.covidvolunteer.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.repository.AuthRepository
import com.isu.covidvolunteer.util.UserDetails

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var authViewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(AuthRepository())
        )[AuthViewModel::class.java]

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            val username = view.findViewById<EditText>(R.id.usernameEditText).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordEditText).text.toString()

            authViewModel.login(AuthDto(username, password))
            authViewModel.response.observe(viewLifecycleOwner, Observer {
                Log.d("AUTH_RESPONSE", it.toString())
                UserDetails.authenticate(it)
            })

            view.postDelayed({
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }, 3000)
        }
    }
}