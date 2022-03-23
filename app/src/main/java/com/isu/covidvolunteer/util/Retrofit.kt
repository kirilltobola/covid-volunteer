package com.isu.covidvolunteer.util

import com.isu.covidvolunteer.api.AuthApi
import com.isu.covidvolunteer.api.ChatApi
import com.isu.covidvolunteer.api.OrderApi
import com.isu.covidvolunteer.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object Retrofit {
    // TODO: use properties file.
    // "http://10.0.2.2:8080/api/v1/"
    private const val BASE_URL = "https://slippery-tiger-35.loca.lt/api/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val chatApi: ChatApi by lazy {
        retrofit.create(ChatApi::class.java)
    }

    val orderApi: OrderApi by lazy {
        retrofit.create(OrderApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}