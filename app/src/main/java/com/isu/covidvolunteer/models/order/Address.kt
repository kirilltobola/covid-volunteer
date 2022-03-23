package com.isu.covidvolunteer.models.order

import com.fasterxml.jackson.annotation.JsonProperty

data class Address(@JsonProperty("from") val from: String, @JsonProperty("to") val to: String)