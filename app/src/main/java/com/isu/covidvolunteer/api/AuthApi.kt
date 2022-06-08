package com.isu.covidvolunteer.api

//import com.isu.covidvolunteer.api.AuthResponse
import com.isu.covidvolunteer.models.auth.AuthDto
import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.models.auth.RegisterUserDto
import com.isu.covidvolunteer.repository.AuthRepository
import com.isu.covidvolunteer.retrofit.ApiError
import com.isu.covidvolunteer.retrofit.CustomResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST(value = "auth/register/")
    suspend fun register(@Body registerDto: RegisterUserDto): GenericAuthResponse<AuthResponse>
    //CustomResponse<AuthResponse, AuthRepository.ApiError>
    //suspend fun register(@Body registerDto: RegisterUserDto): Response<AuthResponse>

    @POST(value = "auth/login/")
    suspend fun login(@Body authDto: AuthDto): GenericAuthResponse<AuthResponse>
    //CustomResponse<AuthResponse, AuthRepository.ApiError>
    //suspend fun login(@Body authDto: AuthDto): Response<AuthResponse>

    // TODO: not implemented on server side
    suspend fun logout()
}

typealias GenericAuthResponse<S> = CustomResponse<S, ApiError>
