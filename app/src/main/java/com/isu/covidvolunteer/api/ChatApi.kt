package com.isu.covidvolunteer.api

import com.isu.covidvolunteer.models.chat.AddChatDto
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.models.message.AddMessageDto
import com.isu.covidvolunteer.models.message.MessageDto
import retrofit2.http.*

interface ChatApi {
    @GET("chats/")
    suspend fun getAllChats(
        @Header("Authorization") token: String
    ): List<ChatDto>

    @GET("chats/{id}/")
    suspend fun getChat(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): List<MessageDto>

    @POST("chats/")
    suspend fun startChat(
        @Header("Authorization") token: String,
        @Body addChatDto : AddChatDto
    )

    @POST("chats/{id}/")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body messageDto: AddMessageDto
    )

    @DELETE("chats/{id}/")
    suspend fun deleteChat(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )
}