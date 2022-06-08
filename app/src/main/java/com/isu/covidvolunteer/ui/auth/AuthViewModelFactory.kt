package com.isu.covidvolunteer.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isu.covidvolunteer.repository.AuthRepository

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}