package com.project.nprg056.proximitychat.model

data class User(
    val userName: String = "",
    val roomId: String = ""
){
    init {
        require(userName != null) {
            "UserName and location cannot be null"
        }
    }
}