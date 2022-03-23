package com.isu.covidvolunteer.models.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenDto(
    @JsonProperty("token") val token: String,
    @JsonProperty("expiresIn") val expiresIn: String
)