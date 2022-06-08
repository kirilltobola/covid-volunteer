package com.isu.covidvolunteer.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.models.auth.RegisterUserDto
import com.isu.covidvolunteer.repository.AuthRepository
import com.isu.covidvolunteer.retrofit.ApiError
import com.isu.covidvolunteer.retrofit.CustomResponse
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    val response: MutableLiveData<CustomResponse<AuthResponse, ApiError>> = MutableLiveData()

    fun register(registerDto: RegisterUserDto) {
        viewModelScope.launch {
            response.value = repository.register(registerDto)
        }
    }

    fun login(authDto: AuthDto) {
        viewModelScope.launch {
            response.value = repository.login(authDto)
        }
    }
}