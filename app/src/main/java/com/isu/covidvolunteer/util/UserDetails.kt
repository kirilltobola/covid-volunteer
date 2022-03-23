package com.isu.covidvolunteer.util

import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.models.auth.TokenDto
import com.isu.covidvolunteer.models.role.RoleDto

object UserDetails {
    var id: Long? = null
    var token: TokenDto? = null
    var roles: List<RoleDto>? = null

    fun authenticate(authResponse: AuthResponse) {
        id = authResponse.id
        token = authResponse.token
        roles = authResponse.roles
    }

    fun isTokenValid(): Boolean {
        // TODO: check expiration date
        return token?.token != null
    }

    fun hasRole(role: RoleDto): Boolean = roles?.contains(role)!!
}