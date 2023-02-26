package com.project.nprg056.proximitychat.api

import com.project.nprg056.proximitychat.model.User
import com.project.nprg056.proximitychat.model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface RestAPI {
    @Headers("Accept: application/json")
    @POST("register-user")
    suspend fun registerUser(@Body user: User): UserResponse?

    @Headers("Accept: application/json")
    @DELETE("delete-user/{user-id}")
    fun deleteUser(@Path("user-id") userId: String): Call<Void>
}