package com.test.weatherapi.data.entities

import com.google.gson.annotations.SerializedName

data class DataModel(
    @SerializedName("current")
    var current: Current? = null
)

data class Current(
//    @SerializedName("last_updated")
//    var last_updated: Int? = null,
    @SerializedName("temp_c")
    var temp_c: Int? = null
)
