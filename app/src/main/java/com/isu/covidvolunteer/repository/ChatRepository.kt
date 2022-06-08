package com.isu.covidvolunteer.repository

import com.isu.covidvolunteer.api.ChatApi
import com.isu.covidvolunteer.api.GenericChatResponse
import com.isu.covidvolunteer.models.chat.AddChatDto
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.models.message.AddMessageDto
import com.isu.covidvolunteer.models.message.MessageDto
import com.isu.covidvolunteer.retrofit.SuccessResponse
import com.isu.covidvolunteer.util.Retrofit

class ChatRepository : ChatApi {
    override suspend fun getAllChats(token: String): GenericChatResponse<List<ChatDto>> {
        return Retrofit.chatApi.getAllChats(token)
    }

    override suspend fun getChatMessages(token: String, id: Long): GenericChatResponse<List<MessageDto>> {
        return Retrofit.chatApi.getChatMessages(token, id)
    }

    override suspend fun getChat(token: String, id: Long): GenericChatResponse<ChatDto> {
        return Retrofit.chatApi.getChat(token, id)
    }

    override suspend fun startChat(token: String, addChatDto: AddChatDto): GenericChatResponse<ChatDto> {
        return Retrofit.chatApi.startChat(token, addChatDto)
    }

    override suspend fun sendMessage(token: String, id: Long, messageDto: AddMessageDto): GenericChatResponse<SuccessResponse> {
        return Retrofit.chatApi.sendMessage(token, id, messageDto)
    }

    override suspend fun countMessages(token: String, id: Long): GenericChatResponse<Long> {
        return Retrofit.chatApi.countMessages(token, id)
    }

    // TODO: use it
    override suspend fun deleteChat(token: String, id: Long) {
        return Retrofit.chatApi.deleteChat(token, id)
    }
}