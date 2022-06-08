package com.isu.covidvolunteer.util

import com.isu.covidvolunteer.models.auth.AuthResponse
import com.isu.covidvolunteer.models.auth.TokenDto
import com.isu.covidvolunteer.models.role.RoleDto
import java.util.*

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
        if (token != null) {
            if (token!!.expiresIn > Date()) return true
        }
        return false
    }

    fun hasRole(role: RoleDto): Boolean = roles?.contains(role)!!
}