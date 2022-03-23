package com.isu.covidvolunteer.models.chat

import com.fasterxml.jackson.annotation.JsonProperty
import com.isu.covidvolunteer.models.user.UserDto

data class ChatDto(@JsonProperty("id") val id: Long, @JsonProperty("user") val user: UserDto)