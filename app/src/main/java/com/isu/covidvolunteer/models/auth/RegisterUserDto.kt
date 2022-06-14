package com.isu.covidvolunteer.models.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterUserDto (
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("phone") val phone: String,
    @JsonProperty("firstName") val firstName: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("medic") val isMedic: Boolean
) {
    override fun toString(): String {
        return "$username $isMedic $firstName $lastName"
    }
}