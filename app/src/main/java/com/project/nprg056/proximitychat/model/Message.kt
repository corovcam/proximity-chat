package com.project.nprg056.proximitychat.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val message: String? = null,
    val userId: String? = null,
    val timestamp: String? = null
)
