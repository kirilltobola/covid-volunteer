package com.isu.covidvolunteer.models.role

import com.fasterxml.jackson.annotation.JsonProperty

data class RoleDto(@JsonProperty("name") val name: String) {
    override fun toString(): String {
        return if (name == "ROLE_USER") {
            "Пользователь"
        }else {
            "Медицинский работник"
        }
    }
}