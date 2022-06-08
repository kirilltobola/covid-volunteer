package com.isu.covidvolunteer.repository

import com.isu.covidvolunteer.api.GenericUserResponse
import com.isu.covidvolunteer.api.UserApi
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.models.user.ChangePasswordDto
import com.isu.covidvolunteer.models.user.UserDto
import com.isu.covidvolunteer.util.Retrofit

class UserRepository : UserApi {
    override suspend fun getUser(
        token: String,
        id: Long
    ): GenericUserResponse<UserDto> {
        return Retrofit.userApi.getUser(token, id)
    }

    override suspend fun getUserOrders(
        token: String,
        id: Long
    ): GenericUserResponse<List<OrderDto>> {
        return Retrofit.userApi.getUserOrders(token, id)
    }

    override suspend fun editUser(
        token: String,
        id: Int,
        user: UserDto
    ) {
        return Retrofit.userApi.editUser(token, id, user)
    }

    override suspend fun changePassword(
        token: String,
        id: Int,
        password: ChangePasswordDto
    ) {
        return Retrofit.userApi.changePassword(token, id, password)
    }

    override suspend fun deleteUser(
        token: String,
        id: Int
    ) {
        return Retrofit.userApi.deleteUser(token, id)
    }
}