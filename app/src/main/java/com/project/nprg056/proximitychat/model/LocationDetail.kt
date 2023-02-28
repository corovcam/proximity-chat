package com.project.nprg056.proximitychat.model

import com.google.gson.annotations.SerializedName

data class LocationDetail(
    @SerializedName("lat") val latitude: Double? = 0.0,
    @SerializedName("lon") val longitude: Double? = 0.0,
) {
    init {
        require(latitude != null && longitude != null) {
            "Latitude and Longitude cannot be null"
        }
    }
}
