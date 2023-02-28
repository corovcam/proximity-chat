package com.project.nprg056.proximitychat.model

data class LocationDetail(
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
) {
    init {
        require(latitude != null && longitude != null) {
            "Latitude and Longitude cannot be null"
        }
    }
}
