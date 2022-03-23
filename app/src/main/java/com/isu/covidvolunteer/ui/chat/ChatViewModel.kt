package com.isu.covidvolunteer.ui.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.covidvolunteer.models.chat.AddChatDto
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.models.message.AddMessageDto
import com.isu.covidvolunteer.models.message.MessageDto
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.util.UserDetails
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    val liveData: MutableLiveData<List<MessageDto>> = MutableLiveData()
    // TODO: read token from property file

    val chats: MutableLiveData<List<ChatDto>> = MutableLiveData()
    val chat: MutableLiveData<ChatDto> = MutableLiveData()

    fun getChat(id: Long) {
        Log.d("GET_CHAT", "started")
        viewModelScope.launch {
            val messages = repository.getChat(
                "Bearer ${UserDetails.token?.token!!}",
                id
            )
            Log.d("GET_CHAT", "ended")
            liveData.value = messages
        }
    }

    fun getChats() {
        viewModelScope.launch {
            chats.value = repository.getAllChats("Bearer ${UserDetails.token?.token!!}")
        }
    }

    fun startChat(userId: Long) {
        Log.d("MY_TAG", userId.toString())
        viewModelScope.launch {
            repository.startChat("Bearer ${UserDetails.token?.token!!}", AddChatDto(userId))
        }
    }

    fun sendMessage(chatId: Long, message: String) {
        viewModelScope.launch {
            repository.sendMessage("Bearer ${UserDetails.token?.token!!}", chatId, AddMessageDto(message))
        }
    }
}