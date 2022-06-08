package com.isu.covidvolunteer.retrofit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class InfoResonse(
    @JsonProperty("message") val message: String,
    @JsonProperty("details") val details: List<String> = emptyList()
) {
    override fun toString(): String {
        return "message: $message, details: $details"
    }
}