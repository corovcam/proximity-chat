package com.project.nprg056.proximitychat.api

import com.project.nprg056.proximitychat.model.RoomResponse
import com.project.nprg056.proximitychat.model.User
import com.project.nprg056.proximitychat.model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface RestAPI {
    /**
     * Register User on Backend server using POST method with user parameters in request body
     * @param user { user-name: "Alice", user-location: { lat: "45.25", lon: "12.5" } }
     * @return { user-id: "m52d5a6i835fsd" }
     */
    @Headers("Accept: application/json")
    @POST("register-user")
    suspend fun registerUser(@Body user: User): UserResponse?

    /**
     * Delete User on Backend server using DELETE method
     * @param userId { user-id: "m52d5a6i835fsd" }
     * @return HTTP Response code 200 if successful otherwise 404 (Not Found)
     */
    @Headers("Accept: application/json")
    @DELETE("delete-user/{user-id}")
    fun deleteUser(@Path("user-id") userId: String): Call<Void>

    /**
     * Get Chat Room for "userId" from Backend server using GET method
     * @param userId { user-id: "m52d5a6i835fsd" }
     * @return { room-id: "5a5sdas8623gfs6" }
     */
    @Headers("Accept: application/json")
    @GET("get-chat-room/{user-id}")
    suspend fun getChatRoom(@Path("user-id") userId: String): RoomResponse?

    /**
     * Disconnect current user with userId from the Chat Room session
     * @param userId { user-id: "m52d5a6i835fsd" }
     * @return HTTP Response code 200 if successful or different Response code otherwise
     */
    @Headers("Accept: application/json")
    @GET("disconnect/{user-id}")
    fun disconnect(@Path("user-id") userId: String): Call<Void>
}