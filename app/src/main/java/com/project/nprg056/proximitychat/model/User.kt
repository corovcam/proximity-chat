package com.project.nprg056.proximitychat.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user-name") val userName: String = "",
    val roomId: String = ""
){
    init {
        require(userName != null) {
            "UserName and location cannot be null"
        }
    }
}