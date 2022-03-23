package com.isu.covidvolunteer.models.user

import com.fasterxml.jackson.annotation.JsonProperty

data class EditUserDto(
    val username: String,
    @JsonProperty("first name") val firstName: String,
    @JsonProperty("last name") val lastName: String,
    val email: String,
    val phone: String
)