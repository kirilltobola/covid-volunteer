package com.isu.covidvolunteer.models.order

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import com.isu.covidvolunteer.models.user.UserDto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDto(
    @JsonProperty("id") val id: Long,
    @JsonProperty("address") var address: Address?,
    @JsonProperty("headline") var headline: String,
    @JsonProperty("comment") var comment: String?,
    @JsonProperty("date") var date: String?,
    @JsonProperty("status") val status: Status,
    @JsonProperty("createdAt") val createdAt: String?,
    @JsonProperty("modifiedAt") val modifiedAt: String?,
    @JsonProperty("performer") val performer: UserDto?,
    @JsonProperty("owner") val owner: UserDto,
) : Parcelable {
    fun hasPerformer(): Boolean {
        return when (performer) {
            null -> false
            else -> true
        }
    }
}