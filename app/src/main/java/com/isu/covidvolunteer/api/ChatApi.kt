package com.isu.covidvolunteer.api

import com.isu.covidvolunteer.models.chat.AddChatDto
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.models.message.AddMessageDto
import com.isu.covidvolunteer.models.message.MessageDto
import com.isu.covidvolunteer.retrofit.ApiError
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.retrofit.SuccessResponse
import retrofit2.http.*

interface ChatApi {
    @GET("chats/")
    suspend fun getAllChats(
        @Header("Authorization") token: String
    ): GenericChatResponse<List<ChatDto>>

    @GET("chats/{id}/messages/")
    suspend fun getChatMessages(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ):  GenericChatResponse<List<MessageDto>>

    @GET("chats/{id}/")
    suspend fun getChat(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): GenericChatResponse<ChatDto>

    @POST("chats/")
    suspend fun startChat(
        @Header("Authorization") token: String,
        @Body addChatDto : AddChatDto
    ): GenericChatResponse<ChatDto>

    @POST("chats/{id}/")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body messageDto: AddMessageDto
    ): GenericChatResponse<SuccessResponse>

    @GET("chats/{id}/count-messages/")
    suspend fun countMessages(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): GenericChatResponse<Long>//Long

    @DELETE("chats/{id}/")
    suspend fun deleteChat(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )
}

typealias GenericChatResponse<S> = CustomResponse<S, ApiError>