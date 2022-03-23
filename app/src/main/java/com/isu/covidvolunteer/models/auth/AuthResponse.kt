package com.isu.covidvolunteer.models.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.isu.covidvolunteer.models.role.RoleDto

data class AuthResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("roles") val roles: List<RoleDto>,
    @JsonProperty("token") val token: TokenDto
)
