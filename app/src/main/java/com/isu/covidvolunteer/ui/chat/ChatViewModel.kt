package com.isu.covidvolunteer.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.covidvolunteer.api.GenericChatResponse
import com.isu.covidvolunteer.models.chat.AddChatDto
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.models.message.AddMessageDto
import com.isu.covidvolunteer.models.message.MessageDto
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.util.UserDetails
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    val liveData: MutableLiveData<GenericChatResponse<List<MessageDto>>> = MutableLiveData()
    // TODO: rename me

    val chats: MutableLiveData<GenericChatResponse<List<ChatDto>>> = MutableLiveData()
    val chat: MutableLiveData<GenericChatResponse<ChatDto>> = MutableLiveData()

    fun getChatMessages(id: Long) {
        viewModelScope.launch {
            liveData.value = repository.getChatMessages(
                "Bearer ${UserDetails.token?.token!!}",
                id
            )
        }
    }

    fun getChats() {
        viewModelScope.launch {
            chats.value = repository.getAllChats("Bearer ${UserDetails.token?.token!!}")
        }
    }

    fun startChat(userId: Long) {
        if (UserDetails.id != userId) {
            viewModelScope.launch {
                chat.value = repository.startChat(
                    "Bearer ${UserDetails.token?.token!!}",
                    AddChatDto(userId))
            }
        }
    }

    fun sendMessage(chatId: Long, message: String) {
        viewModelScope.launch {
            repository.sendMessage(
                "Bearer ${UserDetails.token?.token!!}",
                chatId, AddMessageDto(message))
        }
    }
}