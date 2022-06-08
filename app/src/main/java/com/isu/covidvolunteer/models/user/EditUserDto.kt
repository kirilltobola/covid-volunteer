package com.isu.covidvolunteer.models.user

import com.fasterxml.jackson.annotation.JsonProperty

data class EditUserDto(
    @JsonProperty("username") val username: String,
    @JsonProperty("first name") val firstName: String,
    @JsonProperty("last name") val lastName: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("phone") val phone: String
)