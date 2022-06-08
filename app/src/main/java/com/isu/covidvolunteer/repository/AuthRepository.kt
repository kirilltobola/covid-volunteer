package com.isu.covidvolunteer.repository

import com.isu.covidvolunteer.api.AuthApi
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.models.auth.RegisterUserDto
import com.isu.covidvolunteer.retrofit.ApiError
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.util.Retrofit

class AuthRepository : AuthApi {

    override suspend fun register(registerDto: RegisterUserDto): CustomResponse<AuthResponse, ApiError> {
        return Retrofit.authApi.register(registerDto)
    }

    override suspend fun login(authDto: AuthDto): CustomResponse<AuthResponse, ApiError> {
        return Retrofit.authApi.login(authDto)
    }

    // TODO: implement this
    override suspend fun logout() {
        return Retrofit.authApi.logout()
    }
}