package com.isu.covidvolunteer.models.order

data class AddOrderDto(
    val headline: String,
    val address: Address?,
    val date: String?,
    val comment: String?,
)