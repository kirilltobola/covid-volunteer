package com.isu.covidvolunteer.models.auth

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.isu.covidvolunteer.models.role.RoleDto

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("roles") val roles: List<RoleDto>,
    @JsonProperty("token") val token: TokenDto,
)
