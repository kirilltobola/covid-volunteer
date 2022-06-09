package com.isu.covidvolunteer.ui.auth

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.repository.AuthRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.util.UserDetails
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var authViewModel: AuthViewModel

    // Views
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        usernameField = view.findViewById(R.id.usernameEditText)
        passwordField = view.findViewById(R.id.passwordEditText)

        loginButton = view.findViewById(R.id.loginButton)
        val blankMsg = "Обязательное поле"
        loginButton.setOnClickListener {
            loginButton.isEnabled = false
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            if (username.isNullOrBlank()) {
                usernameField.error = blankMsg
                loginButton.isEnabled = true
            } else if (password.isNullOrBlank()) {
                passwordField.error = blankMsg
                loginButton.isEnabled = true
            } else if (password.length < 8) {
                passwordField.error = "Пароль должен быть больше 8 символов"
                loginButton.isEnabled = true
            } else {
                authViewModel.login(AuthDto(username, password))
            }
        }

        registerButton = view.findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(AuthRepository())
        )[AuthViewModel::class.java]

        authViewModel.response.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomResponse.Success -> {
                    authenticateUser(it.body)
                    view.postDelayed({
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }, 2000)
                }
                is CustomResponse.NetworkError -> {
                    loginButton.isEnabled = true
                    val msg = "Отсутствует интернет соединение"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                is CustomResponse.ApiError -> {
                    loginButton.isEnabled = true
                    highlightTextFields(it.body.details)
                    val msg = it.body.message
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                is CustomResponse.UnexpectedError -> {
                    loginButton.isEnabled = true
                    val msg = "Неизвестная ошибка"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun highlightTextFields(details: List<String>) {
        usernameField.error = details.toString()
        passwordField.error = details.toString()
    }

    private fun authenticateUser(response: AuthResponse) {
        val sharedPrefs = requireActivity().getSharedPreferences(
            "VOLUNTEER_API",
            MODE_PRIVATE
        )
        val editor = sharedPrefs.edit()
        editor.putString("UserDetails", Gson().toJson(response))
        editor.apply()

        UserDetails.authenticate(response)
    }
}