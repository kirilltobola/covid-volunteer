package com.isu.covidvolunteer.models.role

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoleDto(@JsonProperty("name") val name: String) : Parcelable {
    override fun toString(): String {
        return when (name) {
            "ROLE_USER" -> "Пользователь"
            "ROLE_MEDIC" -> "Медицинский работник"
            else -> "Неизвестная роль"
        }
    }
}