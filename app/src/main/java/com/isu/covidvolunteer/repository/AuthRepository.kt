package com.isu.covidvolunteer.repository

import com.isu.covidvolunteer.api.AuthApi
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.util.Retrofit

class AuthRepository : AuthApi {

    override suspend fun register(authDto: AuthDto): AuthResponse {
        return Retrofit.authApi.register(authDto)
    }

    override suspend fun login(authDto: AuthDto): AuthResponse {
        return Retrofit.authApi.login(authDto)
    }

    // TODO: implement this
    override suspend fun logout() {
        return Retrofit.authApi.logout()
    }
}