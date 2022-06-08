package com.isu.covidvolunteer.models.user

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import com.isu.covidvolunteer.models.role.RoleDto
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserDto(
    @JsonProperty("id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("first name") val firstName: String?,
    @JsonProperty("last name") val lastName: String?,
    @JsonProperty("email") val email: String?,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("createdAt") val createdAt: String?, // TODO: delete it, because out of date???
    @JsonProperty("roles") val roles: List<RoleDto>,
) : Parcelable {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}