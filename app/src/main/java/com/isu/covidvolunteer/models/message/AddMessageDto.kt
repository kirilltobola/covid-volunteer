package com.isu.covidvolunteer.models.message

import com.fasterxml.jackson.annotation.JsonProperty

data class AddMessageDto(@JsonProperty("body") val body: String)