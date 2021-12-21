package com.test.weatherapi.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataModel(
    @SerialName("current")
    val current: Current? = null
)

@Serializable
data class Current(
//    @SerialName("last_updated")
//    val last_updated: Int? = null,
    @SerialName("temp_c")
    val temp_c: Double? = null
)
