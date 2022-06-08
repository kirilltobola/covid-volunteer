package com.isu.covidvolunteer.models.chat

import com.fasterxml.jackson.annotation.JsonProperty

data class AddChatDto(@JsonProperty("userId") val userId: Long)