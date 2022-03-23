package com.isu.covidvolunteer.models.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.isu.covidvolunteer.models.user.UserDto


data class MessageDto(
    @JsonProperty("body") val body: String,
    @JsonProperty("sender") val sender: UserDto
)