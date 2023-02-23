package com.project.nprg056.proximitychat.model

data class User(
    val userId: String = "",
    val userName: String = "",
    val location: LocationDetail,
)