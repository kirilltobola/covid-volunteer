package com.isu.covidvolunteer.repository

import com.isu.covidvolunteer.api.ChatApi
import com.isu.covidvolunteer.models.chat.AddChatDto
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.models.message.AddMessageDto
import com.isu.covidvolunteer.models.message.MessageDto
import com.isu.covidvolunteer.util.Retrofit

class ChatRepository : ChatApi {
    override suspend fun getAllChats(token: String): List<ChatDto> {
        return Retrofit.chatApi.getAllChats(token)
    }

    override suspend fun getChat(token: String, id: Long): List<MessageDto> {
        return Retrofit.chatApi.getChat(token, id)
    }

    override suspend fun startChat(token: String, addChatDto: AddChatDto) {
        return Retrofit.chatApi.startChat(token, addChatDto)
    }

    override suspend fun sendMessage(token: String, id: Long, messageDto: AddMessageDto) {
        return Retrofit.chatApi.sendMessage(token, id, messageDto)
    }

    override suspend fun deleteChat(token: String, id: Long) {
        return Retrofit.chatApi.deleteChat(token, id)
    }
}