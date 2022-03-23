package com.isu.covidvolunteer.models.order

import com.fasterxml.jackson.annotation.JsonProperty
import com.isu.covidvolunteer.models.user.UserDto

data class OrderDto(
    @JsonProperty("id") val id: Long,
    @JsonProperty("address") val address: Address?,
    @JsonProperty("headline") val headline: String,
    @JsonProperty("comment") val comment: String?,
    @JsonProperty("date") val date: String?,
    @JsonProperty("status") val status: Status?,
    @JsonProperty("createdAt") val createdAt: String?,
    @JsonProperty("modifiedAt") val modifiedAt: String?,
    @JsonProperty("performer") val performer: UserDto?,
    @JsonProperty("owner") val owner: UserDto,
)