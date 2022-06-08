package com.isu.covidvolunteer.models.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterUserDto (
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("phone") val phone: String,
    @JsonProperty("firstName") val firstName: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("isMedic") val isMedic: Boolean
)