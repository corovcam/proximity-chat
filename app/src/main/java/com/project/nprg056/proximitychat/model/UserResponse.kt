package com.project.nprg056.proximitychat.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("user-id") val userId: String?,
)