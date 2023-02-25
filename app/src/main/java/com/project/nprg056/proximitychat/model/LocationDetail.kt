package com.project.nprg056.proximitychat.model

import com.google.gson.annotations.SerializedName

data class LocationDetail(
    @SerializedName("lat") val latitude: String?,
    @SerializedName("lon") val longitude: String?,
) {
    init {
        require(latitude != null && longitude != null) {
            "Latitude and Longitude cannot be null"
        }
    }
}
