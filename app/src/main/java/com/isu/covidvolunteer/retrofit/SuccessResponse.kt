package com.isu.covidvolunteer.retrofit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SuccessResponse(
    @JsonProperty("status") val message: String,
)