package com.isu.covidvolunteer.api

import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.models.auth.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST(value = "auth/register/")
    suspend fun register(@Body authDto: AuthDto): AuthResponse

    @POST(value = "auth/login/")
    suspend fun login(@Body authDto: AuthDto): AuthResponse

    // TODO: what http method? + implement
    suspend fun logout()
}
