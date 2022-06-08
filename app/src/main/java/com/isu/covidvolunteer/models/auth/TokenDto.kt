package com.isu.covidvolunteer.models.auth

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class TokenDto(
    @JsonProperty("token") val token: String,
    @JsonProperty("expiresIn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val expiresIn: Date
)