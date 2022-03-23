package com.isu.covidvolunteer.api

import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.models.user.ChangePasswordDto
import com.isu.covidvolunteer.models.user.UserDto
import retrofit2.http.*

interface UserApi {
    @GET("users/{id}/")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): UserDto

    @GET("users/{id}/orders/")
    suspend fun getUserOrders(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): List<OrderDto>

    @PATCH("users/{id}")
    suspend fun editUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body user: UserDto
    )

    @PATCH("users/{id}/change-password/")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body password: ChangePasswordDto
    )

    @DELETE("users/{id}/")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )

    // TODO: fun verify()
}