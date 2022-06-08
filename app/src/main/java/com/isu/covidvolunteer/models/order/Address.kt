package com.isu.covidvolunteer.models.order

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    @JsonProperty("from") val from: String,
    @JsonProperty("to") val to: String
) : Parcelable {
    override fun toString(): String {
        return "$from -> $to"
    }
}