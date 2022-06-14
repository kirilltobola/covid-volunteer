package com.isu.covidvolunteer.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.auth.RegisterUserDto
import com.isu.covidvolunteer.repository.AuthRepository
import com.isu.covidvolunteer.retrofit.CustomResponse

class RegisterFragment : Fragment(R.layout.register) {
    private lateinit var authViewModel: AuthViewModel

    // Views
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var phoneField: EditText
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var isMedicSwitch: SwitchCompat
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register, container, false)

        usernameField = view.findViewById(R.id.registerUsername)
        passwordField = view.findViewById(R.id.registerPassword)
        phoneField = view.findViewById(R.id.registerPhone)
        firstNameField = view.findViewById(R.id.registerFirstName)
        lastNameField = view.findViewById(R.id.registerLastName)
        isMedicSwitch = view.findViewById(R.id.isMedicSwitch)
        submitButton = view.findViewById(R.id.registerSubmitButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(AuthRepository())
        )[AuthViewModel::class.java]

        submitButton .setOnClickListener {
            val fieldsToValidate = listOf(
                usernameField, passwordField, phoneField, firstNameField, lastNameField
            )
            val valid = validateFields(fieldsToValidate)

            if (valid) {
                submitButton.isEnabled = false
                val userData = RegisterUserDto(
                    usernameField.text.toString().trim(),
                    passwordField.text.toString().trim(),
                    phoneField.text.toString().trim(),
                    firstNameField.text.toString().trim(),
                    lastNameField.text.toString().trim(),
                    isMedicSwitch.isChecked
                )
                authViewModel.register(userData)

                authViewModel.response.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is CustomResponse.Success -> {
                            Log.d("D_REGISTER", it.body.roles.toString())
                            Toast.makeText(
                                context,
                                "Вы успешно зарегистрировались",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(
                                R.id.action_registerFragment_to_loginFragment
                            )
                        }
                        is CustomResponse.ApiError -> {
                            submitButton.isEnabled = true
                            val msg = it.body.message
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                        is CustomResponse.NetworkError -> {
                            submitButton.isEnabled = true
                            val msg = "Отсутствует интернет соединение"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                        is CustomResponse.UnexpectedError -> {
                            submitButton.isEnabled = true
                            val msg = "Неизвестная ошибка"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    private fun validateFields(fields: List<EditText>): Boolean {
        var valid = true
        for (field in fields) {
            if (field.text.isNullOrBlank()) {
                field.error = "Обязательное поле"
                valid = false
            }
            if (field == passwordField && field.text.toString().length < 8) {
                field.error = "Пароль должен сосотоять минимум из 8 символов"
                valid = false
            }
        }
        return valid
    }
}