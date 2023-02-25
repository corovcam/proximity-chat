package com.project.nprg056.proximitychat.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user-name") val userName: String = "",
    @SerializedName("user-location") val location: LocationDetail?,
)