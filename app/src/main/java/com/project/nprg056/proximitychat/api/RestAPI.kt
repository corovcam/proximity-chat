package com.project.nprg056.proximitychat.api

import com.project.nprg056.proximitychat.model.RoomResponse
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

    @Headers("Accept: application/json")
    @GET("get-chat-room/{user-id}")
    suspend fun getChatRoom(@Path("user-id") userId: String): RoomResponse?
}