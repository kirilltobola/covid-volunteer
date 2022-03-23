package com.isu.covidvolunteer.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    val response: MutableLiveData<AuthResponse> = MutableLiveData()

    fun register(authDto: AuthDto) {
        viewModelScope.launch {
            response.value = repository.register(authDto)
        }
    }

    fun login(authDto: AuthDto) {
        viewModelScope.launch {
            response.value = repository.login(authDto)
        }
    }
}