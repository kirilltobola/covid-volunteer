package com.isu.covidvolunteer.retrofit

import java.io.IOException

sealed class CustomResponse<out T: Any, out T1: Any> {
    data class Success<T: Any>(val body: T) : CustomResponse<T, Nothing>()

    data class ApiError<T1: Any>(val body: T1, val code: Int) : CustomResponse<Nothing, T1>()

    data class NetworkError(val error: IOException) : CustomResponse<Nothing, Nothing>()

    data class UnexpectedError(val error: Throwable?) : CustomResponse<Nothing, Nothing>()
}