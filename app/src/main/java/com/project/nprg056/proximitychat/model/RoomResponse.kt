package com.project.nprg056.proximitychat.model

import com.google.gson.annotations.SerializedName

data class RoomResponse(
    @SerializedName("room-id") val roomId: String?,
)
